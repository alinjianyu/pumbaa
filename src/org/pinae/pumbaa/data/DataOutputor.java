package org.pinae.pumbaa.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.pinae.ndb.Statement;
import org.pinae.pumbaa.data.csv.CSVFile;
import org.pinae.pumbaa.data.excel.ExcelFile;
import org.pinae.pumbaa.data.xml.XMLFile;

/**
 * 数据输出
 * 
 * @author Huiyugeng
 *
 */
public class DataOutputor {
	private static Logger log = Logger.getLogger(DataOutputor.class);
	
	/**
	 * 输出XML格式
	 * 
	 * @param filename 输出XML文件名
	 * @param table 数据表内容
	 */
	public void outputXML(String filename, String encoding, List<Map<String, Object>> table) {
		XMLFile file = new XMLFile();
		Map<String, Object> xml = new HashMap<String, Object>();
		xml.put("data", table);
		file.write(filename, xml, encoding);
	}
	
	/**
	 * 输出CSV格式
	 * 
	 * @param filename 输出CSV文件名
	 * @param columns 数据字段
	 * @param table 数据表内容
	 */
	public void outputCSV(String filename, String columns[], List<Map<String, Object>> table) {
		CSVFile file = new CSVFile();
		file.write(filename, "GBK", columns, table);
	}
	
	/**
	 * 输出CSV格式
	 * 
	 * @param filename 输出CSV文件名
	 * @param encoding CSV文件编码
	 * @param columns 数据字段
	 * @param table 数据表内容
	 */
	public void outputCSV(String filename, String encoding, String columns[], List<Map<String, Object>> table) {
		CSVFile file = new CSVFile();
		file.write(filename, encoding, columns, table);
	}
	
	/**
	 * 输出NDB格式
	 * 
	 * @param filename 输出NDB格式文件
	 * @param table
	 */
	public void outputNDB(String filename, List<Map<String, Object>> table) {
		Statement statement = new Statement();
		Map<String, Object> ndb = new HashMap<String, Object>();
		ndb.put("data", table);
		try {
			statement.write(filename, "data", ndb);
		} catch (IOException e) {
			log.error(String.format("Output NDB File Exception: exception=%s", e.getMessage()));
		}
	}
	
	/**
	 * 输出Excel格式
	 * 
	 * @param filename 输出Excel文件名
	 * @param tableName 数据表名
	 * @param columns 数据字段
	 * @param table 数据表内容
	 */
	public void outputExcel(String filename, String tableName, String columns[], List<Map<String, Object>> table) {
		List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
		
		List<Map<String, String>> style = new ArrayList<Map<String, String>>();
		for (String column : columns) {
			Map<String, String> styleItem = new HashMap<String, String>();
			styleItem.put("name", column);
			styleItem.put("title", column);
			styleItem.put("width", "5000");
			style.add(styleItem);
		}
		
		Map<String, Object> sheet = new HashMap<String, Object>();
		sheet.put("title", tableName);
		sheet.put("row_style", style);
		sheet.put("data", table);
		sheetList.add(sheet);
		
		try {
			new ExcelFile().write(filename, sheetList);
		} catch (Exception e) {
			log.error(String.format("Output Excel File Exception: exception=%s", e.getMessage()));
		}
	}
	
}
