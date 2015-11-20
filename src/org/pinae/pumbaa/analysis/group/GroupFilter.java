package org.pinae.pumbaa.analysis.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 过滤聚合
 * 
 * @author Huiyugeng
 *
 */
public class GroupFilter implements Group {

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
	@SuppressWarnings("unchecked")
	public Object getValue(Object value, String[] data, int[] columns) {
		List<String[]> filterList = null;
		if (value != null && value instanceof List) {
			filterList = (List<String[]>)value;
		} else {
			filterList = new ArrayList<String[]>();
		}
		
		for (int column : columns) {
			data[column] = null;
		}
		
		String[] _data = new String[data.length - columns.length];
		
		int cnt = 0;
		for (int i = 0 ; i < data.length ; i++) {
			String item = data[i];
			if (item != null) {
				_data[cnt++] = item; 
			}
		}
		filterList.add(_data);
		
		return filterList;
	}

}
