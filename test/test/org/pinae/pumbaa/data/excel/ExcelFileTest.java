package test.org.pinae.pumbaa.data.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.data.excel.ExcelFile;
import org.pinae.pumbaa.data.excel.ExcelStyle;

public class ExcelFileTest {
	
	@Test
	public void testWrite() {
		ExcelFile excel = new ExcelFile();
		String filename = "d:\\text.xlsx";
		
		
		Map<String, String> sheetStyle = new HashMap<String, String>();
		sheetStyle.put(ExcelStyle.SHEET_HEAD, "测试表头");
		sheetStyle.put(ExcelStyle.TITLE_HEIGHT, "500");
		sheetStyle.put(ExcelStyle.ROW_HEIGHT, "500");
		
		List<Map<String, String>> rowStyle = new ArrayList<Map<String, String>>();
		
		Map<String, String> styleItem1 = new HashMap<String, String>();
		styleItem1.put(ExcelStyle.CELL_NAME, "name");
		styleItem1.put(ExcelStyle.CELL_TITLE, "姓名");
		styleItem1.put(ExcelStyle.CELL_WIDTH, "5000");
		rowStyle.add(styleItem1);
		
		Map<String, String> styleItem2 = new HashMap<String, String>();
		styleItem2.put(ExcelStyle.CELL_NAME, "age");
		styleItem2.put(ExcelStyle.CELL_TITLE, "年龄");
		styleItem2.put(ExcelStyle.CELL_WIDTH, "2000");
		rowStyle.add(styleItem2);
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> row1 = new HashMap<String, Object>();
		row1.put("name", "Linjianyu");
		row1.put("age", 30);
		data.add(row1);
		
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("name", "wangren");
		row2.put("age", 27);
		data.add(row2);
		
		List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> sheet1 = new HashMap<String, Object>();
		sheet1.put(ExcelStyle.SHEET_LABEL, "sheet1");
		sheet1.put(ExcelStyle.SHEET_STYLE, sheetStyle);
		sheet1.put(ExcelStyle.ROW_STYLE, rowStyle);
		sheet1.put(ExcelStyle.DATA, data);
		
		Map<String, Object> sheet2 = new HashMap<String, Object>();
		sheet2.put(ExcelStyle.SHEET_LABEL, "sheet2");
		sheet2.put(ExcelStyle.ROW_STYLE, rowStyle);
		sheet2.put(ExcelStyle.DATA, data);
		
		sheetList.add(sheet1);
		sheetList.add(sheet2);
		try {
			excel.write(filename, sheetList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
