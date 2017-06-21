package com.shzy.bjj.tools;

import net.arnx.jsonic.JSON;

/**
 * 
 * @brief JSON解析工具类
 * @author Fanhao.Yi
 * @date 2015年5月11日下午4:53:20
 * @version V1.0
 */
public class JSONTool {

	/**
	 * JSON 解析
	 * 
	 * @param JSONContent
	 *            需要解析的JSON字符串
	 * @param cls
	 *            JOSN解析的对象类
	 * @return JSON解析完成后的对象
	 */
	public static <T> String Decode(String JSONContent, Class<? extends T> cls) {
		return (String) JSON.decode(JSONContent, cls);
	}
  
	
	
	
	
	
}
