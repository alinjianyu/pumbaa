package org.pinae.pumbaa.sql.io.resource;

import java.util.ArrayList;
import java.util.List;

import org.pinae.nala.xb.annotation.Element;
import org.pinae.nala.xb.annotation.Root;

/**
 * SQL列表
 * 
 * @author Huiyugeng
 *
 */
@Root(name = "sqls")
public class SQLListMap {
	
	// 引入文件列表
	@Element(name = "import")
	private List<ImportMap> importList = new ArrayList<ImportMap>();

	// 全局变量列表
	@Element(name = "global")
	private List<GlobalMap> globalList = new ArrayList<GlobalMap>(); 

	// SQL列表
	@Element(name = "sql")
	private List<SQLMap> sqlList = new ArrayList<SQLMap>();

	public List<SQLMap> getSqlList() {
		return sqlList;
	}

	public void setSqlList(SQLMap sql) {
		this.sqlList.add(sql);
	}

	public List<ImportMap> getImportList() {
		return importList;
	}

	public void setImportList(ImportMap sqlImport) {
		this.importList.add(sqlImport);
	}

	public List<GlobalMap> getGlobalList() {
		return globalList;
	}

	public void setGlobalList(GlobalMap global) {
		this.globalList.add(global);
	}

}