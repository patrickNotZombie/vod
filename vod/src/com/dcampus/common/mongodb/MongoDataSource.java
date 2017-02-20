package com.dcampus.common.mongodb;

import java.io.Serializable;

/**
 * MongoDB 数据源配置
 */
public class MongoDataSource implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主机信息 */
	private String host;
	/** 端口信息 */
	private int port;
	/** 数据库名称 */
	private String dbName;
	
	public MongoDataSource() {
	}
	public MongoDataSource(String host, int port, String dbName) {
		this.host = host;
		this.port = port;
		this.dbName = dbName;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
