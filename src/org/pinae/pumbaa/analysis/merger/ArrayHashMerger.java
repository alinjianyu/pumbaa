package org.pinae.pumbaa.analysis.merger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pinae.pumbaa.util.MessageDigestUtils;

/**
 * 数组Hash值归并
 * 
 * 如果每条数据为数组，那么可以忽略某些字段，然后将其余的字段进行合并然后Hash运算后，相同Hash值的进行合并
 * 
 * @author Huiyugeng
 *
 */
public class ArrayHashMerger implements Merger {
	private int igronColumns[];
	
	public ArrayHashMerger(int igronColumns[]) {
		this.igronColumns = igronColumns;
	}

	@Override
	public List<Object> merge(List<Object> data) {
		List<Object[]> mergerList = new ArrayList<Object[]>();
		
		for (Object item : data) {
			
			if (item != null && item instanceof String[]) {
				String[] _data = (String[])item;
				for (int i = 0 ; i < igronColumns.length ; i++) {
					_data[i] = null;
				}
				String hashKey = MessageDigestUtils.MD5(StringUtils.join(_data, "|"));
				
				boolean match = false;
				for (Object[] mergerItem : mergerList) {
					if (hashKey.equals(mergerItem[0])) {
						match = true;
						break;
					}
				}
				
				if (match == false) {
					mergerList.add(new String[]{hashKey, item.toString()});
				}
			}

		}
		
		List<Object> valueList = new ArrayList<Object>();
		for (Object[] mergerItem : mergerList) {
			valueList.add(mergerItem[1]);
		}
		return valueList;
	}

}
