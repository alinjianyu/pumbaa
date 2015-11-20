package test.org.pinae.pumbaa.analysis.assoc;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.analysis.assoc.MapAssoc;

public class MapAssocTest {
	
	@Test
	public void testAssoc(){
		
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("name", "hui");
		data1.put("age", "30");
		data1.put("company", "letour");
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("company_name", "letour");
		data2.put("locate", "beijing");
		
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put("user", "hui");
		data3.put("password", "13630183186");
		
		MapAssoc assoc = new MapAssoc();
		Map<String, Object> data4 = (Map<String, Object>)assoc.assoc(data1, data2, "company", "company_name", "equals");
		data4 = (Map<String, Object>)assoc.assoc(data4, data3, "name", "user", "equals");
		
		assertEquals(data4.size(), 7);
		
	}

}
