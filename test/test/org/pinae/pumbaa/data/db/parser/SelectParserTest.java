package test.org.pinae.pumbaa.data.db.parser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.Set;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import org.junit.Test;
import org.pinae.pumbaa.data.db.parser.SelectParser;

public class SelectParserTest {
	
	private CCJSqlParserManager parserManager = new CCJSqlParserManager();
	
	private SelectParser selectParser = new SelectParser();
	
	@Test
	public void testParserSQL() {
		String sql = "select t1.name, t1.password, t1.age, t2.department from user t1, department t2 where t1.department_id = t2.id"
				+ " union "
				+ "select t1.name, t1.password, t1.age, t1.department from (select * from user_temp) t1";
		
		try {
			Statement statement = parserManager.parse(new StringReader(sql));
			if (statement instanceof Select) {
				Set<String> tableSet = selectParser.parse((Select)statement);
				assertEquals(tableSet.size(), 3);
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
		}
	}
}
