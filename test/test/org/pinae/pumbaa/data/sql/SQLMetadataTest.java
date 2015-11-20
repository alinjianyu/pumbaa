package test.org.pinae.pumbaa.data.sql;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.sql.SQLMetadata;

public class SQLMetadataTest {
	
	@Test
	public void testGetMetadataBySQL() {
		SQLMetadata sqlMetadata = new SQLMetadata("oracle.jdbc.driver.OracleDriver", 
				"jdbc:oracle:thin:@192.168.0.13:1521:letour", 
				"cirm", 
				"cirm");
		List<Map<String, String>> table = sqlMetadata.getMetadataBySQL("select * from CIRM_USER");
		assertEquals(table.size(), 9);
	}
	
	@Test
	public void testGetMetadataByTable() {
		SQLMetadata sqlMetadata = new SQLMetadata("oracle.jdbc.driver.OracleDriver", 
				"jdbc:oracle:thin:@192.168.0.13:1521:letour", 
				"cirm", 
				"cirm");
		List<Map<String, String>> table = sqlMetadata.getMetadataByTable(null, "CIRM_USER");
		assertEquals(table.size(), 9);
	}
}
