package test.org.pinae.pumbaa.analysis.comparer;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.analysis.comparer.Comparer;
import org.pinae.pumbaa.analysis.comparer.MapComparer;

public class MapComparerTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCompare(){
		Map<String, Object> src = new HashMap<String, Object>();
		src.put("KEY1", 10);
		src.put("KEY2", 80);
		src.put("KEY3", true);
		src.put("KEY4", true);
		src.put("KEY5", "Me");
		
		Map<String, Object> dst = new HashMap<String, Object>();
		dst.put("KEY1", 30);
		dst.put("KEY2", 60);
		dst.put("KEY3", false);
		dst.put("KEY4", true);
		dst.put("KEY5", "Me");
		
		Comparer comparer = new MapComparer();
		Map<String, Object> result = (Map<String, Object>)comparer.compare(src, dst);
		assertEquals(result.get("KEY1"), -20);
		assertEquals(result.get("KEY2"), 20);
		assertEquals(result.get("KEY3"), false);
		assertEquals(result.get("KEY4"), true);
		assertEquals(result.get("KEY5"), true);
	}
}
