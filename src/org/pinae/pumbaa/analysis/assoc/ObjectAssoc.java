package org.pinae.pumbaa.analysis.assoc;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

public class ObjectAssoc implements Assoc{

	@Override
	public Object assoc(Object src, Object dst, String srcKey, String dstKey, String expression) {
		Class srcClass = src.getClass();
		Class dstClass = dst.getClass();
		try {
			Field srcField = srcClass.getField(srcKey);
			Field dstField = dstClass.getField(dstKey);
			
			Object srcValue = srcField.get(src);
			Object dstValue = dstField.get(dst);
			
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
					//TODO join object
					return null;
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}



}
