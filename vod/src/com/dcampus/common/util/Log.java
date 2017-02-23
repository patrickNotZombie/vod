package com.dcampus.common.util;

import org.apache.commons.logging.LogFactory;

/**
 * 对apache-log的一层代理类<br>
 * 进行该代理的目的是为了控制打印内容的多少
 *
 * @author zim
 *
 */
public class Log {
	public static Log getLog(Class<?> clazz) {
		return new Log(clazz);
	}

	private org.apache.commons.logging.Log log;

	private Log(Class<?> clazz) {
		log = LogFactory.getLog(clazz);
	}

	public void debug(Object arg0, Throwable arg1) {
		log.debug(arg0, arg1);
	}

	public void debug(Object arg0) {
		log.debug(arg0);
	}

	public void error(Object arg0, Throwable arg1) {
		// if (isTraceEnabled())
		log.error(arg0, arg1);
		// else
		// log.error(arg0);
	}

	public void error(Object arg0) {
		log.error(arg0);
	}

	public void fatal(Object arg0, Throwable arg1) {
		if (isTraceEnabled())
			log.fatal(arg0, arg1);
		else
			log.fatal(arg0);
	}

	public void fatal(Object arg0) {
		log.fatal(arg0);
	}

	public void info(Object arg0, Throwable arg1) {
		if (log.isTraceEnabled())
			log.info(arg0, arg1);
		else
			log.info(arg0);
	}

	public void info(Object arg0) {
		log.info(arg0);
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}

	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	public void trace(Object arg0, Throwable arg1) {
		log.trace(arg0, arg1);
	}

	public void trace(Object arg0) {
		log.trace(arg0);
	}

	public void warn(Object arg0, Throwable arg1) {
		// if (isTraceEnabled())
		log.warn(arg0, arg1);
		// else
		// log.warn(arg0);
	}

	public void warn(Object arg0) {
		log.warn(arg0);
	}

}
