package org.pinae.pumbaa.analysis.group;

import org.apache.commons.lang3.StringUtils;

/**
 * 统计聚合
 * 
 * @author Linjianyu
 *
 */
public class GroupCount implements Group {

	@Override
	public String getKey(String[] data, int[] columns) {
		String key = "";
		
		if (columns != null && columns.length > 0){
			for (int column : columns) {
				if (data.length > column){
					key += data[column] + "|";
				}
			}
			
			if (key.endsWith("|")){
				key = StringUtils.substring(key, 0, key.length() -1);
			}
			
			return key;
		}
		
		return null;
	}

	@Override
	public Object getValue(Object value, String[] data, int[] columns) {
		if (value != null){
			value = (Long)value + 1;
		} else {
			value = new Long(1);
		}
		return value;
	}

}
