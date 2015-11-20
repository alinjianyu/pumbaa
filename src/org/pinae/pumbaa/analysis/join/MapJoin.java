package org.pinae.pumbaa.analysis.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map合并
 * 
 * @author Linjianyu
 *
 */
public class MapJoin extends Join {

	@SuppressWarnings("unchecked")
	@Override
	public Object join(Object src, Object dst) {
		
		if (src instanceof Map && dst instanceof Map) {
			
			Map<String, Object> srcMap = (Map<String, Object>)src;
			Map<String, Object> dstMap = (Map<String, Object>)dst;
			
			Set<Entry<String, Object>> dstEntrySet = dstMap.entrySet();
			
			for (Entry<String, Object> dstEntry : dstEntrySet) {
				String dstKey = dstEntry.getKey();
				Object dstValue = dstEntry.getValue();
				
				Object srcValue = srcMap.get(dstKey);
				if (dstValue != null){
					if (srcValue != null){
						List<Object> valueList = null;
						
						if (srcValue instanceof List){
							valueList = (List<Object>)srcValue;
							valueList.add(dstValue);
						} else {
							valueList = new ArrayList<Object>();
							valueList.add(srcValue);
							valueList.add(dstValue);
						}
						srcMap.put(dstKey, valueList);
					} else {
						srcMap.put(dstKey, dstValue);
					}
				}
			}
		}
		
		return src;
	}
	


}
