package test.org.pinae.pumbaa.data.file;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.data.file.FileTools;

public class FileUtilsTest {
	@Test
	public void testGroupFile() {
		Map<String, List<String>> fileGroup = FileTools.groupFile("D:\\TestData\\was_log\\was_log", "was_(\\d+)-(\\d+)-(\\d+)\\S+.log");
		
		for (List<String> fileList : fileGroup.values()) {
			System.out.println(fileList.size());
		}
		
	}
}
