package org.pinae.pumbaa.sql;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.pinae.pumbaa.sql.io.SQLFileLoader;
import org.pinae.pumbaa.sql.io.resource.SQLMap;
import org.pinae.pumbaa.sql.io.resource.SQLMap.Choose;
import org.pinae.pumbaa.util.ClassLoaderUtils;

/**
 * 
 * 获取SQL语句
 * 
 * @author huiyugeng
 * 
 */
public class SQLReader {

	private static Map<String, SQLMap> SQL_MAP = new HashMap<String, SQLMap>();

	static {
		String path = ClassLoaderUtils.getResourcePath("");
		SQLFileLoader sqlReader = new SQLFileLoader(path, "sql.xml");

		SQL_MAP = sqlReader.getSQLMap();

	}

	/**
	 * 根据SQL描述名称获取SQL
	 * 
	 * @param name SQL描述名称
	 * 
	 * @return SQL语句
	 */
	public static String getSQLByName(String name) {
		return getSQLByName(name, null);
	}

	/**
	 * 根据SQL描述名称和参数获取SQL
	 * 
	 * @param name SQL描述名称
	 * @param parameters 参数列表
	 * 
	 * @return SQL语句
	 */
	private static String getSQLByName(String name, Map<String, Object> parameters) {
		StringBuffer sqlBuffer = new StringBuffer();

		SQLMap sql = SQL_MAP.get(name);

		if (sql != null) {
			sqlBuffer = new StringBuffer(sql.getValue());

			List<Choose> chooseList = sql.getChooseList();
			if (chooseList != null && parameters != null) {
				for (Choose choose : chooseList) {
					String when = choose.getWhen();
					if (parameters.containsKey(when)) {
						String chooseValue = choose.getValue();
						sqlBuffer.append(" " + chooseValue);
					}
				}
			}
		}

		return sqlBuffer.toString();
	}

	/**
	 * 根据SQL描述名称和参数构建SQL语句
	 * 
	 * @param name SQL描述名称
	 * @param parameters 参数列表
	 * 
	 * @return 构建后的SQL语句
	 */
	@SuppressWarnings("unchecked")
	public static String getSQLByNameWithParameters(String name, Object parameters) {
		if (name == null || parameters == null) {
			return null;
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		if (parameters.getClass().isArray()) {
			Object[] paramValues = (Object[]) parameters;
			for (int i = 0; i < paramValues.length; i++) {
				if (paramValues[i] == null) {
					paramValues[i] = "";
				}
				parameterMap.put(Integer.toString(i), paramValues[i]);
			}
		} else if (parameters instanceof Map) {
			parameterMap = (Map<String, Object>) parameters;
		} else {
			Class<? extends Object> paramClass = parameters.getClass();
			Field[] fields = paramClass.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);

				String fieldName = field.getName();
				Object fieldValue = null;
				try {
					fieldValue = field.get(parameters);
				} catch (Exception e) {
					fieldValue = null;
				}
				if (fieldName != null && fieldValue != null) {
					parameterMap.put(fieldName, fieldValue);
				}
			}
		}
		return getSQLByNameWithParameters(name, parameterMap);
	}
	
	/**
	 * 根据SQL描述名称和参数构建SQL语句
	 * 
	 * @param name SQL描述名称
	 * @param parameters 参数列表
	 * 
	 * @return 构建后的SQL语句
	 */
	@SuppressWarnings("rawtypes")
	public static String getSQLByNameWithParameters(String name, Map<String, Object> parameters) {
		String sql = getSQLByName(name, parameters);
		try {
			Set<String> keySet = parameters.keySet();
			for (String key : keySet) {
				Object value = parameters.get(key);

				if (value instanceof String) {
					value = String.format("'%s'", value);
				} else if (value instanceof List) {
					StringBuffer valueBuffer = new StringBuffer();

					List list = (List) value;
					for (Object item : list) {
						if (item instanceof String) {
							valueBuffer.append(String.format("'%s'", item) + ",");
						} else {
							valueBuffer.append(item.toString() + ",");
						}
					}
					value = StringUtils.removeEnd(valueBuffer.toString(), ",");
				} else if (value == null) {
					continue;
				} else if (value instanceof Date) {
					value = String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
				} else {
					value = value.toString();
				}

				key = ":" + key;
				sql = StringUtils.replace(sql, key, (String) value);
			}

			return sql;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 限制查询条数的SQL
	 * 
	 * @param sql SQL语句
	 * @param offset SQL查询偏移记录数
	 * @param length SQL目标数量
	 * @param dbType 数据库类型 Oralce, Mysql
	 * 
	 * @return 限制查询条数的SQL
	 */
	public static String getLimitSQL(String sql, long offset, long length, String dbType) {
		sql = sql.trim();
		if (StringUtils.startsWithIgnoreCase(sql, "select")) {
			boolean isForUpdate = false;
			if (sql.toLowerCase().endsWith(" for update")) {
				sql = sql.substring(0, sql.length() - 11);
				isForUpdate = true;
			}
	
			StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
			if (dbType.equalsIgnoreCase("oracle")) {
				if (offset > 0) {
					pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
				} else {
					pagingSelect.append("select * from ( ");
				}
				pagingSelect.append(sql);
				if (offset > 0) {
					pagingSelect.append(String.format(" ) row_ where rownum <= %d) where rownum_ > %d", length + offset, offset));
				} else {
					pagingSelect.append(String.format(" ) where rownum <= ?", length));
				}
			} else if (dbType.equalsIgnoreCase("mysql")) {
				pagingSelect.append("select * from ( ");
				pagingSelect.append(sql);
				pagingSelect.append(String.format(" ) limit %d, %d", offset, length));
			}
	
			if (isForUpdate) {
				pagingSelect.append(" for update");
			}
	
			return pagingSelect.toString();
		}
		return sql;
	}
	
	/**
	 * 获得查询SQL中可返回的结果数量的SQL语句
	 * 
	 * @param sql 原SQL语句
	 * @return SQL可返回的结果数量SQL语句
	 */
	public static String getCountSQL(String sql) {
		sql = sql.trim();
		if (StringUtils.startsWithIgnoreCase(sql, "select")) {
			StringBuffer pagingSelect = new StringBuffer(sql.length() + 50);
			pagingSelect.append("select count(*) from ( ");
			pagingSelect.append(sql);
			pagingSelect.append(" )");
		}
		return sql;
	}


}
