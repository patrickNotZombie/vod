/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dcampus.common.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;

/**
 * Entity支持类
 * @author ThinkGem
 * @version 2013-01-15
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	public static final long serialVersionUID = 1L;

	/** 记录状态：启用 */
	public static final int Status_enabled = 1;
	/** 记录状态：停用 */
	public static final int Status_disabled = 2;
	/** 记录状态：删除 */
	public static final int Status_deleted = 9;

	/** 审核状态：未审核 */
	public static final int AuditStatus_no = 0;
	/** 审核状态：通过 */
	public static final int AuditStatus_pass = 1;
	/** 审核状态：不通过 */
	public static final int AuditStatus_reject = 2;
	/** 审核状态：退回修改 */
	public static final int AuditStatus_back = 3;

	/** 使用状态 */
	private Integer status = Status_enabled;
	/** 最近更新时间 */
	private Date lastModified;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public int getStatusEnabled() {
		return Status_enabled;
	}
	public int getStatusDisabled() {
		return Status_disabled;
	}
	
	public static int getAuditstatusNo() {
		return AuditStatus_no;
	}
	public static int getAuditstatusPass() {
		return AuditStatus_pass;
	}
	public static int getAuditstatusReject() {
		return AuditStatus_reject;
	}
	public static int getAuditstatusBack() {
		return AuditStatus_back;
	}
	
	public static Map<Integer, String> getAuditStatusMap() {
		Map<Integer, String> resultMap = new LinkedHashMap<Integer, String>();
		resultMap.put(AuditStatus_no, "未审核");
		resultMap.put(AuditStatus_pass, "通过");
		resultMap.put(AuditStatus_reject, "不通过");
		resultMap.put(AuditStatus_back, "退回修改");
		return resultMap;
	}
	
	public static Map<Integer, String> getAuditStatusHtmlMap() {
		Map<Integer, String> resultMap = new LinkedHashMap<Integer, String>();
		resultMap.put(AuditStatus_no, "<span style=\"color:blue\">未审核</span>");
		resultMap.put(AuditStatus_pass, "通过");
		resultMap.put(AuditStatus_reject, "<span style=\"color:red\">不通过</span>");
		resultMap.put(AuditStatus_back, "<span style=\"color:red\">退回修改</span>");
		return resultMap;
	}
}
