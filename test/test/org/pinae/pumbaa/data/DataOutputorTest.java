package test.org.pinae.pumbaa.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.pumbaa.data.DataOutputor;
import org.pinae.pumbaa.data.DataOutputor.Sheet;

public class DataOutputorTest {
	private List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
	
	@Before
	public void setUp() {
		Map<String, Object> row1 = new HashMap<String, Object>();
		row1.put("name", "Linjianyu");
		row1.put("age", 30);
		table.add(row1);
		
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("name", "roger");
		row2.put("age", 29);
		table.add(row2);
	}
	
	@Test
	public void testOutputXML() {
		DataOutputor outputor = new DataOutputor();
		outputor.outputXML("test.xml", "utf-8", table);
	}
	
	@Test
	public void testOutputCSV() {
		DataOutputor outputor = new DataOutputor();
		outputor.outputCSV("test.csv", new String[]{"name", "age"} , table);
	}
	
	@Test
	public void testOutputNDB() {
		DataOutputor outputor = new DataOutputor();
		outputor.outputNDB("test.ndb", table);
	}
	
	@Test
	public void testOutputExcel1() {
		DataOutputor outputor = new DataOutputor();
		outputor.outputExcel("test.xlsx", "user", new String[]{"name", "age"} , table);
	}
	
	@Test
	public void testOutputExcel2() {
		DataOutputor outputor = new DataOutputor();
		Sheet[] sheets = new Sheet[2];
		
		sheets[0] = new Sheet("User", new String[]{"name", "age"} , table);
		sheets[1] = new Sheet("用户", new String[]{"name", "age"} , table);
		
		outputor.outputExcel("d://test.xlsx", sheets);
	}
}
