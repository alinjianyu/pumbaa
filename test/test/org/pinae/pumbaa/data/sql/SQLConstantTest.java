package test.org.pinae.pumbaa.data.sql;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.sql.SQLReader;


public class SQLConstantTest {
	
	@Test
	public void testGetSQLByName() {
		String sql = SQLReader.getSQLByName("GET_ID");
		assertEquals(sql, "select id from test");
	}
	
	@Test
	public void testGetSQLByNameWithParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", 20);
		String sql = SQLReader.getSQLByNameWithParameters("GET_TEST", parameters);
		assertEquals(sql, "select * from test where 1=1 and id = 20");
	}
	
	@Test
	public void testGetSQLByNameWithParameters2() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("word", "$(INST)_CDR_GPRS_WORK");
		String sql = SQLReader.getSQLByNameWithParameters("GET_TEST2", parameters);
		assertEquals(sql, "select * from test where 1=1 and word = '$(INST)_CDR_GPRS_WORK'");
	}
	
	@Test
	public void testGetSQLByNameWithParametersBean() {

		User user = new User();
		user.setId(3);
		user.setName("Joe");
		user.setAge(20);
		user.setPhone("13391562775");
		
		String sql = SQLReader.getSQLByNameWithParameters("INSERT_DATA", user);
		assertEquals(sql, "insert into test(id, name, age, phone) values (3, 'Joe', 20, '13391562775')");
	}
	
	@Test
	public void testGetLimitSQL() {
		String sql = SQLReader.getSQLByName("GET_ID");
		sql = SQLReader.getLimitSQL(sql, 10, 10, "oracle");
		assertEquals(sql, "select * from ( select row_.*, rownum rownum_ from ( select id from test ) row_ where rownum <= 20) where rownum_ > 10");
	}
	
	private class User {
		private int id;
		private String name;
		private int age;
		private String phone;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		
	}
}
