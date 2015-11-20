package org.pinae.pumbaa.data.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel文件工具
 * 
 * @author Linjianyu
 * 
 */
public class ExcelFile {
	
	private Font font;
	private CellStyle style;

	/*
	 * 构建标题单元格样式
	 */
	private CellStyle getSheetTitleStyle(Workbook workbook) {
		// 设置表头字体
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 220);

		// 设置标题格式
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		style.setFont(font);// 设置字体

		return style;
	}

	/*
	 * 构建表头单元格样式
	 */
	private CellStyle getColumnTitleStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeight((short) 200);

		// 设置标题格式
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

		// 设置边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);

		style.setFont(font);// 设置字体

		return style;
	}

	/*
	 * 构建一般单元格样式
	 */
	private CellStyle getCellStyle(Workbook workbook) {

		if (style == null) {
			// 设置字体
			font = workbook.createFont();
			font.setFontName("宋体");
			font.setFontHeight((short) 200);

			// 设置单元格格式
			style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setWrapText(true);

			// 设置边框
			style.setBottomBorderColor(HSSFColor.BLACK.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);

			style.setFont(font);// 设置字体
		}

		return style;
	}

	/**
	 * 创建Excel文件
	 * 
	 * @param filename Excel文件
	 * @param title Sheet标题
	 * @param style Sheet列格式(name: 列名称,与data中数据对应, title：Sheet中显示的标题，width：列宽度)
	 * @param data Sheet数据
	 */
	@SuppressWarnings("unchecked")
	public void write(String filename, List<Map<String, Object>> sheetList) throws Exception {

		Workbook workbook = new XSSFWorkbook();

		for (Map<String, Object> sheetMap : sheetList) {

			if (sheetMap != null && sheetMap.containsKey(ExcelStyle.SHEET_LABEL) && sheetMap.containsKey(ExcelStyle.ROW_STYLE)) {

				String sheetTitle = (String) sheetMap.get(ExcelStyle.SHEET_LABEL);

				Map<String, String> sheetStyle = sheetMap.containsKey(ExcelStyle.SHEET_STYLE) ? (Map<String, String>) sheetMap
						.get(ExcelStyle.SHEET_STYLE) : new HashMap<String, String>();
				List<Map<String, String>> rowStyle = (List<Map<String, String>>) sheetMap.get(ExcelStyle.ROW_STYLE);
				List<Map<String, Object>> data = (sheetMap.containsKey(ExcelStyle.DATA)) ? (List<Map<String, Object>>) sheetMap
						.get(ExcelStyle.DATA) : new ArrayList<Map<String, Object>>();

				Sheet sheet = workbook.createSheet(sheetTitle);

				List<String> columnNameList = new ArrayList<String>();
				List<String> columnTitleList = new ArrayList<String>();
				List<Integer> columnWidthList = new ArrayList<Integer>();

				for (Map<String, String> styleItem : rowStyle) {
					if (styleItem.containsKey(ExcelStyle.CELL_NAME)) {
						columnNameList.add(styleItem.get(ExcelStyle.CELL_NAME));
					}
					if (styleItem.containsKey(ExcelStyle.CELL_TITLE)) {
						columnTitleList.add(styleItem.get(ExcelStyle.CELL_TITLE));
					}
					if (styleItem.containsKey(ExcelStyle.CELL_WIDTH)) {
						String width = styleItem.get(ExcelStyle.CELL_WIDTH);
						if (StringUtils.isNumeric(width)) {
							columnWidthList.add(Integer.parseInt(width));
						}
					}
				}

				createTitle(workbook, sheet, sheetTitle, sheetStyle, columnNameList, columnTitleList, columnWidthList);

				//设置行高
				short rowHeight = 300;
				if (sheetStyle.containsKey(ExcelStyle.ROW_HEIGHT)) {
					rowHeight = Short.parseShort(sheetStyle.get(ExcelStyle.ROW_HEIGHT));
				}
				
				//设置行内容
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> item = data.get(i);

					if (item == null) {
						continue;
					}
					//构建内容行(2 包括标题栏和表头)
					Row row = sheet.createRow(i + 2);
					row.setHeight(rowHeight);

					for (int j = 0; j < columnNameList.size(); j++) {
						Object value = item.containsKey(columnNameList.get(j)) ? item.get(columnNameList.get(j)) : "";

						Cell cell = row.createCell(j);

						cell.setCellStyle(getCellStyle(workbook));
						if (value instanceof Short || value instanceof Integer || value instanceof Long
								|| value instanceof Double) {
							cell.setCellValue(Double.parseDouble(value.toString()));
						} else if (value instanceof String) {
							cell.setCellValue(value.toString());
						} else if (value instanceof Date) {
							cell.setCellValue((Date) value);
						}

					}
				}
			}
		}

		OutputStream os = new FileOutputStream(filename);
		workbook.write(os);
		os.close();
	}

	/*
	 * 构建表头
	 * 
	 * @param sheet 构建的表格
	 */
	private void createTitle(Workbook workbook, Sheet sheet, String sheetTitle, Map<String, String> sheetStyle, List<String> columnNameList,
			List<String> columnTitleList, List<Integer> columnWidthList) {
		
		//设置标题栏高
		short titleHeight = 300;
		if (sheetStyle.containsKey(ExcelStyle.TITLE_HEIGHT)) {
			titleHeight = Short.parseShort(sheetStyle.get(ExcelStyle.TITLE_HEIGHT));
		}
		//设置标题内容
		String sheetHead = sheetTitle;
		if (sheetStyle.containsKey(ExcelStyle.SHEET_HEAD)) {
			sheetHead = sheetStyle.get(ExcelStyle.SHEET_HEAD);
		}
		
		//设置表头高
		short rowHeight = 300;
		if (sheetStyle.containsKey(ExcelStyle.ROW_HEIGHT)) {
			rowHeight = Short.parseShort(sheetStyle.get(ExcelStyle.ROW_HEIGHT));
		}
		
		
		//构建标题栏
		Row sheetTitleRow = sheet.createRow(0);
		sheetTitleRow.setHeight(titleHeight);

		// 合并标题栏
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNameList.size() - 1));

		Cell cell = sheetTitleRow.createCell(0);
		cell.setCellStyle(getSheetTitleStyle(workbook));
		cell.setCellValue(sheetHead);

		//构建表头
		Row columnTitleRow = sheet.createRow(1);
		columnTitleRow.setHeight(rowHeight);

		for (int i = 0; i < columnTitleList.size(); i++) {
			cell = columnTitleRow.createCell(i);
			cell.setCellStyle(getColumnTitleStyle(workbook));
			cell.setCellValue(columnTitleList.get(i));
		}

		for (int i = 0; i < columnWidthList.size(); i++) {
			sheet.setColumnWidth(i, columnWidthList.get(i));
		}
	}

	/**
	 * 读取Excel文件
	 * 
	 * @param filename Excel文件名
	 * @param sheetIndex Sheet编号
	 * @param titleRow 标题列的高度(如果为0，则表明没有标题行)
	 * @param columns 列名
	 * 
	 * @return 读取后的Excel数据
	 */
	public List<Map<String, Object>> read(String filename, int sheetIndex, int titleRow, String[] columns) {
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();

		InputStream fileStream = null;
		try {
			fileStream = new FileInputStream(filename);
			if (filename.endsWith("xlsx")) {
				Workbook wb = new XSSFWorkbook(fileStream);
				Sheet sheet = wb.getSheetAt(sheetIndex);

				int rowCount = 0;

				Iterator<Row> rows = sheet.iterator();
				while (rows.hasNext()) {
					Row row = rows.next();

					rowCount++;
					
					//过滤标题行
					if (titleRow <= rowCount) {
						continue;
					}

					Map<String, Object> item = new HashMap<String, Object>();
					for (int i = 0; i < columns.length; i++) {
						item.put(columns[i], getCellValue(row, i));
					}
					table.add(item);

				}

				IOUtils.closeQuietly(fileStream);

				return table;
			} else {

			}
		} catch (Exception e) {

		} finally {
			IOUtils.closeQuietly(fileStream);
		}

		return table;
	}

	private Object getCellValue(Row row, int index) {
		Cell cell = row.getCell(index);
		Object value = null;
		if (cell instanceof XSSFCell) {
			XSSFCell xssfCell = (XSSFCell) cell;
			if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				value = (int) xssfCell.getNumericCellValue();
			} else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				value = xssfCell.getStringCellValue();
			}
		}

		return value;
	}

}
