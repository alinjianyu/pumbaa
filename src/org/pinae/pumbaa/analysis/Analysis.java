package org.pinae.pumbaa.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.pumbaa.analysis.assoc.MapAssoc;
import org.pinae.pumbaa.analysis.comparer.Comparer;
import org.pinae.pumbaa.analysis.comparer.ObjectComparer;
import org.pinae.pumbaa.analysis.filter.DictFilter;
import org.pinae.pumbaa.analysis.filter.Filter;
import org.pinae.pumbaa.analysis.filter.RegexFilter;
import org.pinae.pumbaa.analysis.group.Group;
import org.pinae.pumbaa.analysis.group.GroupCount;
import org.pinae.pumbaa.analysis.group.GroupFilter;
import org.pinae.pumbaa.analysis.group.GroupJoin;
import org.pinae.pumbaa.analysis.group.GroupSum;
import org.pinae.pumbaa.analysis.join.Join;
import org.pinae.pumbaa.analysis.join.ObjectJoin;
import org.pinae.pumbaa.analysis.mapping.Mapping;
import org.pinae.pumbaa.analysis.mapping.RegexMapping;
import org.pinae.pumbaa.analysis.mapping.SplitMapping;
import org.pinae.pumbaa.analysis.replace.DictReplace;
import org.pinae.pumbaa.analysis.replace.Replace;

public class Analysis {
	
	private static Logger log = Logger.getLogger(Analysis.class);
	
	@SuppressWarnings("unchecked")
	public List<String> filter(List<String> content, List<Operate> operateList) {
		
		List<Filter> filterList = new ArrayList<Filter>();
		
		for (Operate operate : operateList){
			Filter filter = null;
			
			if (StringUtils.equalsIgnoreCase(operate.getOperate(), "regex-filter")) {
				Object pattern = operate.getParameterByName("pattern");
				Object expression = operate.getParameterByName("expression");
				
				if (pattern != null && expression != null && pattern instanceof Map && expression instanceof String) {
					filter = new RegexFilter((Map<String, String>)pattern, (String)expression);
				}
			} else if (StringUtils.equalsIgnoreCase(operate.getOperate(), "filter-dict")) {
				Object dictionary = operate.getParameterByName("dictionary");
				
				if (dictionary != null && dictionary instanceof List) {
					filter = new DictFilter((List<String>)dictionary);
				}
			}
			if (filter != null){
				filterList.add(filter);
			}
		}
		
		List<String> result = new ArrayList<String>();
		
		if (filterList !=null && filterList.size() >0 ){
	        for (String line : content){
	        	
	        	if (StringUtils.isEmpty(line)){
	        		continue;
	        	}
	        	
	        	for (Filter filter : filterList) {
	        		line = filter.filter(line);
	        	}
	        	
	        	if (line != null){
	        		result.add(line);
	        	}
	        }
			return result;
		} else {
			return content;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> replace(List<String> content, List<Operate> operateList){
		
		List<Replace> replaceList = new ArrayList<Replace>();
		
		for (Operate operate : operateList){
			Replace replace = null;
			
			if (StringUtils.equalsIgnoreCase(operate.getOperate(), "replace-dict")){
				Object pattern = operate.getParameterByName("pattern");
				
				if (pattern != null && pattern instanceof Map) {
					replace = new DictReplace((Map<String, String>)pattern);
				}
			}

			
			if (replace != null){
				replaceList.add(replace);
			}
		}
		
		List<String> result = new ArrayList<String>();
        
        for (String line : content){
        	
        	if (StringUtils.isEmpty(line)){
        		continue;
        	}

			for (Replace replace : replaceList) {
        		line = replace.replace(line);
        	}
        	
        	if (line != null){
        		result.add(line);
        	}
        	
        }
        
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> map(List<String> content, Operate operate) {
		
		Mapping mapping = null;
		
		String opt = operate.getOperate();
		
		if (StringUtils.equalsIgnoreCase(opt, "regex-map")){
			Object pattern = operate.getParameterByName("pattern");
			Object items = operate.getParameterByName("items");
			
			if (pattern != null && items != null && pattern instanceof Map && items instanceof Map) {
				mapping = new RegexMapping((Map<String, String>)pattern, (Map<String, Map<String, String[]>>)items);
			}
		} else if (StringUtils.equalsIgnoreCase(opt, "split-map")){
			Object columns = operate.getParameterByName("columns");
			Object split = operate.getParameterByName("split");
			
			if (columns != null && split != null && columns instanceof String[] && split instanceof String) {
				mapping = new SplitMapping((String[])columns, (String)split);
			}
		}
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        
		if (mapping != null) {
	        for (String line : content){
	        	if (StringUtils.isEmpty(line)){
	        		continue;
	        	}
	        	
	        	Map<String, String> mapResult = mapping.map(line);
	        	
	        	if (mapResult != null) {
	        		result.add(mapResult);
	        	}
	        }
	        return result;
		} else {
			return null;
		}
		
	}
	
	public Map<String, Map<String, Object>> group(List<String> content, List<Operate> operateList) throws IOException{
		List<String> groupNameList = new ArrayList<String>();
		
		Map<String, Group> groupMap = new HashMap<String, Group>();
		Map<String, DataConvert> convertMap = new HashMap<String, DataConvert>();

        Map<String, int[]> columnpMap = new HashMap<String, int[]>();
        for (Operate operate : operateList) {
        	
        	String[] columns = operate.hasParameter(Group.ALL_COLUMNS, String[].class) ? 
        			(String[])operate.getParameterByName(Group.ALL_COLUMNS) : null;
        	String[] groupColumns = operate.hasParameter(Group.GROUP_COLUMNS, String[].class) ?
        			(String[])operate.getParameterByName(Group.GROUP_COLUMNS) : null;
        	
        	//获取需要聚合的列在全部列中的index值
    		int columnIndex[] = new int[groupColumns.length];
    		for (int i = 0; i < groupColumns.length; i++){
    			for (int j = 0; j < columns.length; j++){
    				if (groupColumns[i].equals(columns[j])){
    					columnIndex[i] = j; 
    				}
    			}
    		}
    		
    		columnpMap.put(operate.getName(), columnIndex);
        }
        
        for (Operate operate : operateList){
        	String name = operate.getName();
    		String opt = operate.getOperate();
    		
    		Group group = null;
    		if (StringUtils.equalsIgnoreCase(opt, Group.GROUP_COUNT)) {
    			group = new GroupCount();
    		} else if (StringUtils.equalsIgnoreCase(opt, Group.GROUP_JOIN)){
    			group = new GroupJoin();
    		} else if (StringUtils.equalsIgnoreCase(opt, Group.GROUP_SUM)){
    			group = new GroupSum();
    		} else if (StringUtils.equalsIgnoreCase(opt, Group.GROUP_FILTE)){
    			group = new GroupFilter();
    		}else {
    			continue;
    		}
    		
    		if (name != null) {
    			
    			groupNameList.add(name);
    			
    			if (group != null){
        			groupMap.put(name, group);
        		}
        		
        		Object convert = operate.getParameterByName(Group.DATA_CONVERT);
        		if (convert != null && convert instanceof DataConvert) {
        			convertMap.put(name, (DataConvert)convert);
        		}
    		}
        }
        
        Map<String, Map<String, Object>> groupPool = new HashMap<String, Map<String, Object>>();
				
        for (String line : content){
  	
        	if (StringUtils.isEmpty(line)){
        		continue;
        	}
        	
        	for (String name : groupNameList) {
        		Group group = groupMap.get(name);
        		DataConvert convert = convertMap.get(name);
        		
        		if (group == null || convert == null){
        			continue;
        		}
        		
        		String data[] = null;
        		
	        	data = ((DataConvert)convert).convert(line);
	        	if (data == null) {
	        		continue;
	        	}

	    		Map<String, Object> statMap = groupPool.get(name);
	    		if (statMap == null) {
	    			statMap = new HashMap<String, Object>();
	    		}
	    		
	    		String key = group.getKey(data, columnpMap.get(name));
	    		
	    		Object value = statMap.get(key);
	    		value = group.getValue(value, data, columnpMap.get(name));
	    		
	    		if (StringUtils.isNotEmpty(key) && value != null) {
	    			statMap.put(key, value);
	    			groupPool.put(name, statMap);
	    		}
        	}
		}
        
        return groupPool;
		
	}

	public Object join(Object src, Object dst){
		if (src != null && dst != null) {
			Join join = new ObjectJoin();
			return join.join(src, dst);
		} else {
			if (src != null) {
				return src;
			} else if (dst != null) {
				return dst;
			}
		}
		return null;
	}
	
	public Object compare(Object src, Object dst){
		if (src != null && dst != null) {
			Comparer comparer = new ObjectComparer();
			return comparer.compare(src, dst);
		} else {
			if (src != null) {
				return src;
			} else if (dst != null) {
				return dst;
			}
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object assoc(Object src, Object dst, Operate operate){
		
		String opt = operate.getOperate();
		
		if (StringUtils.equalsIgnoreCase(opt, "assoc-map")){
			if (src != null && dst != null && src instanceof Map<?, ?> && dst instanceof Map<?, ?>) {
				Object srcKey = operate.getParameterByName("src");
				Object dstKey = operate.getParameterByName("dst");
				Object expression = operate.getParameterByName("expression");
				
				MapAssoc mapAssoc = new MapAssoc();
				return mapAssoc.assoc((Map)src, (Map)dst, (String)srcKey, (String)dstKey, (String)expression);
			} else {
				if (src != null) {
					return src;
				} else if (dst != null) {
					return dst;
				}
			}
		}

		
		return null;
	}
}
