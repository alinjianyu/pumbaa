package org.pinae.pumbaa.analysis.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ListSplitMapping implements Mapping {
	
	private int keyColumn;
	private int valueColumn;
	private String split = " ";
	
	public ListSplitMapping(int keyColumn, int valueColumn, String split) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
		this.split = split;
	}

	@Override
	public Map<String, String> map(Object data) {
		Map<String, String> result = null;
		
		if (split != null && data != null && data instanceof List) {
			
			result = new HashMap<String, String>();
			
			List<Object> list = (List<Object>)data;
			for (Object listItem : list) {
				String arrayItems[] = null;
				
				if (listItem instanceof String) {
					arrayItems = listItem.toString().split(split);
				} else if (listItem instanceof String[]) {
					arrayItems = (String[])listItem;
				}
				
				if (keyColumn < arrayItems.length && valueColumn < arrayItems.length) {
					String key = arrayItems[keyColumn].trim();
					String value = arrayItems[valueColumn].trim();
					
					if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
						String mapVal = result.get(key);
						if (mapVal == null) {
							result.put(key, value);
						} else {
							result.put(key, mapVal + this.split + value);
						}
					}
				}
			}
		}
		return result;
	}

}
