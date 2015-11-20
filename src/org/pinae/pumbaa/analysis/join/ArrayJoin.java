package org.pinae.pumbaa.analysis.join;

public class ArrayJoin extends Join {

	@Override
	public Object join(Object src, Object dst) {
		if (src != null && dst != null) {
			if (src.getClass().isArray() && dst.getClass().isArray()) {
				Object[] srcArray = ((Object[])src);
				Object[] dstArray = ((Object[])dst);
				
				Object[] result = new Object[srcArray.length + dstArray.length];
				int count = 0;
				for (int i = 0; i < srcArray.length; i++) {
					result[count] = srcArray[i];
					count++;
				}
				for (int i = 0; i < dstArray.length; i++) {
					result[count] = dstArray[i];
					count++;
				}
				
				return result;
			}
		}
		
		return null;
	}

}
