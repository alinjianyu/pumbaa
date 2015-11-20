package org.pinae.pumbaa.analysis.comparer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapComparer implements Comparer {

	@SuppressWarnings("unchecked")
	@Override
	public Object compare(Object src, Object dst) {
		
		if (src instanceof Map<?, ?> && dst instanceof Map<?, ?>) {
		
			Map<String, Object> srcMap = (Map<String, Object>)src;
			Map<String, Object> dstMap = (Map<String, Object>)dst;
			
			ObjectComparer objComparer = new ObjectComparer();
			
			Map<String, Object> compareResult = new HashMap<String, Object>();
			
			Set<Entry<String, Object>> srcEntrySet = srcMap.entrySet();
			for (Entry<String, Object> srcEntry : srcEntrySet) {
				String srcKey = srcEntry.getKey();
				Object srcValue = srcEntry.getValue();
				
				Object dstValue = dstMap.get(srcKey);
				
				Object result = objComparer.compare(srcValue, dstValue);
				if (result != null){
					compareResult.put(srcKey, result);
				}
			}
			
			
			return compareResult;
		}
		
		return null;
	}
	

}
