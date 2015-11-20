package org.pinae.pumbaa.sql.io.resource;

import org.pinae.nala.xb.annotation.Attribute;

/**
 * 全局变量
 *
 */
public class GlobalMap {
	@Attribute(name = "key")
	private String key; // 全局变量键名称

	@Attribute(name = "value")
	private String value; // 全局变量值名称

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
