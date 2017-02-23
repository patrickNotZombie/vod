/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dcampus.common.config;

import com.dcampus.common.util.NumberUtils;
import com.dcampus.common.util.PropertiesLoader;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2013-03-23
 */
public class Global {
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader;
	
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("Global.properties");
		}
		return propertiesLoader.getProperty(key);
	}

	/////////////////////////////////////////////////////////
	
	public static String getCompanyName() {
		return getConfig("companyName");
	}
	public static String getCompanyEnName() {
		return getConfig("companyEnName");
	}
	public static String getCopyrightYear() {
		return getConfig("copyrightYear");
	}
	
	
	public static String getFileRootPath() {
		return getConfig("FileRootPath");
	}
	public static String getFileTempPath() {
		return getConfig("FileRootPath") + "/temp";
	}
	
	public static String getAllowedExtensionsMedia(){
		return getConfig("AllowedExtensionsMedia");
	}

	public static Long getAttachSize(String key) {
		return Long.parseLong(getConfig(key));
	}
	public static Long getLabTableAttachApply() {
		return Long.parseLong(getConfig("LabTable_Attach_Apply"));
	}
	public static String getLabTableAttachApplyString() {
		Long fileSize = getLabTableAttachApply();
		if (fileSize >= (1024 * 1024 * 1024)) {
			return NumberUtils.getFormatNumber(new Double(fileSize) / (1024 * 1024 * 1024), "0.#") + "GB";
		} else if (fileSize >= (1024 * 1024)) {
			return NumberUtils.getFormatNumber(new Double(fileSize) / (1024 * 1024), "0.#") + "MB";
		} else if (fileSize >= (1024)) {
			return NumberUtils.getFormatNumber(new Double(fileSize) / (1024),  "0.#") + "KB";
		} else {
			return fileSize + "B";
		}
	}
	
	//获取weblib地址，用户名，密码，weblib groupId
	public static String getWeblibUrl(){
		return getConfig("webliburl");
	}
	
	public static String getWeblibLoginUrl() {
		return getConfig("weblibLoginUrl");
	}
	
	public static String getWeblibSelectUrl() {
		return getConfig("selectidActionUrl");
	}
	
	public static String getWeblibDownloadUrl() {
		return getConfig("weblibDownloadUrl");
	}
	public static String getWelbibUsername(){
		return getConfig("weblibUsername");
	}
	public static String getWelbibPassword(){
		return getConfig("weblibPasswd");
	}
	public static Integer getWeblibGroupId(){
		return Integer.parseInt(getConfig("weblibGroupId"));
	} 
	
	/**
	 * 获取lms在weblib中的柜子Id
	 */
	public static Long getLmsGroupId(){
		return Long.parseLong(getConfig("LmsGroupId").trim());
	}
	
}
