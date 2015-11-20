package org.pinae.pumbaa.analysis.group;

/**
 * 数据聚合
 * 
 * @author Huiyugeng
 *
 */
public interface Group {
	
	public static final String ALL_COLUMNS = "columns";
	
	public static final String GROUP_COLUMNS = "group";
	
	public static final String DATA_CONVERT = "convert"; 
	
	public static final String GROUP_COUNT = "group-count";
	
	public static final String GROUP_JOIN = "group-join";
	
	public static final String GROUP_SUM = "group-sum";
	
	public static final String GROUP_FILTE = "group-filte";
	
	/**
	 * 关键字处理
	 * 
	 * @param data 需要进行聚合的数据
	 * @param columns 需要进行聚合的项
	 * 
	 * @return 处理后的关键字
	 */
	public String getKey(String data[], int columns[]);
	
	/**
	 * 值处理
	 * 
	 * @param value 处理前的值
	 * @param data 需要进行聚合的数据
	 * @param columns 需要进行聚合的项
	 * 
	 * @return 处理后的值
	 */
	public Object getValue(Object value, String data[], int columns[]);
}
