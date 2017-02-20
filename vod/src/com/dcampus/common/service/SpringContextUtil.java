package com.dcampus.common.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 提供获取applicationContext bean的静态方法
 */
@Service
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

	public static ApplicationContext context;
	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static Object getContext() {
		return context;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
		context = applicationcontext;
	}
}
