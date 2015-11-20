package test.org.pinae.pumbaa.analysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class WASDataConvertByHour extends WASDataConvertByMinute {
	
	private Logger log = Logger.getLogger(WASDataConvertByHour.class);
	
	private SimpleDateFormat orignFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:00");
	
	protected String convertTime(String time) {
		try {
			return convertFormat.format(orignFormat.parse(time));
		} catch (ParseException e) {
			return null;
		}
		
	}

}
