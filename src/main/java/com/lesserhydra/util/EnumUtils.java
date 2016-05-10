package com.lesserhydra.util;

import java.util.EnumSet;
import java.util.regex.Pattern;

public class EnumUtils {
	
	private static Pattern enumSeperaterPattern = Pattern.compile("[_\\s]");
	
	public static <T extends Enum<T>> T parseEnum(String typeString, final Class<T> enumClass) {
		//Find enum from value
		String lookupName = enumSeperaterPattern.matcher(typeString.toUpperCase()).replaceAll("");
		for (T type : EnumSet.allOf(enumClass)) {
			String enumName = enumSeperaterPattern.matcher(type.name()).replaceAll("");
			String enumString = enumSeperaterPattern.matcher(type.toString().toUpperCase()).replaceAll("");
			if (lookupName.equals(enumName) || lookupName.equals(enumString)) return type;
		}
		//Not found
		return null;
	}
	
}
