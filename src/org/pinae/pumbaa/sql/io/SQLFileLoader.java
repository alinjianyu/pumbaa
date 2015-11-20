package org.pinae.pumbaa.sql.io;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pinae.nala.xb.exception.NoSuchPathException;
import org.pinae.nala.xb.exception.UnmarshalException;
import org.pinae.nala.xb.unmarshal.Unmarshaller;
import org.pinae.nala.xb.unmarshal.XMLUnmarshaller;
import org.pinae.nala.xb.util.ResourceReader;
import org.pinae.pumbaa.sql.io.resource.GlobalMap;
import org.pinae.pumbaa.sql.io.resource.ImportMap;
import org.pinae.pumbaa.sql.io.resource.SQLListMap;
import org.pinae.pumbaa.sql.io.resource.SQLMap;

/**
 * 从文件中读取SQL配置
 * 
 * @author huiyugeng
 *
 */
public class SQLFileLoader {
	
	private static Logger log = Logger.getLogger(SQLFileLoader.class);

	private Map<String, SQLMap> sqlMap = new HashMap<String, SQLMap>(); // SQL集合

	public SQLFileLoader(String path, String filename) {
		if (path == null) {
			path = "";
		}
		read(path, filename, null);
	}

	/**
	 * 获取SQL列表
	 * 
	 * @param filename SQL文件
	 */
	private void read(String path, String filename, Map<String, String> globalVar) {
		if (globalVar == null) {
			globalVar = new HashMap<String, String>();
		}

		SQLListMap sqlList = new SQLListMap();

		Unmarshaller bind = null;
		try {
			InputStreamReader stream = new ResourceReader().getFileStream(path + filename);
			bind = new XMLUnmarshaller(stream);

			bind.setRootClass(SQLListMap.class);
			sqlList = (SQLListMap) bind.unmarshal();
		} catch (NoSuchPathException e) {
			e.printStackTrace();
		} catch (UnmarshalException e) {
			e.printStackTrace();
		}

		// 载入全局变量
		List<GlobalMap> globals = sqlList.getGlobalList();
		for (GlobalMap global : globals) {
			globalVar.put(global.getKey(), global.getValue());
		}

		Set<String> keySet = globalVar.keySet();

		List<SQLMap> sqls = sqlList.getSqlList();
		for (SQLMap sql : sqls) {
			String sqlStr = sql.getValue();
			for (String key : keySet) {
				// 全局变量替换
				String value = globalVar.get(key);
				key = ":" + key;
				if (sqlStr.contains(key)) {
					sqlStr = sqlStr.replaceAll(key, value);
				}
			}
			sql.setValue(sqlStr);
			sqlMap.put(sql.getName(), sql);
		}

		// 载入引入SQL
		List<ImportMap> imports = sqlList.getImportList();
		for (ImportMap sqlImport : imports) {
			read(path, sqlImport.getFile(), globalVar);
		}

	}

	public Map<String, SQLMap> getSQLMap() {
		return this.sqlMap;
	}
	


}
