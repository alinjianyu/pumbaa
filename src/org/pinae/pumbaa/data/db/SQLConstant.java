package org.pinae.pumbaa.data.db;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.pinae.pumbaa.data.db.SQLList.SQL;
import org.pinae.pumbaa.data.db.SQLList.SQL.Choose;
import org.pinae.pumbaa.util.ClassLoaderUtils;

/**
 * 
 * SQL集合容器
 * 
 * @author Linjianyu
 * 
 */
public class SQLConstant {

	private static Map<String, SQL> SQL_MAP = new HashMap<String, SQL>();

	static {
		String path = ClassLoaderUtils.getResourcePath("");
		SQLReader sqlReader = new SQLReader(path, "sql.xml");

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
	 * 
	 * @param parameters 参数列表
	 * 
	 * @return SQL语句
	 */
	public static String getSQLByName(String name, Map<String, Object> parameters) {
		SQL sql = SQL_MAP.get(name);
		if (sql != null) {
			return replaceStatement(sql, parameters);
		} else {
			return null;
		}
	}

	/**
	 * 根据SQL描述名称和参数构建SQL语句
	 * 
	 * @param name SQL描述名称
	 * @param parameters 参数对象
	 * 
	 * @return 构建后的SQL语句
	 */

	public static String getSQLByNameWithParameters(String name, Object parameters) {
		if (name == null) {
			return null;
		}
		
		Map<String, Object> parameterMap = buildParameters(parameters);
		
		SQL sql = SQL_MAP.get(name);
		if (sql != null) {
			return replaceSubSQL(sql, parameterMap);
		} else {
			return null;
		}
	}
	
	/*
	 * 构建SQL语句参数表 
	 * 
	 * @param parameters 参数对象
	 * 
	 * @return 参数表<参数名称, 参数值>
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> buildParameters(Object parameters) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		
		if (parameters != null) {
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
		}
		
		return parameterMap;
	}
	
	/*
	 * 替换SQL语句中引用的子句
	 * 
	 * @param sql 需要构建的SQL对象
	 * @param parameters 参数表
	 * 
	 * @return 构建后的SQL语句
	 */
	private static String replaceSubSQL(SQL sql, Map<String, Object> parameters) {
		String sqlContent = sql.getValue();
		
		if (sqlContent != null) {
			String regexs = "[$][{](\\w*)[}]"; //子句替换模式
			Pattern regex = Pattern.compile(regexs);
			Matcher regexMatcher = regex.matcher(sqlContent);
			while (regexMatcher.find()) {
				String subSQLName = regexMatcher.group(1);
				
				SQL subSQL = SQL_MAP.get(subSQLName);
				if (subSQL != null) {
					String subSQLContent = replaceSubSQL(subSQL, parameters);
					
					sqlContent = sqlContent.replace(regexMatcher.group(0), subSQLContent);
					sql.setValue(sqlContent);
				}
			}
			
			sqlContent = replaceStatement(sql, parameters);
			sqlContent = replaceVariables(sqlContent, parameters);
		}
		
		return sqlContent;
	}

	/*
	 * 替换SQL语句中的子句
	 * 
	 * @param sql 需要构建的SQL对象
	 * @param parameters 参数表
	 * 
	 * @return 构建后的SQL语句
	 */
	private static String replaceStatement(SQL sql, Map<String, Object> parameters) {
		String sqlContent = null;
		if (sql != null) {
			sqlContent = sql.getValue();
			if (StringUtils.isNotEmpty(sqlContent)) {

				try {
					// 执行子句构建 子句格式为{statement}
					List<Choose> chooseList = sql.getChooseList();

					if (chooseList != null && parameters != null) {
						for (Choose choose : chooseList) {
							String condition = choose.getWhen();
							if (parameters.containsKey(condition)) {
								String statement = choose.getStatement();

								// 如果不存在statement参数，则使用when参数代替
								if (StringUtils.isEmpty(statement)) {
									statement = condition;
								}
								statement = "{" + statement + "}";

								if (StringUtils.contains(sqlContent, statement)) {
									sqlContent = sqlContent.replace(statement, choose.getValue());
								} else {
									sqlContent = sqlContent + " " + choose.getValue();
								}
							}
						}
					}
					
					//清理没有被替换的子句
					String regexs = "[{](\\w*)[}]"; //子句替换模式
					Pattern regex = Pattern.compile(regexs);
					Matcher regexMatcher = regex.matcher(sqlContent);
					while (regexMatcher.find()) {
						sqlContent = sqlContent.replace(regexMatcher.group(0), "");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sqlContent;
	}

	/*
	 * 替换SQL语句的变量
	 * 
	 * @param sql SQL语句
	 * @param parameters 参数表
	 * 
	 * @return 构建后的SQL语句
	 */
	private static String replaceVariables(String sql, Map<String, Object> parameters) {
		if (StringUtils.isNotEmpty(sql)) {
			try {
				if (parameters != null) {
					// 执行变量替换, 变量格式为:var
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
							value = String.format("'%s'",
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
						} else {
							value = value.toString();
						}

						key = ":" + key;
						sql = StringUtils.replace(sql, key, (String) value);
					}
				}

				// 多个空格替换为单个空格
				if (sql != null) {
					sql = sql.replaceAll(" +", " ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sql;
	}

	/**
	 * 限制查询条数的SQL
	 * 
	 * @param sql SQL语句
	 * @param offset SQL查询偏移记录数
	 * @param length SQL目标数量
	 * @param dbType 数据库类型 Oralce,Mysql
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
					pagingSelect.append(String.format(" ) row_ where rownum <= %d) where rownum_ > %d",
							length + offset, offset));
				} else {
					pagingSelect.append(String.format(" ) where rownum <= %d", length));
				}
			} else if (dbType.equalsIgnoreCase("mysql")) {
				pagingSelect.append("select * from ( ");
				pagingSelect.append(sql);
				pagingSelect.append(String.format(" ) t limit %d, %d", offset, length));
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
			StringBuffer pagingSelect = new StringBuffer(sql.length() + 30);
			pagingSelect.append("select count(*) from ( ");
			pagingSelect.append(sql);
			pagingSelect.append(" ) t");
			return pagingSelect.toString();
		}
		return null;
	}

}
