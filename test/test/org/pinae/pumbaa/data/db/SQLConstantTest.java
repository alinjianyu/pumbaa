package test.org.pinae.pumbaa.data.db;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.data.db.SQLConstant;


public class SQLConstantTest {

	public void testGetSQLByName() {
		String sql = SQLConstant.getSQLByName("GET_ID");
		assertEquals(sql, "select id from test");
	}
	
	@Test
	public void testGetSQLByNameWithParameters1() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", 20);
		String sql = SQLConstant.getSQLByNameWithParameters("GET_TEST", parameters);
		System.out.println(sql);
		assertEquals(sql, "select * from test where 1=1 and id = 20");
	}

	public void testGetSQLByNameWithParameters2() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", 20);
		String sql = SQLConstant.getSQLByNameWithParameters("GET_TEST2", parameters);
		assertEquals(sql, "select * from test where 1=1 and id = 20 order by id");
		
		parameters.put("name", "Hui");
		sql = SQLConstant.getSQLByNameWithParameters("GET_TEST2", parameters);
		assertEquals(sql, "select * from test where 1=1 and id = 20 and name = 'Hui' order by id");
	}

	public void testGetSQLByNameWithObject() {

		User user = new User();
		user.setId(3);
		user.setName("Joe");
		user.setAge(20);
		user.setPhone("13391562775");
		
		String sql = SQLConstant.getSQLByNameWithParameters("INSERT_DATA", user);
		assertEquals(sql, "insert into test(id, name, age, phone) values (3, 'Joe', 20, '13391562775')");
	}

	public void testGetSQLByNameWithParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", 20);
		String sql = SQLConstant.getSQLByNameWithParameters("GET_TEST3", parameters);
		assertEquals(sql, "select name from (select name from (select name from USER1 where id = 20) t1 union select name from (select name from USER2 order by id) t2) t order by id");
	}
	

	public void testGetLimitSQL() {
		String sql = SQLConstant.getSQLByName("GET_ID");
		sql = SQLConstant.getLimitSQL(sql, 10, 10, "oracle");
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
