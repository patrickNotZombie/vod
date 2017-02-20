package com.dcampus.common.generic;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcampus.common.persistence.BaseEntity;
import com.dcampus.common.persistence.Page;
import com.dcampus.common.service.BaseService;

/**
 * 通用GenericService
 */
@Service
@Transactional(readOnly=true)
public class GenericService extends BaseService {

	@Autowired
	private GenericDao genericDao;
	

	public void flush() {
		genericDao.getSession().flush();
	}
	
	public <T extends BaseEntity> T get(Class<T> clz, Object id) {
		return genericDao.get(clz, id);
	}
	
	
	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <T> Page<T> findPage(Page<T> page, String qlString, Object... parameter){
		return genericDao.findPage(page, qlString, parameter);
    }
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <T> List<T> findAll(String qlString, Object... parameter){
		return genericDao.findAll(qlString, parameter);
	}
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(int beginIndex, int maxResult, String qlString, Object... parameter){
		return genericDao.findAll(beginIndex, maxResult, qlString, parameter);
	}
	/**
	 * QL 获取第一条符合条件的记录
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <T> T findFirst(String qlString, Object... parameter) {
		return (T) genericDao.findFirst(qlString, parameter);
	}
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter){
		return genericDao.createQuery(qlString, parameter);
	}
	
    // -------------- Hibernate search --------------
	/**
	 * 获取全文Session
	
	public FullTextSession getFullTextSession(){
		return genericDao.getFullTextSession();
	}
	 */
	/**
	 * 建立索引
	
	public void createIndex(){
		genericDao.createIndex();
	}
	 */
	/**
	 * 全文检索
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	
	@SuppressWarnings("unchecked")
	public Page search(Page page, BooleanQuery query, BooleanQuery queryFilter, Sort sort, Class<?> ... clauses){
		return genericDao.search(page, query, queryFilter, sort, clauses);
	} */	
	
	/**
	 * 获取全文查询对象
	 
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		return genericDao.getFullTextQuery(booleanClauses);
	}*/

	/**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 
	public BooleanQuery getFullTextQuery(String q, String... fields){
		return genericDao.getFullTextQuery(q, fields);
	}
	
	/**
	 * 设置关键字高亮
	 * @param query 查询对象
	 * @param list 设置高亮的内容列表
	 * @param fields 字段名
	
	public <T extends BaseEntity> List<T> keywordsHighlight(BooleanQuery query, List<T> list, String... fields){
		return 
		return genericDao.keywordsHighlight(query, list, fields);
	}*/
}
