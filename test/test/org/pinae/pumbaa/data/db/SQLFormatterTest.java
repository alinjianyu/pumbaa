package test.org.pinae.pumbaa.data.db;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.pinae.pumbaa.data.db.SQLFormatter;

public class SQLFormatterTest {
	
	private static Logger log = Logger.getLogger(SQLFormatterTest.class);
	
	@Test
	public void testFormat() {
		String sql = "select LOG_TIME, sum(LOG_COUNT)  "
				+ "from ("
				+ "select to_char(to_date(LOG_TIME, 'YYYY-MM-DD HH24'), 'YYYY-MM-DD') as LOG_TIME, LOG_COUNT "
				+ "from STAT_LOG_SYSTEM_COUNT "
				+ " where LOG_TIME like '2014-05%') group by LOG_TIME order by LOG_TIME;";
		
		log.info(new SQLFormatter().format(sql));
	}
}
