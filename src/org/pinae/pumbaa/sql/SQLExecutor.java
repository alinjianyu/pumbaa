package org.pinae.pumbaa.sql;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.pumbaa.util.ClassLoaderUtils;


/**
 * SQL执行器
 * 
 * @author Huiyugeng
 *
 */
public class SQLExecutor {

	private static Logger log = Logger.getLogger(SQLExecutor.class);

	private Connection conn = null;

	/**
	 * 构造函数
	 */
	public SQLExecutor() {
		
	}

	/**
	 * 构造函数
	 * 
	 * @param filename JDBC配置文件名
	 */
	public SQLExecutor(String filename) {
		
	}
	
	/**
	 * 构造函数
	 * 
	 * @param driver 驱动名称
	 * @param url JDBC URL地址
	 * @param user 数据库用户
	 * @param password 数据库密码
	 */
	public SQLExecutor(String driver, String url, String user, String password) {
		
	}

	/**
	 * 执行Select查询
	 * 
	 * @param sql Select语句
	 * 
	 * @return 执行结果
	 */
	public List<Object[]> select(String sql) {
		if (StringUtils.isEmpty(sql)) {
			return null;
		} else {
			sql = sql.trim();
		}

		if (sql.toLowerCase().startsWith("select")) {

			ResultSet rs = null;
			Statement stmt = null;

			try {
				stmt = conn.createStatement();
				List<Object[]> table = new ArrayList<Object[]>();

				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();

				int columnCount = rsmd.getColumnCount();
				while (rs.next()) {
					Object[] row = new Object[columnCount];
					for (int i = 0; i < columnCount; i++) {
						row[i] = rs.getObject(i + 1);
					}
					table.add(row);
				}

				return table;
			} catch (SQLException e) {
				log.error(String.format("%s : %s", sql, e.getMessage()));
			} finally {
				try {
					if (rs != null && rs.isClosed() == false) {
						rs.close();
					}
					if (stmt != null && stmt.isClosed() == false) {
						stmt.close();
					}
				} catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql 需要执行的SQL
	 * 
	 * @return 是否执行成功
	 */
	public boolean execute(String sql) {
		if (StringUtils.isEmpty(sql)) {
			return false;
		}

		boolean result = false;

		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			result = true;
		} catch (SQLException e) {
			log.error(String.format("%s : %s", sql, e.getMessage()));
		} finally {
			try {
				if (stmt != null && stmt.isClosed() == false) {
					stmt.close();
				}
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}

		return result;
	}
	
	/**
	 * 执行SQL语句
	 * 
	 * @param sql 需要执行的SQL
	 * 
	 * @return 是否执行成功
	 */
	public boolean execute(List<String> sqlList) {
		
		if (sqlList == null || sqlList.size() == 0) {
			return false;
		}

		boolean result = false;

		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			for (String sql : sqlList) {
				if (StringUtils.isNotEmpty(sql)) {
					stmt.addBatch(sql);
				}
			}
			stmt.executeBatch();
			result = true;
		} catch (SQLException e) {
			log.error(String.format("%s", e.getMessage()));
		} finally {
			try {
				if (stmt != null && stmt.isClosed() == false) {
					stmt.close();
				}
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}

		return result;
	}

	/**
	 * 关闭数据库连接
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
}
