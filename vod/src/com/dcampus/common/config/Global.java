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
	 * 超级管理员路径
	 */
	public static final String ADMIN_PATH = "/admin";
	
	/**
	 * 设置内部科研人员中心访问路径
	 * 1. 修改本类 MEMBER_PATH 常量
	 * 2. 修改 applicationContext-shiro.xml 中的 shiroFilter
	 * 3. 修改 decorators.xml 中的 default
	 * 4. 修改 spring-mvc.xml 中的 mvc:view-controller
	 */
	public static final String MEMBER1_PATH = "/member1";

	/**
	 * 设置外部科研人员中心访问路径
	 * 1. 修改本类 MEMBER_PATH 常量
	 * 2. 修改 applicationContext-shiro.xml 中的 shiroFilter
	 * 3. 修改 decorators.xml 中的 default
	 * 4. 修改 spring-mvc.xml 中的 mvc:view-controller
	 */
	public static final String MEMBER2_PATH = "/member2";

	/**
	 * 本用户访问路径
	 * 1. 修改本类 PRINCIPAL_PATH 常量
	 * 2. 修改 applicationContext-shiro.xml 中的 shiroFilter
	 * 3. 修改 decorators.xml 中的 default
	 * 4. 修改 spring-mvc.xml 中的 mvc:view-controller
	 */
	public static final String PRINCIPAL_PATH = "/principal";
	
	/**
	 * 设置访问URL后缀
	 */
	public static final String URL_SUFFIX = ".html";
	
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
	
	public static String getAdminPath() {
		return ADMIN_PATH;
	}
	public static String getMember1Path() {
		return MEMBER1_PATH;
	}
	public static String getMember2Path() {
		return MEMBER2_PATH;
	}
	public static String getPrincipalPath() {
		return PRINCIPAL_PATH;
	}

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
}
