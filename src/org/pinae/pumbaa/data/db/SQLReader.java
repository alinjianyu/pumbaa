package org.pinae.pumbaa.data.db;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pinae.nala.xb.exception.NoSuchPathException;
import org.pinae.nala.xb.exception.UnmarshalException;
import org.pinae.nala.xb.unmarshal.Unmarshaller;
import org.pinae.nala.xb.unmarshal.XmlUnmarshaller;
import org.pinae.nala.xb.util.ResourceReader;
import org.pinae.pumbaa.data.db.SQLList.Global;
import org.pinae.pumbaa.data.db.SQLList.Import;
import org.pinae.pumbaa.data.db.SQLList.SQL;

/**
 * 读取SQL配置文件
 * 
 * @author Linjianyu
 *
 */
public class SQLReader {
	
	private static Logger log = Logger.getLogger(SQLReader.class);

	private Map<String, SQL> sqlMap = new HashMap<String, SQL>(); // SQL集合

	public SQLReader(String path, String filename) {
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

		SQLList sqlList = new SQLList();

		Unmarshaller bind = null;
		try {
			InputStreamReader stream = new ResourceReader().getFileStream(path + filename);
			bind = new XmlUnmarshaller(stream);

			bind.setRootClass(SQLList.class);
			sqlList = (SQLList) bind.unmarshal();
		} catch (NoSuchPathException e) {
			e.printStackTrace();
		} catch (UnmarshalException e) {
			e.printStackTrace();
		}

		// 载入全局变量
		List<Global> globals = sqlList.getGlobalList();
		for (Global global : globals) {
			globalVar.put(global.getKey(), global.getValue());
		}

		Set<String> keySet = globalVar.keySet();

		List<SQL> sqls = sqlList.getSqlList();
		for (SQL sql : sqls) {
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
		List<Import> imports = sqlList.getImportList();
		for (Import sqlImport : imports) {
			read(path, sqlImport.getFile(), globalVar);
		}

	}

	public Map<String, SQL> getSQLMap() {
		return this.sqlMap;
	}

}
