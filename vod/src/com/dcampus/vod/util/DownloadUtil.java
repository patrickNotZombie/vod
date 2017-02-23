package com.dcampus.vod.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.dcampus.common.config.Global;
import com.dcampus.common.util.Crypt;
import com.dcampus.common.util.Log;
import com.dcampus.vod.entity.Resource;

public class DownloadUtil {
	private static Log log = Log.getLog(DownloadUtil.class);
//	/**
//	 * 下载文件
//	 *
//	 * @param uriPath
//	 * @param outputStream
//	 * @throws Exception
//	 */
//	public static void downloadFile(String uriPath, OutputStream outputStream)
//			throws IOException {
//		FileInputStream inputStream = null;
//		try {
//			File targetFile = new File(new URI(uriPath));
//
//			inputStream = new FileInputStream(targetFile);
//			//Crypt.fileDecrypt(inputStream, outputStream);
//			 IOUtils.copy(inputStream, outputStream);
//			outputStream.flush();
//		} catch (Exception e) {
//			throw new IOException(e);
//		} finally {
//			if (inputStream != null) {
//				try {
//					inputStream.close();
//				} catch (Exception e) {
//					throw new IOException(e);
//				}
//			}
//		}
//	}
	
	/**
	 * 获取文件的完整路径，包括文件本身
	 * @param resource
	 * @return
	 */
	public static String getFileFullPath(Resource resource) {
		StringBuffer buffer = new StringBuffer();
		String path = Global.getFileRootPath();
		
		String subPath = new File(path).toURI().toString();
		buffer.append(subPath);
		if (!subPath.endsWith("/"))                //toURI()之后已是正斜杠，因此此处从File.separator改为“/”
			buffer.append("/");
		String fileName = resource.getName();
		String ext = resource.getFileExt();
		buffer.append(fileName);
		buffer.append(".");
		buffer.append(ext);

		return buffer.toString();
	}
	
	
	
//	/**
//	 * 保存文件到服务器
//	 *
//	 * @param file 源文件
//	 * @param uriPath 目的文件地址
//	 * @throws Exception
//	 */
//	static void copyFileToServer(File file, String uriPath,
//			boolean ingoreEncrypt) throws IOException {
//		FileInputStream inputStream = null;
//		FileOutputStream outputStream = null;
//		try {
//			inputStream = new FileInputStream(file);
//			File targetFile = new File(new URI(uriPath));
//			outputStream = new FileOutputStream(targetFile, true);
//
//			if (ingoreEncrypt) {
//				IOUtils.copy(inputStream, outputStream);
//			} else {
//				Crypt.fileEncrypt(inputStream, outputStream);
//			}
//		} catch (Exception e) {
//			if (e instanceof FileNotFoundException)
//				throw new IOException("找不到待复制文件" );
//			else
//				throw new IOException(e);
//		} finally {
//			if (inputStream != null) {
//				try {
//					inputStream.close();
//				} catch (Exception e) {
//					log.error(e, e);
//				}
//			}
//			if (outputStream != null) {
//				try {
//					outputStream.close();
//				} catch (Exception e) {
//					log.error(e, e);
//				}
//			}
//		}
//	}
//
//	
//
//	static void writeToServer(byte[] data, String uriPath, boolean ingoreEncrypt)
//			throws IOException {
//		FileOutputStream outputStream = null;
//		try {
//			File targetFile = new File(new URI(uriPath));
//			outputStream = new FileOutputStream(targetFile, true);
//
//			if (ingoreEncrypt)
//				IOUtils.write(data, outputStream);
//			else {
//				Crypt.en(data);
//				IOUtils.write(data, outputStream);
//			}
//		} catch (Exception e) {
//			throw new IOException(e);
//		} finally {
//			if (outputStream != null) {
//				try {
//					outputStream.close();
//				} catch (Exception e) {
//					log.error(e, e);
//				}
//			}
//		}
//	}
//
//	static void moveFileToServer(File file, String uriPath,
//			boolean ingoreEncrypt) throws IOException {
//		try {
//			File targetFile = new File(new URI(uriPath));
//			FileUtils.copyFile(file, targetFile);
//			FileUtils.forceDelete(file);
//			if (!ingoreEncrypt)
//				Crypt.fileEncrypt(targetFile, targetFile);
//		} catch (URISyntaxException e) {
//			throw new IOException(e);
//		} catch (IOException e) {
//			throw new IOException(e);
//		}
//
//	}


}
