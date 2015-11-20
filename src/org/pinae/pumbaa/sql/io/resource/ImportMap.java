package org.pinae.pumbaa.sql.io.resource;

import org.pinae.nala.xb.annotation.Attribute;

/**
 * 引入的SQL文件
 * 
 * @author Huiyugeng
 *
 */
public class ImportMap {
	@Attribute(name = "file")
	private String file; // 引入文件名称

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
