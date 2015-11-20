package org.pinae.pumbaa.analysis.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 对文本消息进行分割后映射
 * 
 * @author Linjianyu
 *
 */
public class SplitMapping implements Mapping {

	private String[] columns;
	private String split = " ";
	
	public SplitMapping(String[] columns, String split) {
		this.columns = columns;
		this.split = split;
	}
	
	@Override
	public Map<String, String> map(Object data) {
		Map<String, String> result = null;
		
		if (columns != null && split != null && data != null) {
			String items[] = data.toString().split(split);
			if (items != null && items.length == columns.length) {
				result = new HashMap<String, String>();
				for (int i = 0; i < columns.length; i++) {
					result.put(columns[i], items[i].trim());
				}
			}
			
		}
		return result;
	}

}
