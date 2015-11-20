package test.org.pinae.pumbaa.analysis.join;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.analysis.join.Join;
import org.pinae.pumbaa.analysis.join.MapJoin;

public class MapJoinTest {
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testJoin(){
		Map<String, Object> src = new HashMap<String, Object>();
		src.put("KEY1", "Hello");
		src.put("KEY2", "World");
		
		Map<String, Object> dst1 = new HashMap<String, Object>();
		dst1.put("KEY1", "Test");
		dst1.put("KEY2", "PRT");
		dst1.put("KEY3", "YOU");
		
		Map<String, Object> dst2 = new HashMap<String, Object>();
		dst2.put("KEY1", "Test");
		dst2.put("KEY2", "PYT");
		dst2.put("KEY4", "ME");
		
		Join join = new MapJoin();
		src = (Map<String, Object>)join.join(src, dst1);
		src = (Map<String, Object>)join.join(src, dst2);
		
		assertEquals(src.size(), 4);
		assertTrue(src.get("KEY2") instanceof List);
		assertEquals(((List)src.get("KEY2")).size(), 3);
	}
	

}
