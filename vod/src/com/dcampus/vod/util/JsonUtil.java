package com.dcampus.vod.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * json工具类，沿用之前lms系统
 * @author patrick
 *
 */

@Service
@Lazy(false)
public class JsonUtil implements ApplicationContextAware{
	
	private String result;
	private Integer code;
	private String message;
	private Boolean upRecursion = false;//向上递归，比如在course对象中有一个user对象，该值为true，则去获取user里面所有属性
	private Boolean downRecursion = false;//向下递归，比如在course对象中有一个section的list，该值为true则得到所有section
	
	public JsonUtil(Boolean upRecursion, Boolean downRecursion){
		this.upRecursion = upRecursion;
		this.downRecursion = downRecursion;
	}

	public JsonUtil(){
		
	}
	/**
	 * 将对象生成json格式数据，如果对象是bean，则调用bean2json来解析该bean
	 * @param obj
	 * @return
	 */
	public  String object2json(Object obj) {  
	    StringBuilder json = new StringBuilder();  
	    if (obj == null) {  
	      json.append("\"\"");  
	    } else if (obj instanceof String ||
	         obj instanceof Integer ||
	         obj instanceof Float  ||
	         obj instanceof Boolean ||
	         obj instanceof Short ||
	         obj instanceof Double || 
	         obj instanceof Long ||
	         obj instanceof BigDecimal ||
	         obj instanceof BigInteger || 
	         obj instanceof Byte) {  
	      json.append("\"").append(string2json(obj.toString())).append("\"");  
	    } else if(obj instanceof Date){
	    	json.append("\"").append(((Date) obj).toString()).append("\"");	
	    }else if (obj instanceof Object[]) {  
	      json.append(array2json((Object[]) obj));  
	    } else if (obj instanceof List<?>) {  
	      json.append(list2json((List<?>) obj));  
	    } else if (obj instanceof Map<?,?>) {  
	      json.append(map2json((Map<?, ?>) obj));  
	    } else if (obj instanceof Set<?>) {  
	      json.append(set2json((Set<?>) obj));  
	    } else {  
	      if(upRecursion)
	    	  json.append(bean2json(obj));  
	      else
	    	  json.append("\"\"");
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String bean2json(Object bean) {  
	    StringBuilder json = new StringBuilder();  
	    json.append("{");  
	    PropertyDescriptor[] props = null;  
	    try {  
	      props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();  
	    } catch (IntrospectionException e) {}  
	    if (props != null) {  
	      for (int i = 0; i < props.length; i++) {  
	        try {  
	        
	          if(!downRecursion && props[i].getPropertyType().isInterface()){//如果已经实现了接口，比如Set、List、Map等
	        	  continue;
	          }
	          String name = object2json(props[i].getName());  
	          String value = object2json(props[i].getReadMethod().invoke(bean));  
	          json.append(name);  
	          json.append(":");  
	          json.append(value);  
	          json.append(",");  
	        } catch (Exception e) {}  
	      }  
	      json.setCharAt(json.length() - 1, '}');  
	    } else {  
	      json.append("}");  
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String list2json(List<?> list) {  
	    StringBuilder json = new StringBuilder();  
	    json.append("[");  
	    if (list != null && list.size() > 0) {  
	      for (Object obj : list) {  
	        json.append(bean2json(obj));  
	        json.append(",");  
	      }  
	      json.setCharAt(json.length() - 1, ']');  
	    } else {  
	      json.append("]");  
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String array2json(Object[] array) {  
	    StringBuilder json = new StringBuilder();  
	    json.append("[");  
	    if (array != null && array.length > 0) {  
	      for (Object obj : array) {  
	        json.append(bean2json(obj));  
	        json.append(",");  
	      }  
	      json.setCharAt(json.length() - 1, ']');  
	    } else {  
	      json.append("]");  
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String map2json(Map<?, ?> map) {  
	    StringBuilder json = new StringBuilder();  
	    json.append("{");  
	    if (map != null && map.size() > 0) {  
	      for (Object key : map.keySet()) {  
	        json.append(object2json(key));  
	        json.append(":");  
	        //if(map.get(key).getClass().isInterface())
	        json.append(list2json((List<?>)map.get(key)));  
	       // else
	       // 	json.append(object2json(map.get(key)));
	        json.append(",");  
	      }  
	      json.setCharAt(json.length() - 1, '}');  
	    } else {  
	      json.append("}");  
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String set2json(Set<?> set) {  
	    StringBuilder json = new StringBuilder();  
	    json.append("[");  
	    if (set != null && set.size() > 0) {  
	      for (Object obj : set) {  
	        json.append(bean2json(obj));  
	        json.append(",");  
	      }  
	      json.setCharAt(json.length() - 1, ']');  
	    } else {  
	      json.append("]");  
	    }  
	    return json.toString();  
	  }  
	 
	   
	  public  String string2json(String s) {  
	    if (s == null)  
	      return "";  
	    StringBuilder sb = new StringBuilder();  
	    for (int i = 0; i < s.length(); i++) {  
	      char ch = s.charAt(i);  
	      switch (ch) {  
	      case '"':  
	        sb.append("\\\"");  
	        break;  
	      case '\\':  
	        sb.append("\\\\");  
	        break;  
	      case '\b':  
	        sb.append("\\b");  
	        break;  
	      case '\f':  
	        sb.append("\\f");  
	        break;  
	      case '\n':  
	        sb.append("\\n");  
	        break;  
	      case '\r':  
	        sb.append("\\r");  
	        break;  
	      case '\t':  
	        sb.append("\\t");  
	        break;  
	      case '/':  
	        sb.append("\\/");  
	        break;  
	      default:  
	        if (ch >= '\u0000' && ch <= '\u001F') {  
	          String ss = Integer.toHexString(ch);  
	          sb.append("\\u");  
	          for (int k = 0; k < 4 - ss.length(); k++) {  
	            sb.append('0');  
	          }  
	          sb.append(ss.toUpperCase());  
	        } else {  
	          sb.append(ch);  
	        }  
	      }  
	    }  
	    return sb.toString();  
	  }



	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		 
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	} 
}  