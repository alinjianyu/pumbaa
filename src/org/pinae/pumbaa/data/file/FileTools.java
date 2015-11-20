package org.pinae.pumbaa.data.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件处理工具
 * 
 * @author Linjianyu
 * 
 */
public class FileTools {
	/**
	 * 获取文件列表
	 * 
	 * @param path 文件路径
	 * 
	 * @return 文件列表（全路径列表）
	 */
	public static List<String> getFileList(String path) {

		List<String> fileList = new ArrayList<String>();

		File file = new File(path);
		if (file.isDirectory()) {
			String filenames[] = file.list();
			for (int i = 0; i < filenames.length; i++) {
				fileList.add(path + File.separator + filenames[i]);
			}
		} else if (file.isFile()) {
			fileList.add(path);
		}

		return fileList;
	}

	/**
	 * 文件分组
	 * 
	 * @param path 文件列表路径
	 * @param pattern 文件分组模式
	 * 
	 * @return 分组后的文件<分组模式, 本组的文件列表>
	 */
	public static Map<String, List<String>> groupFile(String path, String pattern) {
		Map<String, List<String>> fileGroup = new HashMap<String, List<String>>();

		Pattern p = Pattern.compile(pattern);

		File filePath = new File(path);
		if (filePath.isDirectory()) {
			String filenames[] = filePath.list();
			for (String filename : filenames) {
				Matcher matcher = p.matcher(filename);
				while (matcher.find()) {
					String key = "";
					for (int i = 1; i <= matcher.groupCount(); i++) {
						String value = matcher.group(i);
						if (StringUtils.isNotEmpty(value)) {
							key = key + value;
						}
					}

					if (StringUtils.isNotEmpty(key)) {
						List<String> fileList = new ArrayList<String>();
						if (fileGroup.containsKey(key)) {
							fileList = fileGroup.get(key);
						}
						fileList.add(path + File.separator + filename);

						fileGroup.put(key, fileList);
					}
				}
			}
		} else if (filePath.isFile()) {
			List<String> fileList = new ArrayList<String>();
			fileGroup.put("default", fileList);
		}

		return fileGroup;
	}

	/**
	 * 删除文件集合
	 * 
	 * @param filenames 文件路径列表(支持文件与目录)
	 * 
	 * @return 是否删除成功
	 */
	public static boolean deleteFileSet(Collection<String> filenames) {
		boolean result = true;
		if (filenames != null) {
			for (String filename : filenames) {
				try {
					File file = new File(filename);
					if (file.exists()) {
						if (file.isFile()) {
	
							FileUtils.forceDelete(file);
	
						} else if (file.isDirectory()) {
							FileUtils.deleteDirectory(file);
						}
					}
				} catch (IOException e) {
					return false;
				}
			}
		} else {
			result = false;
		}
		return result;
	}
	
	/**
	 * 重命名文件集合
	 * 
	 * @param filenames 文件名列表
	 * @param pattern 文件名命名规范
	 * 					{count}代表文件计数器
	 * 					{date}代表当前日期, yyyy-MM-dd
	 * 					{time}代表当前时间, HH-mm-ss
	 *					{timestamp}代表当前时间戳
	 *
	 * @return 是否重命名成功
	 */
	public static boolean renameFileSet(Collection<String> filenames, String pattern) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
		
		boolean result = false;
		if (filenames != null && pattern != null) {
			long count = 1;
			try {
				for (String filename : filenames) {
					if (StringUtils.isNotEmpty(filename)) {
						File file = new File(filename);
						if (file.isFile()) {
							String path = file.getParent();
							Date now = new Date();
							
							String newFilename = pattern;
							newFilename = newFilename.replaceAll("\\{count\\}", Long.toString(count));
							newFilename = newFilename.replaceAll("\\{date\\}", dateFormat.format(now));
							newFilename = newFilename.replaceAll("\\{time\\}", timeFormat.format(now));
							newFilename = newFilename.replaceAll("\\{timestamp\\}", Long.toString(now.getTime()));
							
							File newFile = new File(path + File.separator + newFilename);
							result = file.renameTo(newFile);
						}
					}
					count++;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return result;
	}
	
	/**
	 * 拷贝文件集合
	 * 
	 * @param filenames 文件名列表
	 * @param destPath 目标目录
	 * 
	 * @return 是否拷贝成功
	 */
	public static boolean copyFileSet(Collection<String> filenames, String destPath) {
		boolean result = false;
		if (filenames != null && StringUtils.isNotEmpty(destPath)) {
			try {
				File destDir = new File(destPath);
				if (destDir.isDirectory()) {
					for (String filename : filenames) {
						if (StringUtils.isNotEmpty(filename)) {
							File srcFile = new File(filename);
							if (srcFile.isFile()) {
								FileUtils.copyFileToDirectory(srcFile, destDir);
							} else if (srcFile.isDirectory()) {
								FileUtils.copyDirectory(srcFile, destDir);
							}
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
		}
		return result;
	}
}
