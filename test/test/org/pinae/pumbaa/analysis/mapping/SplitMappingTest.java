package test.org.pinae.pumbaa.analysis.mapping;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.analysis.mapping.Mapping;
import org.pinae.pumbaa.analysis.mapping.SplitMapping;

public class SplitMappingTest {
	@Test
	public void testMap() {
		String[] columns = new String[]{"MASTER_ACC", "USER_NAME", "SRC_IP", "DST_IP", "SLAVE_ACC"};
		
		Mapping mapping = new SplitMapping(columns, "\\|\\|\\|");
		Map<String, String> result = mapping.map("dwlishunan ||| 李树南 |||  10.243.224.242 |||  10.243.178.102 |||  lisn ");
		
		assertEquals(result.get("MASTER_ACC"), "dwlishunan");
		assertEquals(result.get("USER_NAME"), "李树南");
		assertEquals(result.get("SRC_IP"), "10.243.224.242");
		assertEquals(result.get("DST_IP"), "10.243.178.102");
		assertEquals(result.get("SLAVE_ACC"), "lisn");
		
	}
}
