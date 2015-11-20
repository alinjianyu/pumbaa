package org.pinae.pumbaa.data.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * ZIP文件处理
 * 
 * @author Huiyugeng
 *
 */
public class ZipFile {
	private static Logger log = Logger.getLogger(ZipFile.class);

	/**
	 * 解压文件
	 * 
	 * @param zipFile zip文件
	 * @param outDir 解压目录
	 * 
	 * @throws IOException 异常处理
	 */
	public static List<String> unzip(String zipFilename, String outDir) throws IOException {

		if (StringUtils.isEmpty(zipFilename)) {
			return null;
		}
		
		File dir = new File(outDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		List<String> unzipFileList = new ArrayList<String>();
		
		File zipFile = new File(zipFilename);
		
		ArchiveInputStream zais = null;
		FileInputStream fileis =  new FileInputStream(zipFile);
		try {

			zais = new ArchiveStreamFactory().createArchiveInputStream("zip", fileis);

			ZipArchiveEntry entry = null;
			while ((entry = (ZipArchiveEntry) zais.getNextEntry()) != null) {

				File unzipFile = new File(outDir, entry.getName());

				unzipFile.createNewFile();
				OutputStream out = new FileOutputStream(unzipFile);
				try {
					IOUtils.copy(zais, out);
				} finally {
					IOUtils.closeQuietly(out);
				}

				unzipFileList.add(unzipFile.getAbsolutePath());
			}
			
			IOUtils.closeQuietly(zais);
		} catch (ArchiveException e) {
			log.error(String.format("unzip Exception: exception=%s", e.getMessage()));
		}

		return unzipFileList;

	}

	/**
	 * 压缩文件
	 * 
	 * @param zipFileList 需要压缩的文件列表
	 * @param zipFilename 压缩后的文件名
	 * 
	 * @throws IOException 异常处理
	 * 
	 */
	public static void zip(Collection<String> zipFileList, String zipFilename) throws IOException {
		if (zipFileList != null && zipFileList.size() > 0) {
			ZipArchiveOutputStream zaos = null;
			try {
				File zipFile = new File(zipFilename);
				zaos = new ZipArchiveOutputStream(zipFile);

				zaos.setUseZip64(Zip64Mode.AsNeeded);

				// 将每个文件用ZipArchiveEntry封装
				// 再用ZipArchiveOutputStream写到压缩文件中
				for (String filename : zipFileList) {
					File file = new File(filename);
					
					if (file != null) {
						ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
						zaos.putArchiveEntry(zipArchiveEntry);
						InputStream is = null;
						try {
							is = new FileInputStream(file);
							byte[] buffer = new byte[1024 * 5];
							int len = -1;
							while ((len = is.read(buffer)) != -1) {
								// 把缓冲区的字节写入到ZipArchiveEntry
								zaos.write(buffer, 0, len);
							}
							zaos.closeArchiveEntry();
						} finally {
							IOUtils.closeQuietly(is);
						}

					}
				}
				
				zaos.finish();
			} finally {
				IOUtils.closeQuietly(zaos);
			}

		}
	}
}
