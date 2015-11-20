package org.pinae.pumbaa.data.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * CSV文件工具
 * 
 * @author Linjianyu
 *
 */
public class CSVFile {
	/**
	 * 读取CSV文件
	 * 使用逗号作为字段分隔符,使用冒号作为引用符
	 * 
	 * @param filename CSV文件
	 * @param encoding CSV文件编码
	 * @param columns CSV文件中的字段顺序
	 * 
	 * @return CSV文件中数据List<<Column, Data>>
	 */
	public List<Map<String, Object>> read(String filename, String encoding, String columns[]) {
		return read(filename, encoding, columns, ',', '"');
	}
	
	/**
	 * 读取CSV文件
	 * 
	 * @param filename CSV文件
	 * @param encoding CSV文件编码
	 * @param columns CSV文件中的字段顺序
	 * @param separator CSV字段分隔符
	 * @param quotechar CSV字段引用符
	 * 
	 * @return CSV文件中数据List<<Column, Data>>
	 */
	public List<Map<String, Object>> read(String filename, String encoding, String columns[], char separator, char quotechar) {
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(filename) && columns != null && columns.length != 0) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));
				CSVReader reader = new CSVReader(br, separator, quotechar);
				String line[] = null;
				while ((line = reader.readNext()) != null) {
					if (line.length <= columns.length) {
						Map<String, Object> row = new HashMap<String, Object>();
						for (int i = 0; i < columns.length ; i++) {
							String column = columns[i];
							if (column != null) {
								String data = line[i];
								row.put(column, StringUtils.defaultString(data, ""));
							}
						}
						table.add(row);
					}
				}
				reader.close();
			} catch (IOException e) {
	
			}
		}
		return table;
	}
	
	/**
	 * 写入CSV文件
	 * 使用逗号作为字段分隔符,使用冒号作为引用符
	 * 
	 * @param filename CSV文件
	 * @param encoding CSV文件编码
	 * @param columns CSV的字段顺序
	 * @param table 需要写入的数据List<<Column, Data>>
	 * 
	 */
	public void write(String filename, String encoding, String columns[], List<Map<String, Object>> table) {
		write(filename, encoding, columns, table, ',', '"');
	}
	
	/**
	 * 写入CSV文件
	 * 
	 * @param filename CSV文件
	 * @param encoding CSV文件编码
	 * @param columns CSV的字段顺序
	 * @param table 需要写入的数据List<<Column, Data>>
	 * @param separator CSV字段分隔符
	 * @param quotechar CSV字段引用符
	 */
	public void write(String filename, String encoding, String columns[], List<Map<String, Object>> table, char separator, char quotechar) {
		if (StringUtils.isNotEmpty(filename) && columns != null && columns.length != 0) {
			try {
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filename), encoding), separator, quotechar);
				writer.writeNext(columns);
				for (Map<String, Object> row : table) {
					if (row != null) {
						String[] line = new String[columns.length];
						for (int i = 0; i < columns.length ; i++) {
							String column = columns[i];
							Object data = row.get(column);
							data = data == null ? "" : data;	
							line[i] = data.toString();
						}
						writer.writeNext(line);
					}
				}
				writer.close();
			} catch (IOException e) {
				
			}
		}
	}
	
	
	/**
	 * 写入CSV文件
	 * 
	 * @param filename CSV文件
	 * @param encoding CSV文件编码
	 * @param content CSV文件内容
	 * @param separator CSV字段分隔符
	 * @param quotechar CSV字段引用符
	 */
	public void write(String filename, String encoding, List<String[]> content, char separator, char quotechar) {
		if (StringUtils.isNotEmpty(filename) && content != null && content.size() != 0) {
			try {
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filename), encoding), separator, quotechar);
				writer.writeAll(content);
				writer.close();
			} catch (IOException e) {
				
			}
		}
	}
}
