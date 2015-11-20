package org.pinae.pumbaa.analysis.merger;

import java.util.ArrayList;
import java.util.List;

import org.pinae.pumbaa.util.MessageDigestUtils;

/**
 * 字符串Hash归并
 * 
 * 对每条数据建立Hash值（MD5），相同Hash值得进行合并
 * 
 * @author Huiyugeng
 *
 */
public class HashMerger implements Merger{

	@Override
	public List<Object> merge(List<Object> data) {
		List<String[]> mergerList = new ArrayList<String[]>();
		
		for (Object item : data) {
			
			if (item == null) {
				continue;
			}
			
			String hashKey = MessageDigestUtils.MD5(item.toString());
			
			boolean match = false;
			for (String[] mergerItem : mergerList) {
				if (hashKey.equals(mergerItem[0])) {
					match = true;
					break;
				}
			}
			
			if (match == false) {
				mergerList.add(new String[]{hashKey, item.toString()});
			}
		}
		
		List<Object> valueList = new ArrayList<Object>();
		for (String[] mergerItem : mergerList) {
			valueList.add(mergerItem[1]);
		}
		return valueList;
	}

}
