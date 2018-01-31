package com.lesserhydra.moonaware;

import java.util.EnumSet;
import java.util.regex.Pattern;

class EnumUtils {
	
	private static Pattern enumSeperaterPattern = Pattern.compile("[_\\s]");
	
	static <T extends Enum<T>> T parseEnum(String typeString, final Class<T> enumClass) {
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
