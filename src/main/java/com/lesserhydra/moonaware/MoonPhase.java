package com.lesserhydra.moonaware;

import org.bukkit.World;


public enum MoonPhase {
	
	FULL			("Full Moon"),
	WANING_GIBBOUS	("Waning Gibbous"),
	LAST_QUARTER	("Last Quarter"),
	WANING_CRESCENT	("Waning Crescent"),
	NEW				("New Moon"),
	WAXING_CRESCENT	("Waxing Crescent"),
	FIRST_QUARTER	("First Quarter"),
	WAXING_GIBBOUS	("Waxing Gibbous");
	
	
	private final String nameString;
	
	MoonPhase(String nameString) {
		this.nameString = nameString;
	}
	
	@Override
	public String toString() {
		return nameString;
	}
	
	public static MoonPhase fromWorld(World world) {
		return fromFullTime(world.getFullTime());
	}
	
	private static MoonPhase fromFullTime(long worldTime) {
		int i = (int) (worldTime/24000%8);
		return values()[i];
	}
	
}
