package org.pinae.pumbaa.data.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pinae.pumbaa.data.file.FileTools;

/**
 * 文本文件工具
 * 
 * @author Huiyugeng
 * 
 */
public class TextFile {

	private FileInputStream fis = null;
	private InputStreamReader isr = null;
	private BufferedReader br = null;

	/**
	 * 文件内容读取
	 * 
	 * @param filename 需要访问的文件名
	 * @param encoding 文件编码
	 * @param size 读取的行数
	 * 
	 * @return 文件内容列表
	 */
	public List<String> read(String filename, String encoding, int size) {
		List<String> content = new ArrayList<String>();

		open(filename, encoding);
		try {
			String line = null;
			if (size <= 0) {
				size = 1;
			}
			for (int i = 0; i < size; i++) {
				if ((line = br.readLine()) != null) {
					content.add(line);
				} else {
					break;
				}
			}
		} catch (IOException e) {

		} finally {
			close();
		}

		return content;
	}

	/**
	 * 文件内容读取
	 * 
	 * @param filename 需要访问的文件名
	 * @param encoding 文件编码
	 * 
	 * @return 文件内容列表
	 */
	public List<String> read(String filename, String encoding) {
		List<String> content = new ArrayList<String>();

		open(filename, encoding);
		try {

			String line = null;
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} catch (IOException e) {

		} finally {
			close();
		}

		return content;
	}

	/**
	 * 文件行数统计
	 * 
	 * @param filename 需要统计的文件名
	 * @param encoding 文件编码
	 * 
	 * @return 文件行数
	 */
	public long size(String filename, String encoding) {
		long length = 0;

		open(filename, encoding);
		try {

			while (br.readLine() != null) {
				length = length + 1;
			}
		} catch (IOException e) {

		} finally {
			close();

		}

		return length;
	}

	/**
	 * 文件内容写入（新文件模式）
	 * 
	 * @param filename 需要写入的文件名
	 * @param encoding 文件编码
	 * @param content 需要写入的内容列表
	 */
	public void write(String filename, String encoding, List<String> content) {
		try {
			write(filename, encoding, content, false);
		} catch (IOException e) {

		}
	}

	/**
	 * 文件内容写入（追加模式）
	 * 
	 * @param filename 需要写入的文件名
	 * @param encoding 文件编码
	 * @param content 需要写入的内容列表
	 * @param append 采用追加模式写入
	 */
	public void write(String filename, String encoding, List<String> content,
			boolean append) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osr = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(filename, append);
			osr = new OutputStreamWriter(fos, encoding);
			bw = new BufferedWriter(osr);

			for (String line : content) {
				bw.write(line + "\n");
			}

			bw.flush();
			osr.flush();
			fos.flush();
		} catch (IOException e) {

		} finally {
			if (bw != null) {
				bw.close();
			}
			if (osr != null) {
				osr.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 文件切分
	 * 
	 * @param filename 需要切分的文件名
	 * @param encoding 文件编码
	 * @param path 切分后文件保存路径
	 * @param extFilename 切分后文件扩展名
	 * @param splitSize 文件切分长度
	 */
	public void split(String filename, String encoding, String path, String extFilename, long splitSize) {

		File file = new File(filename);
		String name = file.getName();

		long splitFileCount = 1;

		open(filename, encoding);
		try {
			List<String> content = new ArrayList<String>();

			String line = null;
			while ((line = br.readLine()) != null) {
				content.add(line);

				if (content.size() >= splitSize) {

					write(path + File.separator + name + "_" + Long.toString(splitFileCount) + "." + extFilename, encoding, content);
					content.clear();

					splitFileCount++;
				}
			}
		} catch (IOException e) {

		} finally {
			close();
		}
	}

	/**
	 * 文件合并
	 * 
	 * @param filename 合并后文件名
	 * @param encoding 合并的文件编码
	 * @param path 文件列表路径
	 */
	public void join(String filename, String encoding, String path) {
		List<String> fileList = FileTools.getFileList(path);
		join(filename, encoding, fileList);
	}

	/**
	 * 文件合并
	 * 
	 * @param filename 合并后文件名
	 * @param encoding 合并的文件编码
	 * @param fileList 需要合并的文件列表
	 */
	public void join(String filename, String encoding, List<String> fileList) {
		List<String> result = new ArrayList<String>();

		for (String file : fileList) {
			List<String> content = read(file, encoding);

			result.addAll(content);
		}

		write(filename, encoding, result);
	}

	/**
	 * 文件内容过滤
	 * 
	 * @param filename 文件名
	 * @param encoding 文件编码
	 * @param filteList 需要过滤的内容（过滤内容将从文件中删去）
	 */
	public void filte(String filename, String encoding, List<String> filteList) {
		List<String> content = new ArrayList<String>();

		open(filename, encoding);

		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				boolean matched = false;
				for (String filte : filteList) {
					line = line.trim();
					if (line.matches(filte)) {
						matched = true;
						break;
					}
				}
				if (!matched) {
					content.add(line);
				}
			}
		} catch (IOException e) {

		} finally {
			close();
		}
		write(filename, encoding, content);
	}

	/**
	 * 文件内容定位
	 * 
	 * @param filename 文件名
	 * @param encoding 文件编码
	 * @param filteList 需要定位的内容（支持正则表达式）
	 * 
	 * @return 文件匹配内容的行数
	 */
	public List<Long> locate(String filename, String encoding, List<String> filteList) {
		List<Long> locateList = new ArrayList<Long>();
		
		open(filename, encoding);

		try {
			long lineNum = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				boolean matched = false;
				for (String filte : filteList) {
					line = line.trim();
					if (line.matches(filte)) {
						matched = true;
						break;
					}
				}
				if (!matched) {
					locateList.add(lineNum);
				}
				lineNum++;
			}
		} catch (IOException e) {

		} finally {
			close();
		}
		
		return locateList;
	}
	
	/**
	 * 文件名变更
	 * 
	 * @param path 文件路径（需要变更文件名的路径）
	 * @param prefix 文件名前缀
	 * @param extFilename 文件扩展名
	 * 
	 */
	public void rename(String path, String prefix, String extFilename) {
		List<String> fileList = FileTools.getFileList(path);
		for (int i = 1 ; i <= fileList.size(); i++) {
			File file = new File(fileList.get(i));
			if (file.isFile()) {
				String newFilename = String.format("%s_%s.%s", prefix, Integer.toString(i), extFilename);
				file.renameTo(new File(newFilename));
			}
		}
	}

	/*
	 * 文件打开，默认采用UTF8
	 */
	private void open(String filename, String encoding) {
		if (StringUtils.isEmpty(encoding)) {
			encoding = "UTF8";
		}
		try {
			this.fis = new FileInputStream(filename);
			this.isr = new InputStreamReader(fis, encoding);
			this.br = new BufferedReader(isr);

		} catch (IOException e) {

		}
	}

	/*
	 * 文件关闭 
	 */
	private void close() {
		try {
			if (this.br != null) {
				this.br.close();
			}
			if (this.isr != null) {
				this.isr.close();
			}
			if (this.fis != null) {
				this.fis.close();
			}
		} catch (IOException e) {

		}
	}
}
