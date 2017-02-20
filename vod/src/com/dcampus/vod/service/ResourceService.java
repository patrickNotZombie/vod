package com.dcampus.vod.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcampus.common.generic.GenericDao;
import com.dcampus.common.service.BaseService;
import com.dcampus.vod.entity.Resource;

/**
 * 资源类service
 * 简单的增删查改
 * 
 * @author patrick
 *
 */

@Service
@Transactional(readOnly = false)
public class ResourceService extends BaseService{
	
	@Autowired
	private GenericDao genericDao;
	
	/**
	 * description:查找资源
	 * @param id 根据资源id查找资源
	 * @return resource 返回此ID对应的资源
	 */
	public Resource getResourceById(Integer id) {
		return genericDao.get(Resource.class, id);
	}
	
	/**
	 * description:查找资源
	 * @param id 根据资源weblibId查找资源
	 * @return resource 返回此weblibId对应的资源
	 */
	public Resource getResourceByWeblibId(Long weblibId) {
		return genericDao.get(Resource.class, weblibId);
	}
	
	
	/**
	 * description: 保存或者更新资源信息
	 * @param resource 资源相关信息
	 * @return  
	 */
	public void saveOrUpdateResource(Resource resource) {
		if(resource.getId() != null && resource.getId() > 0)
			genericDao.update(resource);
		else
			genericDao.save(resource);
	}
	
	/**
	 * description:删除资源
	 * @param resource 资源相关信息
	 * @return
	 */
	public void deleteResource(Resource resource) {
		Resource r = genericDao.get(Resource.class, resource.getId());
		genericDao.delete(r);
	}
	
	/**
	 * description: 批量删除资源
	 * @param resources 资源相关信息
	 * @return
	 */
	public void deleteResources(Collection<Resource> resources) {
		for(Resource item : resources) {
			deleteResource(item);
		}
	}
	

}
