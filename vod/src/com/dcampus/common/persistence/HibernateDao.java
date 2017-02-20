package com.dcampus.common.persistence;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.dcampus.common.util.StringUtils;

public class HibernateDao {

	/**
	 * 获取实体工厂管理对象
	 */
	@PersistenceContext
	protected EntityManager entityManager;
	
	/**
	 * 获取 SESSION
	 */
	public Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	/**
	 * 清除緩存
	 */
	public void clear(){  
		getSession().clear();
	}

	public <T extends BaseEntity> T get(Class<T> clz, Object id) {
		return entityManager.find(clz, id);
	}
	
	//----------------------------------------------------------------
	/**
	 * T save
	 */
	public <T extends BaseEntity> void save(T t) {
		entityManager.persist(t);
	}
	/**
	 * T update
	 */
	public <T extends BaseEntity> void update(T t) {
		entityManager.merge(t);
	}
	/**
	 * T delete
	 */
	public <T extends BaseEntity> void delete(T t) {
		entityManager.remove(t);
	}
	/**
	 * QL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int update(String qlString, Object... parameter){
		return createQuery(qlString, parameter).executeUpdate();
	}
	
	//-------------------------------------------------------------------
	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public <T> Page<T> findPage(Page<T> page, String qlString, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
	        page.setCount((Long)createQuery(countQlString, parameter).getSingleResult());
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults());
        }
        page.setList(query.getResultList());
		return page;
    }
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String qlString, Object... parameter){
		return createQuery(qlString, parameter).getResultList();
	}

    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(int beginIndex, int maxResult, String qlString, Object... parameter){
		Query query = createQuery(qlString, parameter);
        query.setFirstResult(beginIndex);
        query.setMaxResults(maxResult);
		return query.getResultList();
	}
	
	/**
	 * QL 获取第一条符合条件的记录
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findFirst(String qlString, Object... parameter) {
		Query query = createQuery(qlString, parameter); 
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<T> list = query.getResultList();
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter){
		Query query = entityManager.createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- Query Tools --------------
	/**
	 * 设置查询参数
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Object... parameter){
		if (parameter != null){
			for (int i=0; i<parameter.length; i++){
				query.setParameter(i+1, parameter[i]);
			}
		}
	}
	
    /** 
     * 去除qlString的select子句。 
     * @param hql 
     * @return 
     */  
    private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
      
    /** 
     * 去除hql的orderBy子句。 
     * @param hql 
     * @return 
     */  
    private String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    }
    
    
    // -------------- Hibernate search --------------
	/**
	 * 获取全文Session
	
	public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	 */
	/**
	 * 建立索引
	
	public void createIndex(){
		try {
			getFullTextSession().createIndexer(Article.class).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		
		// 按关键字查询
		FullTextQuery fullTextQuery = null;
		if(clauses == null)
			fullTextQuery = getFullTextSession().createFullTextQuery(query, entityClass);
		else
			fullTextQuery = getFullTextSession().createFullTextQuery(query, clauses);
        
		// 过滤无效的内容
		fullTextQuery.setFilter(new CachingWrapperFilter(new QueryWrapperFilter(queryFilter)));
        
        // 按时间排序
		fullTextQuery.setSort(sort);

		// 定义分页
		page.setCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirstResult());
		fullTextQuery.setMaxResults(page.getMaxResults()); 

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY); 
        
		// 返回结果
		page.setList(fullTextQuery.list());
        
		return page;
	}
	 */	
	/**
	 * 获取全文查询对象
	 
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}*/

	/**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 
	public BooleanQuery getFullTextQuery(String q, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (StringUtils.isNotBlank(q)){
				for (String field : fields){
					QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);   
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}
	
	/**
	 * 设置关键字高亮
	 * @param query 查询对象
	 * @param list 设置高亮的内容列表
	 * @param fields 字段名
	
	public <T extends BaseEntity> List<T> keywordsHighlight(BooleanQuery query, List<T> list, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");   
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query)); 
		highlighter.setTextFragmenter(new SimpleFragmenter(100)); 
		for(T entity : list){ 
			try {
				for (String field : fields){
					String text = StringUtils.replaceHtml((String)Reflections.invokeGetter(entity, field));
					String desciption = highlighter.getBestFragment(analyzer,field, text);
					if(desciption!=null){
						Reflections.invokeSetter(entity, fields[0], desciption);
						break;
					}
					Reflections.invokeSetter(entity, fields[0], StringUtils.abbreviate(text, 100));
				}
				//Reflections.invokeSetter(entity, fields[1], "sdfkjsdlkfjklsdjf");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}*/
}
