package org.pinae.pumbaa.sql.io.resource;

import java.util.ArrayList;
import java.util.List;

import org.pinae.nala.xb.annotation.Attribute;
import org.pinae.nala.xb.annotation.Element;
import org.pinae.nala.xb.annotation.ElementValue;

/**
 * SQL语句
 *
 * @author Huiyugeng
 */
public class SQLMap {
	@Attribute(name = "name")
	private String name; // SQL名称

	@Element(name = "choose")
	private List<Choose> chooseList = new ArrayList<Choose>(); // 选择条件

	@ElementValue
	private String value; // SQL语句

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Choose> getChooseList() {
		return chooseList;
	}

	public void setChooseList(Choose choose) {
		this.chooseList.add(choose);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public class Choose {
		@Attribute(name = "when")
		private String when; // 选择条件

		@ElementValue
		private String value; // 条件值ß

		public String getWhen() {
			return when;
		}

		public void setWhen(String when) {
			this.when = when;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
