package org.pinae.pumbaa.analysis.group;

import org.apache.commons.lang3.StringUtils;

/**
 * 累加聚合
 * 
 * @author Linjianyu
 *
 */
public class GroupSum implements Group {

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
	public Object getValue(Object value, String[] data, int[] columns) {
		Double sum = new Double(0);
		
		int sumColumn = columns[columns.length - 1]; //group参数的最后一个字段为数据累加字段
		
		if (data.length > sumColumn) {
			if (value != null && value instanceof Long) {
				sum = (Double)value;
				
				String addValue = data[sumColumn];
				if (addValue != null && StringUtils.isNumeric(addValue)){
					sum += Double.parseDouble(addValue);
				}
				
			}
		}
		return sum;
	}

}
