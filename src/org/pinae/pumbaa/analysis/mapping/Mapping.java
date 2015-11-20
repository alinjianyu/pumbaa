package org.pinae.pumbaa.analysis.mapping;

import java.util.Map;

public interface Mapping {

	/**
	 * 根据规则将消息进行映射
	 * 
	 * @param data 消息内容
	 * 
	 * @return 映射后的Map
	 */
	public Map<String, String> map(Object data);
}
