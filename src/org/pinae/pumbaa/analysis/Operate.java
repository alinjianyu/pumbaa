package org.pinae.pumbaa.analysis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 操作配置
 * 
 * @author Huiyugeng
 *
 */
public class Operate {

	private String name; //操作名
	private String operate; //操作内容
	private Map<String, Object> parameters = new HashMap<String, Object>(); //操作参数
	
	public Operate(){
		
	}
	
	public Operate(String name, String operate){
		this.name = name;
		this.operate = operate;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOperate() {
		if (operate != null) {
			return operate.trim();
		} else {
			return null;
		}
	}
	
	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public boolean hasParameter(String name){
		return this.parameters.containsKey(name);
	}
	
	public boolean hasParameter(String name, Class<?> type){
		Object parameter = this.parameters.get(name);
		if (parameter != null && StringUtils.equals(parameter.getClass().getName(), type.getName())){
			return true;
		} else {
			return false;
		}
	}
	
	public Object getParameterByName(String name) {
		return this.parameters.get(name);
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String name, Object value){
		this.parameters.put(name, value);
	}

}
