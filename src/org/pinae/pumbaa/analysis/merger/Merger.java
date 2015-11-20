package org.pinae.pumbaa.analysis.merger;

import java.util.List;

/**
 * 数据归并
 * 
 * @author Linjianyu
 *
 */
public interface Merger {
	public List<Object> merge(List<Object> data);
}
