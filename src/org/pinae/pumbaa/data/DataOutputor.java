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
import org.pinae.pumbaa.data.excel.ExcelStyle;
import org.pinae.pumbaa.data.xml.XMLFile;

/**
 * 数据输出
 * 
 * @author Linjianyu
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
			log.error(String.format("outputNDB Exception: exception=%s", e.getMessage()));
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
			styleItem.put(ExcelStyle.CELL_NAME, column);
			styleItem.put(ExcelStyle.CELL_TITLE, column);
			styleItem.put(ExcelStyle.CELL_WIDTH, "5000");
			style.add(styleItem);
		}

		Map<String, Object> sheet = new HashMap<String, Object>();
		sheet.put(ExcelStyle.SHEET_LABEL, tableName);
		sheet.put(ExcelStyle.ROW_STYLE, style);
		sheet.put(ExcelStyle.DATA, table);
		sheetList.add(sheet);

		try {
			new ExcelFile().write(filename, sheetList);
		} catch (Exception e) {
			log.error(String.format("outputExcel Exception: exception=%s", e.getMessage()));
		}
	}

	/**
	 * 输出Excel格式
	 * 
	 * @param filename 输出Excel文件名
	 * @param sheet 表格信息
	 */
	public void outputExcel(String filename, Sheet sheet) {
		if (sheet != null) {
			outputExcel(filename, sheet.getTableName(), sheet.getColumns(), sheet.getTable());
		} else {
			log.error("Empty sheet config");
		}

	}

	/**
	 * 输出Excel格式（输出多个表格）
	 * 
	 * @param filename 输出Excel文件名
	 * @param sheets 表格信息列表
	 */
	public void outputExcel(String filename, Sheet[] sheets) {
		List<Map<String, Object>> sheetMapList = new ArrayList<Map<String, Object>>();

		if (sheets != null && sheets.length > 0) {
			for (Sheet sheet : sheets) {

				String tableName = sheet.getTableName();
				String columns[] = sheet.getColumns();
				List<Map<String, Object>> table = sheet.getTable();

				List<Map<String, String>> style = new ArrayList<Map<String, String>>();
				for (String column : columns) {
					Map<String, String> styleItem = new HashMap<String, String>();
					styleItem.put(ExcelStyle.CELL_NAME, column);
					styleItem.put(ExcelStyle.CELL_TITLE, column);
					styleItem.put(ExcelStyle.CELL_WIDTH, "5000");
					style.add(styleItem);
				}

				Map<String, Object> sheetMap = new HashMap<String, Object>();
				sheetMap.put(ExcelStyle.SHEET_LABEL, tableName);
				sheetMap.put(ExcelStyle.ROW_STYLE, style);
				sheetMap.put(ExcelStyle.DATA, table);
				sheetMapList.add(sheetMap);
			}

			try {
				new ExcelFile().write(filename, sheetMapList);
			} catch (Exception e) {
				log.error(String.format("outputExcel Exception: exception=%s", e.getMessage()));
			}
		} else {
			log.error("Empty sheet config");
		}
	}

	public static class Sheet {
		private String tableName;
		private String columns[];
		private List<Map<String, Object>> table;

		public Sheet(){
			
		}
		
		public Sheet(String tableName, String columns[], List<Map<String, Object>> table) {
			this.tableName = tableName;
			this.columns = columns;
			this.table = table;
		}
		
		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String[] getColumns() {
			return columns;
		}

		public void setColumns(String[] columns) {
			this.columns = columns;
		}

		public List<Map<String, Object>> getTable() {
			return table;
		}

		public void setTable(List<Map<String, Object>> table) {
			this.table = table;
		}

	}
}
