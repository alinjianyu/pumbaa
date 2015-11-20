package org.pinae.pumbaa.analysis.group;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 值拼接聚合
 * 
 * @author Linjianyu
 *
 */
public class GroupJoin implements Group {

	@Override
	public String getKey(String[] data, int[] columns) {
		String key = "";
		
		if (columns != null && columns.length > 0){
			for (int i = 0; i < columns.length - 1; i++) {
				if (data.length > columns[i]){
					key += data[columns[i]] + "|";
				}
			}
		}
		
		if (key.endsWith("|")){
			key = StringUtils.substring(key, 0, key.length() -1);
		}
		
		return key;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getValue(Object value, String[] data, int[] columns) {
		Set<String> joinSet = null;
		
		int groupColumn = columns[columns.length - 1]; //group参数的最后一个字段为数据聚合字段
		
		if (data.length > groupColumn) {
			if (value != null && value instanceof Set) {
				joinSet = (Set)value;
			} else {
				joinSet = new HashSet<String>();
			}
			
			String joinValue = data[groupColumn];
			if (StringUtils.isNotEmpty(joinValue)){
				joinSet.add(joinValue.trim());
			}
		}
		
		return joinSet;
	}

}
