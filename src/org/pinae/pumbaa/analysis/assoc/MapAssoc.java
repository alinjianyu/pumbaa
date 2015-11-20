package org.pinae.pumbaa.analysis.assoc;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pinae.pumbaa.analysis.join.MapJoin;


public class MapAssoc implements Assoc{

	@SuppressWarnings("unchecked")
	public Object assoc(Object src, Object dst, String srcKey, String dstKey, String expression){
		
		if (src instanceof Map<?, ?> && dst instanceof Map<?, ?>) {
			Map<String, Object> srcMap = (Map<String, Object>)src;
			Map<String, Object> dstMap = (Map<String, Object>)dst;
			
			if (srcMap.containsKey(srcKey) && dstMap.containsKey(dstKey)){
				Object srcValue = srcMap.get(srcKey);
				Object dstValue = dstMap.get(dstKey);
				
				boolean match = false;
				
				if (srcValue != null && dstValue !=null){
					if (expression.startsWith("regex") && expression.contains(":")) {
						String regex = StringUtils.substringAfter(expression, ":");
						if (srcValue.toString().matches(regex) && dstValue.toString().matches(regex)) {
							match = true;
						}
					} else {
						if (srcValue.equals(dstValue)){
							match = true;
						}
					}
					
					if (match) {
						return (Map<String, Object>)new MapJoin().join(src, dst);
					}
				}
			}
		}
		
		return null;
	}

}
