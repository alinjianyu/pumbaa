package org.pinae.pumbaa.analysis.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

/**
 * 正则表达过滤
 * 
 * @author Huiyugeng
 *
 */
public class RegexFilter implements Filter{
	
	private Map<String, String> patterns = new HashMap<String, String>();
	private String expression;
	
	public RegexFilter(Map<String, String> patterns, String expression) {
		this.patterns = patterns;
		this.expression = expression;
	}
	
	@Override
	public String filter(String data) {
		
		if (patterns == null) {
			return data;
		}
		
		if (data != null) {
			List<Variable> variables = new ArrayList<Variable>();
			
			Set<String> ruleNameSet = patterns.keySet();
			for (String ruleName : ruleNameSet){
				String pattern = patterns.get(ruleName);
				if (data.matches(pattern)){
					variables.add(Variable.createVariable(ruleName, true));
				} else {
					variables.add(Variable.createVariable(ruleName, false));
				}
			}
			Object evaResult = ExpressionEvaluator.evaluate(expression, variables);
			boolean pass = BooleanUtils.toBoolean(String.valueOf(evaResult));
			
			if (pass) {
				return data;
			} else {
				return null;
			}
		} else {
			return null;
		}
		
	}

}
