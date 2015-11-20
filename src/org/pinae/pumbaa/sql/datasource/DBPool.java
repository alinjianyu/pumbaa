package org.pinae.pumbaa.sql.datasource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.pinae.pumbaa.util.ClassLoaderUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库连接管理
 * 
 * @author Huiyugeng
 * 
 */
public class DBPool {

	private static Logger log = Logger.getLogger(DBPool.class);
	
	private static DBPool dbPool;
	
	static {
		dbPool = new DBPool();
	}

	private ComboPooledDataSource dataSource;

	private String driver; // JDBC驱动程序
	private String url; // JDBC连接地址
	private String user; // JDBC连接用户
	private String password; // JDBC密码

	public DBPool() {
		this(ClassLoaderUtils.getResourcePath("") + "database.properties");
	}

	public DBPool(String filename) {
		try {

			Properties properties = new Properties();

			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			properties.load(in);

			this.driver = properties.getProperty("driver");
			this.url = properties.getProperty("url");
			this.user = properties.getProperty("user");
			this.password = properties.getProperty("password");

			// 是否激活池
			boolean isPool = Boolean.parseBoolean(properties.getProperty("active_pool", "false"));

			if (isPool) {
				dataSource = new ComboPooledDataSource();
				dataSource.setJdbcUrl(url);
				dataSource.setDriverClass(driver);
				dataSource.setUser(user);
				dataSource.setPassword(password);
				dataSource.setInitialPoolSize(2);
				dataSource.setMinPoolSize(1);
				dataSource.setMaxPoolSize(10);
				dataSource.setMaxStatements(50);
				dataSource.setMaxIdleTime(60);
			}

		} catch (Exception e) {
			log.error("Connection Database Error :" + e.getMessage());
		}
	}
	

	/**
	 * 获取数据库连接
	 * 
	 * @return 数据库连接
	 */
	public final static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dbPool.dataSource.getConnection();
		} catch (SQLException e) {
			log.error("Connection Database Error :" + e.getMessage());
		}
		return conn;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param driver 驱动名称
	 * @param url JDBC URL地址
	 * @param user 数据库用户
	 * @param password 数据库密码
	 * 
	 * @return 数据库连接
	 */
	public final static Connection getConnection(String driver, String url, String user, String password) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			log.error("Connection Database Error :" + e.getMessage());
		}
		return conn;
	}
}
