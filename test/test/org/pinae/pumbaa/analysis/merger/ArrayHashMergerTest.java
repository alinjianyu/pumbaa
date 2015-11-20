package test.org.pinae.pumbaa.analysis.merger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.pinae.pumbaa.analysis.merger.ArrayHashMerger;
import org.pinae.pumbaa.analysis.merger.Merger;

public class ArrayHashMergerTest {
	
	@Test
	public void testMerge() {
		List<Object> data = new ArrayList<Object>();
		data.add(new String[]{"20150108235856","123456","JMAIT00088","10.246.126.117","null","/ngcustcare/custLogin.action","null"});
		data.add(new String[]{"20150108235857","123456","JMAIT00088","10.246.126.117","null","/ngcustcare/custLogin.action","null"});
		data.add(new String[]{"20150108235858","123456","JMAIT00088","10.246.126.117","null","/ngcustcare/custLogin.action","null"});
		data.add(new String[]{"20150108235858","123456","JMAIT00088","10.246.126.117","null","/ngcustcare/custLogin.action","null"});
		
		Merger merger = new ArrayHashMerger(new int[]{0, 4, 6});
		List<Object> mergerList = merger.merge(data);
		
		assertEquals(mergerList.size(), 1);
	}
}
