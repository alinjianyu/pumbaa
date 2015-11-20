package test.org.pinae.pumbaa.analysis.merger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.pinae.pumbaa.analysis.merger.HashMerger;
import org.pinae.pumbaa.analysis.merger.Merger;

public class HashMergerTest {
	
	@Test
	public void testMerge() {
		List<Object> data = new ArrayList<Object>();
		data.add("20150108235856|123456|JMAIT00088|10.246.126.117|null|/ngcustcare/custLogin.action|null");
		data.add("20150108235857|123456|JMAIT00088|10.246.126.117|null|/ngcustcare/custLogin.action|null");
		data.add("20150108235858|123456|JMAIT00088|10.246.126.117|null|/ngcustcare/custLogin.action|null");
		data.add("20150108235858|123456|JMAIT00088|10.246.126.117|null|/ngcustcare/custLogin.action|null");
		
		Merger merger = new HashMerger();
		List<Object> mergerList = merger.merge(data);
		
		assertEquals(mergerList.size(), 3);
	}
}
