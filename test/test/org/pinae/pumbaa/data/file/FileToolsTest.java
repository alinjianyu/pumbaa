package test.org.pinae.pumbaa.data.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.data.file.FileTools;

public class FileToolsTest {
	@Test
	public void testGroupFile() {
		Map<String, List<String>> fileGroup = FileTools.groupFile("D:\\TestData\\was_log\\was_log",
				"was_(\\d+)-(\\d+)-(\\d+)\\S+.log");

		for (List<String> fileList : fileGroup.values()) {
			System.out.println(fileList.size());
		}

	}

	@Test
	public void testRenameFileSet() {
		for (int i = 6; i <= 7; i++) {
			List<String> filenames = FileTools.getFileList(String.format("E:\\TestData\\formatLog\\2015%02d", i));
			FileTools.renameFileSet(filenames, String.format("2015%02d_{count}.log", i));
		}
	}

	@Test
	public void testCopyFileSet() {
		List<String> dirList = new ArrayList<String>();
		for (int i = 4; i <= 12; i++) {
			dirList.add(String.format("D:\\TestData\\formatLog\\2014%02d", i));
		}
		FileTools.copyFileSet(dirList, "D:\\TestData\\2014_4a.log");
	}
}
