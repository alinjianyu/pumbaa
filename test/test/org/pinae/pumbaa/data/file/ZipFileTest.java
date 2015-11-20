package test.org.pinae.pumbaa.data.file;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.pinae.pumbaa.data.file.ZipFile;
import org.pinae.pumbaa.util.ClassLoaderUtils;

public class ZipFileTest {
	@Test
	public void testZip() {
		String path = ClassLoaderUtils.getResourcePath("");
		
		List<String> zipFileList = new ArrayList<String>();
		zipFileList.add(path + "log4j.properties");
		zipFileList.add(path + "sql.xml");
		try {
			ZipFile.zip(zipFileList, "test.zip");
			assertTrue(new File("test.zip").exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUnzip() {
		try {
			ZipFile.unzip("test.zip", "test-zip");
			assertTrue(new File("test-zip/log4j.properties").exists());
			assertTrue(new File("test-zip/sql.xml").exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
