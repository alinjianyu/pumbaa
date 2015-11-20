package org.pinae.pumbaa.analysis.replace;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 字典替换
 * 
 * @author Huiyugeng
 *
 */
public class DictReplace implements Replace{
	
	private Map<String, String> dict;
	
	public DictReplace(Map<String, String> dict) {
		this.dict = dict;
	}
	
	@Override
	public String replace(String data) {
		if (data != null && dict != null) {
			
			Set<String> keySet = dict.keySet();
			for (String key : keySet) {
				String replaceData = dict.get(key);
				data = StringUtils.replace(data, key, replaceData);
			}
		}
		return data;
	}

}
