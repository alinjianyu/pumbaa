package test.org.pinae.pumbaa.analysis.join;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.pinae.pumbaa.analysis.join.ObjectJoin;

public class ObjectJoinTest {
	
	@Test
	public void testSum(){
		List<Object> valueList = new ArrayList<Object>();
		
		valueList.add(1);
		valueList.add((Integer)4);
		valueList.add("7");
		
		ObjectJoin join = new ObjectJoin();
		Long sumValue = join.sum(valueList);
		
		assertTrue(sumValue == 12);
	}
	
	@Test
	public void testUniq(){
		List<Object> valueList = new ArrayList<Object>();
		
		valueList.add("You");
		valueList.add("Me");
		valueList.add("You");
		
		assertEquals(valueList.size(), 3);
		
		ObjectJoin join = new ObjectJoin();
		Set<Object> valueSet = join.uniq(valueList);
		
		assertEquals(valueSet.size(), 2);
	}
}
