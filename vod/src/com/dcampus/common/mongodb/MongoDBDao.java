package com.dcampus.common.mongodb;

import java.net.UnknownHostException;
import java.util.List;

import com.dcampus.common.persistence.Page;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBDao {
	
	private MongoDataSource dataSource;
	
	private String collection;
	
	public MongoDBDao() {
	}
	
	public void setDataSource(MongoDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public MongoDataSource getDataSource() {
		return dataSource;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection() {
		return collection;
	}

	private DBCollection getDBCollection() throws UnknownHostException {
		return getDBCollection(collection);
	}
	
	private DBCollection getDBCollection(String collection) throws UnknownHostException {
		MongoClient mongo = new MongoClient(getDataSource().getHost(), getDataSource().getPort());
		DB db = mongo.getDB(getDataSource().getDbName());
		return db.getCollection(getCollection()); 
	}
	
	/**
	 * 插入新记录
	 * @param dataObjs
	 * @throws UnknownHostException
	 */
	public void insert(DBObject dataObj) throws UnknownHostException {
		getDBCollection().insert(dataObj);
	}

	/**
	 * 批量插入新记录
	 * @param dataObjs
	 * @throws UnknownHostException
	 */
	public void inserts(List<DBObject> dataObjs) throws UnknownHostException {
		getDBCollection().insert(dataObjs);
	}
	
	/**
	 * 更新所有符合条件的记录
	 * @param queryObj  查询条件
	 * @param dataObj   更新的值信息
	 * @throws UnknownHostException
	 */
	public void update(DBObject queryObj, DBObject dataObj) throws UnknownHostException {
		getDBCollection().updateMulti(queryObj, dataObj);
	}
	
	/**
	 * 删除符合条件的记录
	 * @param queryObjs
	 * @throws UnknownHostException
	 */
	public void remove(DBObject queryObj) throws UnknownHostException {
		getDBCollection().remove(queryObj);
	}

	/**
	 * 删除混符合条件的记录
	 * @param queryObjs
	 * @throws UnknownHostException
	 */
	public void removes(List<DBObject> queryObjs) throws UnknownHostException {
		for (DBObject qo : queryObjs) {
			remove(qo);
		}
	}
	
	/**
	 * 获取符合查询条件的key列的distinct返回值
	 * @param key
	 * @param queryObj
	 * @return
	 * @throws UnknownHostException
	 */
	public List distinct(String key, DBObject queryObj) throws UnknownHostException {
		return getDBCollection().distinct(key, queryObj);
	}
	
	/**
	 * 获取符合查询条件的记录数
	 * @param queryObj
	 * @return
	 * @throws UnknownHostException
	 */
	public long count(DBObject queryObj) throws UnknownHostException {
		return getDBCollection().count(queryObj);
	}
	
	/**
	 * 获取符合条件所有记录
	 * @param queryObj  查询条件
	 * @param fieldObj  查询列信息，null返回所有
	 * @param orderObj  排序条件
	 * @return
	 * @throws UnknownHostException
	 */
	public List<DBObject> findAll(DBObject queryObj, DBObject fieldObj, DBObject orderObj) throws UnknownHostException {
		DBCursor cur = getDBCollection().find(queryObj, fieldObj);
		if (orderObj != null) {
			cur.sort(orderObj);
		}
		return cur.toArray();
	}
	
	/**
	 * 获取符合条件的第一条记录
	 * @param queryObj  查询条件
	 * @param fieldObj  查询列信息，null返回所有
	 * @param orderObj  排序条件
	 * @return
	 * @throws UnknownHostException
	 */
	public DBObject findFirst(DBObject queryObj, DBObject fieldObj, DBObject orderObj) throws UnknownHostException {
		DBCursor cur = getDBCollection().find(queryObj, fieldObj);
		if (orderObj != null) {
			cur.sort(orderObj);
		}
		cur.skip(1);
		cur.limit(1);
		cur.batchSize(1);
		return cur.next();
	}
	
	/**
	 * 获取符合条件的分页记录
	 * @param page   分页信息
	 * @param queryObj  查询条件
	 * @param fieldObj  查询列信息，null返回所有
	 * @param orderObj  排序条件
	 * @return
	 * @throws UnknownHostException
	 */
	public Page<DBObject> findPage(Page<DBObject> page, DBObject queryObj, DBObject fieldObj, DBObject orderObj) throws UnknownHostException {
		DBCursor cur = getDBCollection().find(queryObj, fieldObj);
		if (orderObj != null) {
			cur.sort(orderObj);
		}
		cur.skip(page.getFirstResult() + 1);//DBCursor下标从1开始，Page.firstResult从0开始
		cur.limit(page.getPageSize());
		cur.batchSize(page.getPageSize());
		page.setList(cur.toArray());
		page.setCount(count(queryObj));
		return page;
	}
}
