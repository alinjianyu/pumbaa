package test.org.pinae.pumbaa.analysis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.pumbaa.analysis.Analysis;
import org.pinae.pumbaa.analysis.Operate;
import org.pinae.pumbaa.data.text.TextFile;

public class AnalysisTest {
	private Analysis analysis = new Analysis();
	
	private List<String> content;
	
	@Before
	public void setUp(){
		content = new TextFile().read("log\\test.log", "UTF-8");
	}
	
	
	@Test
	public void testFilter(){
		
		Operate operate = new Operate("Interface-Filter", "filter-regex");
		Map<String, String> pattern = new HashMap<String, String>();
		pattern.put("OPT1", ".*ccqrysubselectprods.*");
		
		operate.addParameter("pattern", pattern);
		operate.addParameter("expression", "OPT1");
		
		List<Operate> operateList = new ArrayList<Operate>();
		operateList.add(operate);
		
		List<String> filteContent = analysis.filter(content, operateList);
		
		assertTrue(filteContent.size() - content.size() < 0);
	}

	@Test
	public void testGroup(){
		
		String columns[] = {"RETDATA", "请求流水号", "接入时间", "返回时间", "处理时长", "号码", "接口业务标识", "渠道", "操作员",
    			"返回码", "返回信息", "入参", "出参"};

		Operate operate = new Operate("Time-Group", "group-count");
		operate.addParameter("columns", columns);
		operate.addParameter("group", new String[]{"RETDATA", "接口业务标识"});
		operate.addParameter("convert", new WASDataConvertByHour());
		
		List<Operate> operateList = new ArrayList<Operate>();
		operateList.add(operate);
		
		try{
			Map<String, Map<String, Object>> result = analysis.group(content, operateList);
			
			System.out.println(result);
		}catch(IOException e){
			fail(e.getMessage());
		}
		
	}
}
