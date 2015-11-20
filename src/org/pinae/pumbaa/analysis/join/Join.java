package org.pinae.pumbaa.analysis.join;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


public abstract class Join {
	public abstract Object join(Object src, Object dst);
	
	/**
	 * 累计计数
	 * 
	 * @param valueList 数据列表
	 * 
	 * @return 累加值
	 */
	public Long sum(List<Object> valueList){
		long sumValue = 0;
		
		for (Object value : valueList) {
			
			if (value == null) {
				continue;
			}
			
			if (value instanceof Long) {
				sumValue += (Long)value;
			} else if (value instanceof Integer) {
				sumValue += (Integer)value;
			} else if (value instanceof Short) {
				sumValue += (Short)value;
			} else if (value instanceof BigDecimal) {
				sumValue += ((BigDecimal)value).longValue();
			} else if (value instanceof String) {
				if (StringUtils.isNumeric(value.toString())){
					sumValue += Long.parseLong(value.toString());
				}
			}
		}
		
		return sumValue;
	}
	
	/**
	 * 去重计算
	 * 
	 * @param valueList 需要进行去重的列表
	 * 
	 * @return 去重后的列表
	 */
	public Set<Object> uniq(List<Object> valueList) {
		if (valueList == null) {
			return null;
		}
		
		Set<Object> uniqSet = new HashSet<Object>();
		
		for (Object value : valueList) {
			uniqSet.add(value);
		}
		return uniqSet;
		
	}
	
	/**
	 * 去重计算
	 * 
	 * @param valueArray 需要进行去重的数组
	 * 
	 * @return 去重后的数组
	 */
	public Object[] uniq(Object[] valueArray) {
		if (valueArray == null) {
			return null;
		}
		
		Set<Object> uniqSet = new HashSet<Object>();
		
		for (Object value : valueArray) {
			uniqSet.add(value);
		}
		
		return uniqSet.toArray();
	}
	
	/**
	 * 去重计算
	 * 
	 * @param value 需要去重的字符串
	 * @param split 字符串分隔符
	 * 
	 * @return 去重后的数组
	 */
	public Object[] uniq(String value, String split) {
		if (value == null || split == null){
			return null;
		}
		
		String[] array = value.split(split);
		
		return uniq(array);
	}
}
