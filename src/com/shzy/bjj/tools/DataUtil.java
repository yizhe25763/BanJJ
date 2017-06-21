package com.shzy.bjj.tools;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataUtil {
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
	private static DateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final String TYPE_STR = "str";
	public static final String TYPE_INT = "int";
	public static final String TYPE_BOOL = "bool";
	public static final String TYPE_BYTE = "byte";
	public static final String TYPE_CHAR = "char";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_INTEGER = "integer";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_CHARACTER = "character";
	public static final String TYPE_LONG = "long";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_SHORT = "short";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_DATETIME = "datetime";
	public static final String TYPE_TIME = "time";
	public static final String TYPE_TIMESTAMP = "timestamp";
	public static final String TYPE_SIMPLE_DATE = "simple_date";
	public static final String TYPE_SIMPLE_TIME = "simple_time";
	public static final String TYPE_SIMPLE_DATE_TIME = "simple_date_time";
	public static final String TYPE_INTS = "ints";
	public static final String TYPE_INT_ARRAY = "int_array";
	public static final String TYPE_INT_JAVA_ARRAY = "int[]";
	public static final String TYPE_INTEGER_ARRAY = "integer_array";
	public static final String TYPE_BYTES = "bytes";
	public static final String TYPE_BYTE_JAVA_ARRAY = "byte[]";
	public static final String TYPE_BYTE_ARRAY = "byte_array";
	public static final String TYPE_CHARS = "chars";
	public static final String TYPE_CHAR_JAVA_ARRAY = "char[]";
	public static final String TYPE_CHAR_ARRAY = "char_array";
	public static final String TYPE_SHORT_ARRAY = "short_array";
	public static final String TYPE_SHORT_JAVA_ARRAY = "short[]";
	public static final String TYPE_LONG_ARRAY = "long_array";
	public static final String TYPE_LONG_JAVA_ARRAY = "long[]";
	public static final String TYPE_FLOAT_ARRAY = "float_array";
	public static final String TYPE_FLOAT_JAVA_ARRAY = "float[]";
	public static final String TYPE_DOUBLE_ARRAY = "double_array";
	public static final String TYPE_DOUBLE_JAVA_ARRAY = "double[]";
	public static final String TYPE_STRS = "strs";
	public static final String TYPE_STRINGS = "strings";
	public static final String TYPE_STRING_JAVA_ARRAY = "String[]";
	public static final String TYPE_STRING_ARRAY = "string_array";
	public static final String TYPE_BOOL_ARRAY = "bool_array";
	public static final String TYPE_BOOLEAN_JAVA_ARRAY = "boolean[]";
	public static final String TYPE_BOOLEAN_ARRAY = "boolean_array";
	public static final String TYPE_OBJECT = "object";
	public static final String TYPE_OBJECTS = "objects";
	public static final String TYPE_OBJECT_JAVA_ARRAY = "Object[]";
	public static final String TYPE_OBJECT_ARRAY = "object_array";
	public static final String TYPE_INET_ADDRESS = "inet_address";
	static char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public DataUtil() {
	}

	public static boolean isBasicType(Class type) {
		return Number.class.equals(type.getSuperclass())
				|| Boolean.class.equals(type) || String.class.equals(type)
				|| Date.class.equals(type) || Boolean.TYPE.equals(type)
				|| Integer.TYPE.equals(type) || Byte.TYPE.equals(type)
				|| Long.TYPE.equals(type) || Double.TYPE.equals(type)
				|| Float.TYPE.equals(type);
	}

	public static boolean isObjectType(Class type) {
		return !isBasicType(type);
	}

	public static Map<String, Object> getMap(String[] names, Object[] values) {
		HashMap map = new HashMap();

		for (int i = 0; i < names.length & i < values.length; ++i) {
			map.put(names[i], values[i]);
		}

		return map;
	}

	public static final Date getDate(Object obj) {
		return getDate(obj, format, (Date) null);
	}

	public static final Date getDate(Object obj, Date defValue) {
		return getDate(obj, format, defValue);
	}

	public static final Date getDate(Object obj, DateFormat format) {
		return getDate(obj, format, (Date) null);
	}

	public static final Date getDate(Object obj, DateFormat format,
			Date defValue) {
		if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) obj;
			return new Date(timestamp.getTime()
					+ (long) (timestamp.getNanos() / 1000000));
		} else if (obj instanceof Long) {
			return new Date(((Long) obj).longValue());
		} else if (obj instanceof String) {
			synchronized (format) {
				Date var10000;
				try {
					var10000 = format.parse((String) obj);
				} catch (Exception var6) {
					return defValue;
				}

				return var10000;
			}
		} else {
			return defValue;
		}
	}

	public static final Timestamp getTimestamp(Object obj) {
		return getTimestamp(obj, (Timestamp) null);
	}

	public static final Timestamp getTimestamp(Object obj, Timestamp defValue) {
		if (obj instanceof Timestamp) {
			return (Timestamp) obj;
		} else if (obj instanceof Date) {
			return new Timestamp(((Date) obj).getTime());
		} else if (obj instanceof Long) {
			return new Timestamp(((Long) obj).longValue());
		} else if (obj instanceof String) {
			try {
				return Timestamp.valueOf((String) obj);
			} catch (Exception var3) {
				return defValue;
			}
		} else {
			return defValue;
		}
	}

	public static final Time getTime(Object obj) {
		return getTime(obj, (Time) null);
	}

	public static final Time getTime(Object obj, Time defValue) {
		if (obj instanceof Time) {
			return (Time) obj;
		} else if (obj instanceof Date) {
			return new Time(((Date) obj).getTime());
		} else if (obj instanceof Long) {
			return new Time(((Long) obj).longValue());
		} else if (obj instanceof String) {
			try {
				return Time.valueOf((String) obj);
			} catch (Exception var3) {
				return defValue;
			}
		} else {
			return defValue;
		}
	}

}
