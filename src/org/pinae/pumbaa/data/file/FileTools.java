package org.pinae.pumbaa.data.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
 * @author Huiyugeng
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
		return result;
	}
}
