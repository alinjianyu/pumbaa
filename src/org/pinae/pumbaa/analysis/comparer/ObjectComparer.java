package org.pinae.pumbaa.analysis.comparer;

import java.util.Map;

public class ObjectComparer implements Comparer {

	@Override
	public Object compare(Object src, Object dst){
		if (src == null || dst == null) {
			return null;
		}
		if (src instanceof Short && dst instanceof Short) {
			return ((Short)src) - ((Short)dst);
		} else if (src instanceof Integer && dst instanceof Integer){
			return ((Integer)src) - ((Integer)dst);
		} else if (src instanceof Long && dst instanceof Long){
			return ((Long)src) - ((Long)dst);
		} else if (src instanceof Map && dst instanceof Map) {
			return new MapComparer().compare(src, dst);
		} else {
			return src.equals(dst);
		}
	}

}
