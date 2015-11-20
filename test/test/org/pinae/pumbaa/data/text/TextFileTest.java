package test.org.pinae.pumbaa.data.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.pumbaa.data.file.FileTools;
import org.pinae.pumbaa.data.text.TextFile;

public class TextFileTest {

	
	//@Test
	public void testSplit(){
		TextFile txtFile = new TextFile();
			
		List<String> filelist = FileTools.getFileList("d:\\TestData\\orgnialLog\\201502");
			
		//new File("d:\\TestData\\formatLog\\201411").mkdir();
			
		for (String file : filelist) {
			txtFile.split(file, "GBK", String.format("d:\\TestData\\formatLog\\201502"), "log", 10000);
		}
	}
	
	//@Test
	public void testFilte(){
		TextFile txtFile = new TextFile();
		
		List<String> filteList = new ArrayList<String>();
		
		filteList.add(".*select.*from dual.*");
		filteList.add(".*gsh create.*");
		filteList.add(".*test_gw_selection.*");
		filteList.add(".*select");
		filteList.add(".*delete");
		filteList.add(".*subscriber-interface.*");
		filteList.add(".*vprn.*customer.*");
		filteList.add(".*mysql.*");
		filteList.add(".*nohup.*");
		filteList.add("group-interface");
		filteList.add(".*gsh delete_subscriber.*");
		filteList.add(".*feidingzhi_group_imei.*");
		filteList.add(".*dingzhi_group_imei.*");
		filteList.add(".*T.F_4.*T.F_16.*");
		filteList.add(".*------.*-----.*");
		
		
		List<String> filelist = FileTools.getFileList("D:\\TestData\\formatLog\\201502");
		for (String file : filelist) {
			System.out.println(file);
			txtFile.filte(file, "GBK", filteList);
		}
		
	}
	
	//@Test
	public void testJoin(){
		TextFile txtFile = new TextFile();
		txtFile.join("d:\\7.log", "GBK", "D:\\TestData\\log2");
	}
	
	//@Test
	public void testRebuild(){
		TextFile txtFile = new TextFile();
		List<String> filelist = FileTools.getFileList("D:\\TestData\\formatLog\\201502");
		for (String file : filelist) {
			
			List<String> result = new ArrayList<String>();
			
			List<String> content = txtFile.read(file, "GBK");
			for (String line : content) {
				String items [] = line.split("\\|\\|\\|");
				
				if (items!= null && items.length == 8){
					result.add(items[0] + "|||" + items[3] + "|||" + items[4] + "|||" + items[5] + "|||" + items[6] + "|||" + items[7]);
				}
			}
			
			txtFile.write(file, "GBK", result);
		}
		
	}
	
	//@Test
	public void testJoin2() {
		TextFile txtFile = new TextFile();
		Map<String, List<String>> fileGroup = FileTools.groupFile("D:\\TestData\\was_log\\was_log", "was_(\\d+)-(\\d+)-(\\d+)\\S+.log");
		
		for (String filename : fileGroup.keySet()) {
			List<String> fileList = fileGroup.get(filename);
			txtFile.join(String.format("d:\\TestData\\was_log_%s.log", filename), "GBK", fileList);
		}
		
	}
	
}
