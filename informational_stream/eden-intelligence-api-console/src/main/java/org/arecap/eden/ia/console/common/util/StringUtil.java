package org.arecap.eden.ia.console.common.util;

public class StringUtil {

	public static String toLowerCaseFirstLetter(String s) {
		if (s == null || s.isEmpty()) {return s;}
		return s.substring(0,1).toLowerCase()+s.substring(1, s.length());
	}
}
