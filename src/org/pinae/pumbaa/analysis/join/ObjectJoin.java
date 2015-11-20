package org.pinae.pumbaa.analysis.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对象合并
 * 
 * @author Linjianyu
 *
 */
public class ObjectJoin extends Join{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object join(Object src, Object dst) {
		if (src == null) {
			return null;
		}
		if (dst != null) {
			if (src instanceof Map && dst instanceof Map) {
				return new MapJoin().join(src, dst);
			} else if (src instanceof List) {
				if (dst instanceof List) {
					((List)src).addAll((List)dst);
				} else {
					((List)src).add(dst);
				}
			} else if (src.getClass().isArray() && dst.getClass().isArray()) {
				return new ArrayJoin().join(src, dst);
			} else {
				List<Object> result = new ArrayList<Object>();
				result.add(src);
				result.add(dst);
				
				return result;
			}
		}
		return src;
	}
	

}
