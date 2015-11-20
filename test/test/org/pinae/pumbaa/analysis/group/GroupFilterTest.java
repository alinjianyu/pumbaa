package test.org.pinae.pumbaa.analysis.group;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.pinae.pumbaa.analysis.group.Group;
import org.pinae.pumbaa.analysis.group.GroupFilter;

public class GroupFilterTest {
	
	@Test
	public void testGroup() {
		
		String dataSet[][] = {
				{"20150108235832", "CS_100010021016", "JMAIT00043", "10.246.80.213"},
				{"20150108235837", "CS_100010021016", "JMAIT00043", "10.246.80.213"},
				{"20150108235856", "123456", "JMAIT00088", "10.246.126.117"},
				{"20150108235859", "NULL", "JMAIT00088", "10.246.126.117"},
				};
		
		int groupColumns[] = {2};
		
		Group group = new GroupFilter();
		
		Map<String, Object> statMap = new HashMap<String, Object>();
		for (String[] data : dataSet) {
			String key = group.getKey(data, groupColumns);
			Object value = statMap.get(key);
			value = group.getValue(value, data, groupColumns);
			
			if (StringUtils.isNotEmpty(key) && value != null) {
    			statMap.put(key, value);
    		}
		}
		
		assertEquals(statMap.size(), 2);
	}
}
