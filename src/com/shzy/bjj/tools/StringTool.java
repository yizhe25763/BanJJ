package com.shzy.bjj.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @brief 字符串工具类
 * @author Fanhao.Yi
 * @date 2015年4月23日下午6:59:39
 * @version V1.0
 */
public class StringTool {

	/**
	 * 获取UUID
	 * 
	 * @return 32UUID小写字符串
	 */
	public static String gainUUID() {
		String strUUID = UUID.randomUUID().toString();
		strUUID = strUUID.replaceAll("-", "").toLowerCase();
		return strUUID;
	}

	/**
	 * 判断字符串是否非空非null
	 * 
	 * @param strParm
	 *            需要判断的字符串
	 * @return 真假
	 */
	public static boolean isNoBlankAndNoNull(String strParm) {
		return !((strParm == null) || (strParm.equals("")));
	}

	/**
	 * 将流转成字符串
	 * 
	 * @param is
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	/**
	 * 将文件转成字符串
	 * 
	 * @param file
	 *            文件
	 * @return
	 * @throws Exception
	 */
	public static String getStringFromFile(File file) throws Exception {
		FileInputStream fin = new FileInputStream(file);
		String ret = convertStreamToString(fin);
		fin.close();
		return ret;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
