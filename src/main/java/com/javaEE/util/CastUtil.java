package com.javaEE.util;

/**
 * 转型操作工具类
 * 
 * @author mwb
 * 
 */
public final class CastUtil {

	/**
	 * 转为String类型(默认值为空)
	 */
	public static String castString(Object obj) {
		return castString(obj, "");
	}

	/**
	 * 转为String类型(指定默认值)
	 */
	public static String castString(Object obj, String defaultValue) {
		return obj != null ? String.valueOf(obj) : defaultValue;
	}

	/**
	 * 转double类型 （默认值为0）
	 */
	public static double castDoule(Object obj) {
		return castDouble(obj, 0);
	}

	/**
	 * 转double类型（指定默认值）
	 */
	public static double castDouble(Object obj, double defaultValue) {
		double doubleValue = defaultValue;
		if (obj != null) {
			String strValue = castString(obj);
			if (StringUtil.isNotEmpty(strValue)) {
				try {
					doubleValue = Double.parseDouble(strValue);
				} catch (NumberFormatException e) {
					doubleValue = defaultValue;
				}
			}
		}
		return doubleValue;
	}

	/**
	 * 转long类型（默认值为0）
	 */
	public static long castLong(Object obj){
		return castLong(obj,0);
	}
	/**
	 * 转long类型 （指定默认值）
	 */
	public static long castLong(Object obj, long defaultValue) {
		long longValue = defaultValue;
		if (obj != null) {
			String strValue = castString(obj);
			if (StringUtil.isNotEmpty(strValue)) {
				try {
					longValue=Long.parseLong(strValue);
				} catch (NumberFormatException e) {
					longValue = defaultValue;
				}
			}
		}
		return longValue;
	}
	
	/**
	 * 转int类型（ 默认值为0）
	 */
	public static int castInt(Object obj){
		return castInt(obj,0);
	}
	/**
	 * 转int类型（指定默认值）
	 */
	public static int castInt(Object obj,int defaultValue){
		int intValue=defaultValue;
		if (obj!=null) {
			String strValue=castString(obj);
			if (StringUtil.isNotEmpty(strValue)) {
				try {
					intValue=Integer.parseInt(strValue);
				} catch (NumberFormatException e) {
					intValue=defaultValue;
				}
			}
		}
		return intValue;
	}
	
	/**
	 * 转boolean类型(默认值false)
	 */
	
	/**
	 * 转boolean类型 （指定默认值）
	 */
	public static boolean castBoolean(Object obj,boolean defaultValue){
		boolean value=defaultValue;
		if (obj!=null) {
			value=Boolean.parseBoolean(castString(obj));
		}
		return value;
	}
}






