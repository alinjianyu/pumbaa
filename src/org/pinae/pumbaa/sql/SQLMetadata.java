package org.pinae.pumbaa.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * SQL元数据工具
 * 
 * @author Huiyugeng
 * 
 */
public class SQLMetadata {

	private static Logger log = Logger.getLogger(SQLMetadata.class);

	private Connection conn = null;

	/**
	 * 构造函数
	 * 
	 * @param driver 驱动名称
	 * @param url JDBC URL地址
	 * @param user 数据库用户
	 * @param password 数据库密码
	 */
	public SQLMetadata(String driver, String url, String user, String password) {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			log.error("Connection Database Error :" + e.getMessage());
		}
	}

	/**
	 * 通过SQL语句获取元数据信息
	 * 
	 * @param sql SQL语句
	 * 
	 * @return 元数据信息
	 */
	public List<Map<String, String>> getMetadataBySQL(String sql) {
		
		List<Map<String, String>> table = new ArrayList<Map<String, String>>();
		
		try {

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();

			int columnCount = rsme.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				
				Map<String, String> field = new HashMap<String, String>();

				field.put("NAME", rsme.getColumnName(i)); // 字段名称
				field.put("TYPE", rsme.getColumnTypeName(i)); //字段类型名称(例如：VACHAR2)
				field.put("SIZE", Integer.toString(rsme.getPrecision(i))); //字段长度
				field.put("NULLABLE", rsme.isNullable(i) == ResultSetMetaData.columnNullable ? "YES" : "NO"); //是否为空
				field.put("REMARK", rsme.getColumnLabel(i)); //字段注释
				
				table.add(field);
			}

		} catch (Exception e) {
			log.error(String.format("Get Metadata FAIL: ", e.getMessage()));
		}
		return table;
	}

	/**
	 * 通过表名获取元数据信息
	 * 
	 * @param schema 模式名称
	 * @param tableName 表名称
	 * 
	 * @return 元数据信息
	 */
	public List<Map<String, String>> getMetadataByTable(String schema, String tableName) {
		
		List<Map<String, String>> table = new ArrayList<Map<String, String>>();
		
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			ResultSet rs = metadata.getColumns(null, schema, tableName, null);
			
			while (rs.next()) {
				
				Map<String, String> field = new HashMap<String, String>();
				
				field.put("NAME",  rs.getString("COLUMN_NAME"));// 字段名称
				field.put("TYPE", rs.getString("TYPE_NAME")); //字段类型名称(例如：VACHAR2)
				field.put("SIZE", Integer.toString(rs.getInt("COLUMN_SIZE"))); //字段长度
				field.put("NULLABLE", rs.getString("IS_NULLABLE")); //是否为空
				field.put("REMARK", rs.getString("REMARKS")); //字段注释
				
				table.add(field);
				
			}
		} catch (Exception e) {
			log.error(String.format("Get Metadata FAIL: ", e.getMessage()));
		}
		return table;
	}
}
