package org.pinae.pumbaa.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * SQL执行结果映射
 * 
 * @author huiyugeng
 *
 */
public class SQLMapper {
	
	private static Logger log = Logger.getLogger(SQLMapper.class);
	
	/**
	 * 将Select查询结果映射成为List<Map>
	 * 
	 * @param table 查询结果
	 * @param columns 列名（按查询结果顺序）
	 * 
	 * @return 映射后的List<Map>
	 */
	public List<Map<String, Object>> toMapList(List<Object[]> table, String[] columns) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if ((table != null && columns != null) && table.size() > 0) {
			for (Object[] row : table) {
				Map<String, Object> result = new HashMap<String, Object>();
				for (int i = 0; i < columns.length; i++) {
					result.put(columns[i], row[i]);
				}
				resultList.add(result);
			}
		}
		return resultList;
	}
	
	/**
	 * 将Select查询结果映射成为List<Map>，并支持当字段为空值时进行默认值设定
	 * 
	 * @param table 查询结果
	 * @param columns 列名（按查询结果顺序）
	 * @param defaultMap 默认值
	 * 
	 * @return 映射后的List<Map>
	 */
	public List<Map<String, Object>> toMapList(List<Object[]> table, String[] columns, Map<String, Object> defaultMap) {
		List<Map<String, Object>> resultList = toMapList(table, columns);
		
		for (Map<String, Object> resultItem : resultList) {
			Set<String> keySet = defaultMap.keySet();
			for (String key : keySet) {
				if (resultItem.containsKey(key)) {
					Object value = resultItem.get(key);
					if (value == null || StringUtils.isEmpty(value.toString())) {
						Object defaultValue = defaultMap.get(key);
						if (defaultValue != null) {
							resultItem.put(key, defaultValue);
						}
					}
				}
			}
		}
		
		return resultList;
	}

	/**
	 * 将Select查询结果映射成为List<对象>
	 * 
	 * @param clazz 映射的类名称
	 * @param table 查询结果
	 * @param columns 列名（按查询结果顺序）
	 * 
	 * @return 映射后的List<对象>
	 */
	@SuppressWarnings("rawtypes")
	public List<?> toObjectList(Class clazz, List<Object[]> table, String[] columns) {

		List<Object> resultList = new ArrayList<Object>();
		if ((table != null && columns != null) && table.size() > 0) {

			for (Object[] row : table) {
				Object object = null;
				try {
					object = clazz.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int i = 0; i < columns.length; i++) {

					String columnName = (String) columns[i];
					try {
						Field field = clazz.getDeclaredField(columnName);
						field.setAccessible(true);
						field.set(object, row[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				resultList.add(object);
			}

		}
		return resultList;
	}
}
