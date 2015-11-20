package org.pinae.pumbaa.analysis.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典过滤
 * 
 * @author Huiyugeng
 *
 */
public class DictFilter implements Filter {
	
	private List<String> dictionary = new ArrayList<String>();
	
	public DictFilter(List<String> dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String filter(String data) {
		
		if (dictionary != null && dictionary.size() > 0) {
			for (String item : dictionary) {
				if (data.contains(item)) {
					return data;
				}
			}
		}
		
		return null;
	}

}
