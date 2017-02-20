package com.dcampus.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	/**
	 * 替换掉HTML标签
	 */
	public static String replaceHtml(String html){ 
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(html); 
        String s = m.replaceAll(""); 
        return s; 
    }
	
}
