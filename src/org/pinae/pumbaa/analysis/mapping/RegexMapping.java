package org.pinae.pumbaa.analysis.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 对文本消息进行正则表达式映射
 * 
 * @author Huiyugeng
 *
 *
 */
public class RegexMapping implements Mapping{
	private static Logger log = Logger.getLogger(RegexMapping.class);
	
	private Map<String, Pattern> patternMap = new HashMap<String, Pattern>(); //正则匹配
	private Map<String, Map<String, String[]>> itemMap = new HashMap<String, Map<String, String[]>>(); //正则值映射
	
	public RegexMapping(Map<String, String> pattern, Map<String, Map<String, String[]>> items){
		Set<String> patternKeySet = pattern.keySet();
		for (String patternKey : patternKeySet) {
			this.patternMap.put(patternKey, Pattern.compile(pattern.get(patternKey)));
		}
		this.itemMap = items;
	}
	
	@Override
	public Map<String, String> map(Object data) {
		Map<String, String> result = null;
		
		Set<String> mapNameSet = patternMap.keySet();
		for (String mapName : mapNameSet){
			
			Pattern pattern = patternMap.get(mapName);
			
			Matcher matcher = pattern.matcher(data.toString());
			while(matcher.find()) {
				Map<String, String[]> itemConfigMap = itemMap.get(mapName); 
				
				if (itemConfigMap != null && itemConfigMap.size() > 0){
					result = new HashMap<String, String>();
					
					for(int i = 1; i <= matcher.groupCount(); i++){
						String matchParameters[] = itemConfigMap.get(Integer.toString(i));
						String value = matcher.group(i);
						
						if (matchParameters != null && matchParameters.length > 1 && StringUtils.isNotEmpty(value)){
							//根据key进行值设定
							if (matchParameters[0].equals("match") && matchParameters.length == 2){
								result.put(matchParameters[1], value.trim());
							}
							//切分后进行key设定
							else if (matchParameters[0].equals("split") && matchParameters.length == 3){
								
								String split = matchParameters[1];
								Pattern subPattern = Pattern.compile(matchParameters[2]);
								String valueItems[] = value.split(split);
								
								for (String valueItem : valueItems){
									Matcher subMatcher = subPattern.matcher(valueItem);
									while(subMatcher.find()){
										String itemKey = subMatcher.group(1);
										String itemValue = null;
										
										List<String> tempValue = new ArrayList<String>();
										for (int j = 2; j<=subMatcher.groupCount(); j++){
											tempValue.add(subMatcher.group(j));
										}
										itemValue = StringUtils.join(tempValue, " ");
										if (StringUtils.isNotEmpty(itemKey) && StringUtils.isNotEmpty(itemValue)){
											result.put(itemKey, itemValue.trim());
										}
									}
								}
							}
							
						}
					}
				}
				
				String _mapName[] = mapName.split(":");
				if (_mapName.length >= 2){
					result.put("_type", _mapName[0]);
					result.put("_name", _mapName[1]);
				}else{
					result.put("_type", mapName);
					result.put("_name", mapName);
				}
				
				if (result != null){
					return result;
				}
			}
		}
		return result;
	}
	

	

}
