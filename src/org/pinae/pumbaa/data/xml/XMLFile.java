package org.pinae.pumbaa.data.xml;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.pinae.nala.xb.marshal.Marshaller;
import org.pinae.nala.xb.marshal.XMLMarshaller;
import org.pinae.nala.xb.unmarshal.Unmarshaller;
import org.pinae.nala.xb.unmarshal.XMLUnmarshaller;
import org.pinae.nala.xb.util.ResourceReader;
import org.pinae.nala.xb.util.ResourceWriter;


/**
 * XML文件工具
 * 
 * @author Huiyugeng
 *
 */
public class XMLFile {
	private static Logger log = Logger.getLogger(XMLFile.class);
	
	/**
	 * 读取XML文件
	 * 
	 * @param filename 需要读取的XML文件
	 * 
	 * @return XML对象
	 */
	@SuppressWarnings("rawtypes")
	public Map<?, ?> read(String filename) {
		Map<?, ?> xmlObject = new HashMap();
		try {
			Unmarshaller bind = new XMLUnmarshaller(new ResourceReader().getFileStream(filename));
			bind.setRootClass(Map.class);
			xmlObject = (Map<?, ?>)bind.unmarshal();
		} catch (Exception e) {
			log.error(String.format("Read XML File Exception: filename=%s, exception=%s", filename, e.getMessage()));
		}
		return xmlObject;
	}
	
	/**
	 * 将对象写入XML文件
	 * 
	 * @param filename 需要写入的XML文件
	 * @param xmlObject XML对象
	 * @param encoding XML编码
	 */
	public void write(String filename, Object xmlObject, String encoding) {
		try {
			new ResourceWriter().writeToFile(marshal(xmlObject, encoding), filename);
		} catch (Exception e) {
			log.error(String.format("Write XML File Exception: filename=%s, exception=%s", filename, e.getMessage()));			
		}
	}
	
	/**
	 * 将对象生成XML字符串
	 * 
	 * @param xmlObject XML对象
	 * @param encoding XML编码
	 */
	public StringBuffer marshal(Object xmlObject, String encoding) {
		StringBuffer xml = new StringBuffer();
		try {
			Marshaller marshaller = new XMLMarshaller(xmlObject);
			marshaller.setDocumentStart(String.format("<?xml version='1.0' encoding='%s'?>", encoding));
			marshaller.enablePrettyPrint(true);
			marshaller.enableNodeMode(true);
			marshaller.enableLowCase(true);
			
			xml = marshaller.marshal();
		} catch (Exception e) {
			log.error(String.format("Marshal Exception: exception=%s", e.getMessage()));
		}
		return xml;
	}
	
	/**
	 * 将XML文本转换为对象
	 * 
	 * @param xml XML文本
	 * @param encoding XML文本编码
	 * @param clazz 需要生成对象的类
	 * 
	 * @return XML对象
	 */
	@SuppressWarnings("rawtypes")
	public Object unmarshal(String xml, String encoding, Class clazz) {
		Object xmlObject = null;
		try {
			Unmarshaller bind = new XMLUnmarshaller(new ByteArrayInputStream(xml.getBytes(encoding)));
			bind.setRootClass(clazz);
			xmlObject = (Map<?, ?>)bind.unmarshal();
		} catch (Exception e) {
			log.error(String.format("XML Unmarshal Exception: exception=%s", e.getMessage()));
		}
		return xmlObject;
	}
}
