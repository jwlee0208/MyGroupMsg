package com.leejw.utils;

import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * null 체크
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		boolean result = false;
		if(str == null || str == ""){
			result = true;
		}
		return result;
	}
	
	/**
	 * not null 체크
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str){
		return (!isNull(str));
	}
	
	public static String[] splitStr(String value){
		Pattern p = Pattern.compile("[;]+");
		return p.split(value);
	}
}
