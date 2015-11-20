package test.org.pinae.pumbaa.analysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.pumbaa.analysis.DataConvert;

public class WASDataConvertByMinute implements DataConvert {

	private Logger log = Logger.getLogger(WASDataConvertByMinute.class);

	private SimpleDateFormat orignFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public String[] convert(String data) {

		String retData[] = data.split("\\|");

		if (retData.length == 13) {
			for (int i = 0; i < retData.length; i++) {
				String item = retData[i];

				int pos = -1;
				pos = StringUtils.indexOf(item, ":");
				if (pos == -1) {
					pos = StringUtils.indexOf(item, "：");
				}
				if (pos != -1) {
					String columnValue = StringUtils.substring(item, pos + 1);
					if (StringUtils.isEmpty(columnValue)) {
						columnValue = "null";
					}
					retData[i] = columnValue;
				} else {
					retData[i] = "null";
				}
			}

			retData[0] = convertTime(retData[0]);
			if (StringUtils.endsWith(retData[4], "ms")) {
				retData[4] = StringUtils.substringBefore(retData[4], "ms");
			}
			return retData;
		} else {
			log.debug(data);
		}
		return null;

	}

	protected String convertTime(String time) {
		try {
			return convertFormat.format(orignFormat.parse(time));
		} catch (ParseException e) {
			return null;
		}

	}

}
