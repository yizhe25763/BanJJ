package com.shzy.bjj.tools;

/**
 * 
 * @brief 数组工具类
 * @author Fanhao.Yi
 * @date 2015年4月27日下午5:18:52
 * @version V1.0
 */
public class ArrayTool {

	private ArrayTool() {
		throw new AssertionError();
	}

	/**
	 * 判断数组是否为空或长度为0
	 * 
	 * @param sourceArray
	 * @return
	 */
	public static <V> boolean isEmpty(V[] sourceArray) {
		return (sourceArray == null || sourceArray.length == 0);
	}

	/**
	 * 得到数组中某个元素前一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static <V> V getLast(V[] sourceArray, V value, V defaultValue,
			boolean isCircle) {
		if (isEmpty(sourceArray)) {
			return defaultValue;
		}

		int currentPosition = -1;
		for (int i = 0; i < sourceArray.length; i++) {
			if (ObjectTool.isEquals(value, sourceArray[i])) {
				currentPosition = i;
				break;
			}
		}
		if (currentPosition == -1) {
			return defaultValue;
		}

		if (currentPosition == 0) {
			return isCircle ? sourceArray[sourceArray.length - 1]
					: defaultValue;
		}
		return sourceArray[currentPosition - 1];
	}

	/**
	 * 得到数组中某个元素下一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static <V> V getNext(V[] sourceArray, V value, V defaultValue,
			boolean isCircle) {
		if (isEmpty(sourceArray)) {
			return defaultValue;
		}

		int currentPosition = -1;
		for (int i = 0; i < sourceArray.length; i++) {
			if (ObjectTool.isEquals(value, sourceArray[i])) {
				currentPosition = i;
				break;
			}
		}
		if (currentPosition == -1) {
			return defaultValue;
		}

		if (currentPosition == sourceArray.length - 1) {
			return isCircle ? sourceArray[0] : defaultValue;
		}
		return sourceArray[currentPosition + 1];
	}

	/**
	 * 得到数组中某个元素前一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static <V> V getLast(V[] sourceArray, V value, boolean isCircle) {
		return getLast(sourceArray, value, null, isCircle);
	}

	/**
	 * 得到数组中某个元素下一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static <V> V getNext(V[] sourceArray, V value, boolean isCircle) {
		return getNext(sourceArray, value, null, isCircle);
	}

	/**
	 * 得到数组中某个元素前一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static long getLast(long[] sourceArray, long value,
			long defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException(
					"The length of source array must be greater than 0.");
		}

		Long[] array = ObjectTool.transformLongArray(sourceArray);
		return getLast(array, value, defaultValue, isCircle);

	}

	/**
	 * 得到数组中某个元素下一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static long getNext(long[] sourceArray, long value,
			long defaultValue, boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException(
					"The length of source array must be greater than 0.");
		}

		Long[] array = ObjectTool.transformLongArray(sourceArray);
		return getNext(array, value, defaultValue, isCircle);
	}

	/**
	 * 得到数组中某个元素前一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static int getLast(int[] sourceArray, int value, int defaultValue,
			boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException(
					"The length of source array must be greater than 0.");
		}

		Integer[] array = ObjectTool.transformIntArray(sourceArray);
		return getLast(array, value, defaultValue, isCircle);

	}

	/**
	 * 得到数组中某个元素下一个元素
	 * 
	 * @param sourceArray
	 * @param value
	 * @param defaultValue
	 * @param isCircle
	 *            是否循环
	 * @return
	 */
	public static int getNext(int[] sourceArray, int value, int defaultValue,
			boolean isCircle) {
		if (sourceArray.length == 0) {
			throw new IllegalArgumentException(
					"The length of source array must be greater than 0.");
		}

		Integer[] array = ObjectTool.transformIntArray(sourceArray);
		return getNext(array, value, defaultValue, isCircle);
	}
}
