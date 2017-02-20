package com.dcampus.common.web;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面参数获取
 * @author scut
 *
 */
public class RequestParamHelper implements Serializable {

	private static final long serialVersionUID = 1204800083477572826L;

	public static Date getDate(HttpServletRequest request, String param, String datePattern) {
		String p = request.getParameter(param);
		if (p == null || p.trim().length() == 0) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(datePattern);
		Date d = null;
		try {
			d = df.parse(p.trim());
		} catch (ParseException e) {
		}
		return d;
	}
	
	public static boolean getCheckboxBoolean(HttpServletRequest request, String param) {
		String p = request.getParameter(param);
		if (p != null && p.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Long[] getLongs(HttpServletRequest request, String param) {
		String[] ps = request.getParameterValues(param);
		if (ps == null || ps.length == 0) {
			return new Long[0];
		} else {
			Long[] result = new Long[ps.length];
			for (int i = 0; i < ps.length; i++) {
				result[i] = Long.parseLong(ps[i].trim());
			}
			return result;
		}
	}
	
	public static Integer getInteger(Map<String, String> parameterMap, String param, Integer defaultValue) {
		if (parameterMap.get(param) == null) {
			return defaultValue;
		}
		return Integer.valueOf(parameterMap.get(param));
	}

	public static Integer[] getIntegers(HttpServletRequest request, String param) {
		String[] ps = request.getParameterValues(param);
		if (ps == null || ps.length == 0) {
			return new Integer[0];
		} else {
			Integer[] result = new Integer[ps.length];
			for (int i = 0; i < ps.length; i++) {
				result[i] = Integer.parseInt(ps[i].trim());
			}
			return result;
		}
	}
	
}
