package com.shzy.bjj.tools;


/**
 * 
 * @brief 将textview中的字符全角化
 * @author Fanhao.Yi
 * @date 2015年5月14日下午4:38:31
 * @version V1.0
 */
public class TextViewTool {

	/**
	 *  半角转为全角
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
