package com.shzy.bjj.tools;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * 
 * @brief List工具类
 * @author Fanhao.Yi
 * @date 2015年4月27日下午5:42:53
 * @version V1.0
 */
public class ListTool {

	/**
	 * 默认分隔符号
	 */
	public static final String DEFAULT_JOIN_SEPARATOR = ",";

	private ListTool() {
		throw new AssertionError();
	}

	/**
	 * 获取list
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <V> int getSize(List<V> sourceList) {
		return sourceList == null ? 0 : sourceList.size();
	}

	/**
	 * 判断List是否为空或长度为0
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <V> boolean isEmpty(List<V> sourceList) {
		return (sourceList == null || sourceList.size() == 0);
	}

	/**
	 * 比较两个对象是否相等
	 * 
	 * @param actual
	 * @param expected
	 * @return
	 */
	public static <V> boolean isEquals(ArrayList<V> actual,
			ArrayList<V> expected) {
		if (actual == null) {
			return expected == null;
		}
		if (expected == null) {
			return false;
		}
		if (actual.size() != expected.size()) {
			return false;
		}

		for (int i = 0; i < actual.size(); i++) {
			if (!ObjectTool.isEquals(actual.get(i), expected.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * List转换为字符串，并以默认分隔符分割
	 * 
	 * @param list
	 * @return
	 */
	public static String join(List<String> list) {
		return join(list, DEFAULT_JOIN_SEPARATOR);
	}

	/**
	 * List转换为字符串，并以固定分隔符分割
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String join(List<String> list, char separator) {
		return join(list, new String(new char[] { separator }));
	}

	/**
	 * List转换为字符串，并以固定分隔符分割
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String join(List<String> list, String separator) {
		return list == null ? "" : TextUtils.join(separator, list);
	}

	/**
	 * 向list中添加不重复元素
	 * 
	 * @param sourceList
	 * @param entry
	 * @return
	 */
	public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
		return (sourceList != null && !sourceList.contains(entry)) ? sourceList
				.add(entry) : false;
	}

	/**
	 * 向list中添加不重复元素
	 * 
	 * @param sourceList
	 * @param entryList
	 * @return
	 */
	public static <V> int addDistinctList(List<V> sourceList, List<V> entryList) {
		if (sourceList == null || isEmpty(entryList)) {
			return 0;
		}

		int sourceCount = sourceList.size();
		for (V entry : entryList) {
			if (!sourceList.contains(entry)) {
				sourceList.add(entry);
			}
		}
		return sourceList.size() - sourceCount;
	}

	/**
	 * 获取不同list的数量
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <V> int distinctList(List<V> sourceList) {
		if (isEmpty(sourceList)) {
			return 0;
		}

		int sourceCount = sourceList.size();
		int sourceListSize = sourceList.size();
		for (int i = 0; i < sourceListSize; i++) {
			for (int j = (i + 1); j < sourceListSize; j++) {
				if (sourceList.get(i).equals(sourceList.get(j))) {
					sourceList.remove(j);
					sourceListSize = sourceList.size();
					j--;
				}
			}
		}
		return sourceCount - sourceList.size();
	}

	/**
	 * 向list中添加不重复元素
	 * 
	 * @param sourceList
	 * @param value
	 * @return
	 */
	public static <V> boolean addListNotNullValue(List<V> sourceList, V value) {
		return (sourceList != null && value != null) ? sourceList.add(value)
				: false;
	}

	/**
	 * 得到数组中某个元素前一个元素
	 * 
	 * @param sourceList
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getLast(List<V> sourceList, V value) {
		return (sourceList == null) ? null : (V) ArrayTool.getLast(
				sourceList.toArray(), value, true);
	}

	/**
	 * 得到数组中某个元素下一个元素
	 * 
	 * @param sourceList
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getNext(List<V> sourceList, V value) {
		return (sourceList == null) ? null : (V) ArrayTool.getNext(
				sourceList.toArray(), value, true);
	}

	/**
	 * List转化
	 * 
	 * @param sourceList
	 * @return
	 */
	public static <V> List<V> invertList(List<V> sourceList) {
		if (isEmpty(sourceList)) {
			return sourceList;
		}

		List<V> invertList = new ArrayList<V>(sourceList.size());
		for (int i = sourceList.size() - 1; i >= 0; i--) {
			invertList.add(sourceList.get(i));
		}
		return invertList;
	}
}
