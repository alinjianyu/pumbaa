package test.org.pinae.pumbaa.analysis.mapping;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.pinae.pumbaa.analysis.mapping.Mapping;
import org.pinae.pumbaa.analysis.mapping.RegexMapping;

public class RegexMappingTest {
	
	@Test
	public void testMap() {
		String[] columns = new String[]{"MASTER_ACC", "USER_NAME", "SRC_IP", "DST_IP", "SLAVE_ACC"};
		Map<String, String> pattern = new HashMap<String, String>();
		pattern.put("SQL-LOG", "(.*)\\|\\|\\|(.*)\\|\\|\\|(.*)\\|\\|\\|(.*)\\|\\|\\|(.*)");
		
		
		Map<String, String[]> item = new TreeMap<String, String[]>();
		for (int i = 0; i < columns.length; i++) {
			item.put(Integer.toString(i + 1), new String[]{"match", columns[i]});
		}
		Map<String, Map<String, String[]>> items = new HashMap<String, Map<String, String[]>>();
		items.put("SQL-LOG", item);
		
		Mapping mapping = new RegexMapping(pattern, items);
		
		Map<String, String> result = mapping.map("dwlishunan ||| 李树南 |||  10.243.224.242 |||  10.243.178.102 |||  lisn ");
		
		assertEquals(result.get("MASTER_ACC"), "dwlishunan");
		assertEquals(result.get("USER_NAME"), "李树南");
		assertEquals(result.get("SRC_IP"), "10.243.224.242");
		assertEquals(result.get("DST_IP"), "10.243.178.102");
		assertEquals(result.get("SLAVE_ACC"), "lisn");
	}
}
