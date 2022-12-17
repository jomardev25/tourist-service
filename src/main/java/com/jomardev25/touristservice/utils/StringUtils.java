package com.jomardev25.touristservice.utils;

public class StringUtils {

	public static String camelToSnake(String camelStr) {
		String ret = camelStr.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");
		return ret.toLowerCase();
	}

}
