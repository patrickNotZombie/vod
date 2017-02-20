package com.dcampus.vod.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dcampus.common.persistence.BaseEntity;

/**
 * 资源信息，只有对应的weblibId，名字等基本信息
 * @author patrick
 *
 */
@Entity
@Table(name = "vod_resource")
public class Resource extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	/**
	 * 该资源关联的资源库(weblib)中资源id
	 * 如果为0，则表示该资源还未与资源库中资源关联，需要向资源库请求资源
	 */
	@Column(name = "weblib_id")
	private Long weblibId = 0L;
	
	/**资源名字 */
	@Column(name = "name")
	private String name;
	
	/**存储路径*/
	@Column(name = "filepath")
	private String filepath;
	
	/** 资源大小，以K为单位 **/
	@Column(name = "size")
	private Long size;

	/** 资源创建时间 **/
	@Column(name = "creation_date")
	private Timestamp creationDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getWeblibId() {
		return weblibId;
	}

	public void setWeblibId(Long weblibId) {
		this.weblibId = weblibId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	
	

}
