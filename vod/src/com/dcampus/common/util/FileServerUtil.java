package com.dcampus.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dcampus.vod.util.FileEncoder;

/**
 * 
 * 提供断点续传下载的工具类
 * 
 * @author dqyang
 *
 */
public class FileServerUtil {

	/**
	 * 解析 http 请求头中的参数，将文件写出到 http 输出流
	 * @param file 写出到http的文件
	 * @param filename 文件名
	 * @param request http请求
	 * @param response http响应
	 * @param isInline 
	 * @throws IOException
	 */
	public static void output(File file, String filename, HttpServletRequest request,
			HttpServletResponse response, int isInline) throws IOException{
		
		if(!file.isFile())
			throw new FileNotFoundException();
		
		long contentLength = file.length();
		long byteStart = 0, byteEnd = contentLength;

		if (request.getHeader("Range") != null) {
			String rangeFull = request.getHeader("Range");
			String[] range = rangeFull.split("=");

			if (range.length == 2) {
				rangeFull = range[1].trim();
				range = rangeFull.split("-");
				if (range.length > 0) {
					long rangeByteStart = -1L;
					try {
						rangeByteStart = Integer.decode(range[0]).intValue();
					} catch (NumberFormatException ex) {
						
					}
					if ((rangeByteStart >= 0L)
							&& (rangeByteStart < contentLength)) {
						byteStart = rangeByteStart;
					}
					
					if (range.length > 1) {
						long rangeByteEnd = -1L;
						try {
							rangeByteEnd = Integer.decode(range[1]).intValue();
						} catch (NumberFormatException ex) {
							
						}
						if ((rangeByteEnd >= 0L) && (rangeByteEnd < contentLength)) {
							byteEnd = rangeByteEnd + 1;
						}
					}
				}
			}
		}

		long total = byteEnd - byteStart;
		
		response.reset();
		response.setHeader("Content-Length", "" + total);
		response.addHeader("Accept-Ranges", "bytes");
		response.addHeader("Last-Modified", toRfc1123(file.lastModified()));

		if (total == contentLength) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.addHeader("Content-Range", "bytes " + byteStart + "-" + (byteEnd - 1)
					+ "/" + contentLength);

			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
		}

		response.setContentType("application/octet-stream");
//		if(filename != null){
//			response.setHeader("Content-Disposition",
//                "attachment; filename=" + new String(filename.getBytes("gbk"), "iso-8859-1"));
//		}
		if(filename != null){
			String toFileName = filename;
			try {
				toFileName = FileEncoder.encode(filename, request.getHeader("User-agent"));
			} catch (Exception e) {
				toFileName=" filename=" + new String(filename.getBytes("gbk"), "iso-8859-1");
			}
			if (isInline == 1) {
				response.setHeader("Content-Disposition",
		                "inline; " + toFileName);
			} else {
				response.setHeader("Content-Disposition",
		                "attachment; " + toFileName);
			}
			
		}
		/*String contentType = file.getContentType();
		if (contentType != null) {
			response.setContentType(contentType);
		}*/

		OutputStream out = response.getOutputStream();
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		in.skip(byteStart);
		byte[] buffer = new byte[8192];
		int read = -1;
		try{
			while((read = in.read(buffer)) > 0){
				if(total < read){
					out.write(buffer, 0, (int)total);
					break;
				}else{
					out.write(buffer, 0, read);
					total -= read;
				}
			}			
		}catch(Exception e){
			Throwable te = e.getCause();
			if(!(te instanceof SocketException))
				if(e instanceof IOException)
					throw (IOException)e;
				else
					throw new IOException(e.getLocalizedMessage());
		}finally{
			try{
				in.close();
			}catch(Exception fe){
				//IGNORE
			}
			try{
	            out.flush();
	            out.close();
			}catch(Exception fe){
				//IGNORE
			}
		}
	}
	
	/**
	 * 将毫秒值格式化为 rfc1123定义的日期格式
	 * @param modified
	 * @return
	 */
	private static String toRfc1123(long modified) {
		String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT,
				Locale.ENGLISH);
		return sdf.format(Long.valueOf(modified));
	}
}
