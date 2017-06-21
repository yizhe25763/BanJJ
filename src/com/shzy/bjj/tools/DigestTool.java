package com.shzy.bjj.tools;

import java.security.MessageDigest;

/**
 * 
 * @brief MD5工具类
 * @author Fanhao.Yi
 * @date 2015年4月27日下午4:12:59
 * @version V1.0
 */

public class DigestTool {

	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private DigestTool() {
		throw new AssertionError();
	}

	/**
	 * 转化为MD5
	 * 
	 * @param str
	 *            需要转化的内容
	 * @return 转化为MD5的内容
	 */
	public static String md5(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(str.getBytes());
			return new String(encodeHex(messageDigest.digest()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	protected static char[] encodeHex(final byte[] data) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return out;
	}

}
