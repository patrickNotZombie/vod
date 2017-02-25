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
		System.out.println("该文件完整路径为：" + buffer.toString());
		return buffer.toString();
	}
	
	
	



}
