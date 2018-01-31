package com.lesserhydra.moonaware;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;


class ConfigOptions {
	
	private final MoonAware plugin;
	
	private final Map<MoonPhase, Integer> lightThresholdMap;
	private final Map<MoonPhase, Double> surfaceScarcityMap;
	private final Set<EntityType> entityWhitelist;
	
	
	ConfigOptions(MoonAware plugin, FileConfiguration config) {
		this.plugin = plugin;
		
		this.lightThresholdMap = parseLightThresholds(config);
		this.surfaceScarcityMap = parseSurfaceScarcities(config);
		this.entityWhitelist = parseMonsterWhitelist(config);
	}

	int getLightThreshold(MoonPhase moon) {
		return lightThresholdMap.get(moon);
	}
	
	double getSurfaceScarcity(MoonPhase moon) {
		return surfaceScarcityMap.get(moon);
	}
	
	boolean isEntityEnabled(MoonPhase moon, EntityType type) {
		return entityWhitelist.contains(type);
	}
	
	private Map<MoonPhase, Integer> parseLightThresholds(FileConfiguration config) {
		ConfigurationSection section = config.getConfigurationSection("Light Threshold Per Moon Phase");
		Map<MoonPhase, Integer> result = new EnumMap<>(MoonPhase.class);
		Arrays.stream(MoonPhase.values())
				.forEach(moon -> result.put(moon, section.getInt(moon.toString(), 23)));
		return result;
	}
	
	private Map<MoonPhase, Double> parseSurfaceScarcities(FileConfiguration config) {
		ConfigurationSection section = config.getConfigurationSection("Surface Scarcity Per Moon Phase");
		Map<MoonPhase, Double> result = new EnumMap<>(MoonPhase.class);
		Arrays.stream(MoonPhase.values())
				.forEach(moon -> result.put(moon, section.getDouble(moon.toString(), 1)));
		return result;
	}
	
	private Set<EntityType> parseMonsterWhitelist(FileConfiguration config) {
		EnumSet<EntityType> result = EnumSet.allOf(EntityType.class);
		
		for (String typeString : config.getStringList("Mobs to Ignore")) {
			EntityType type = EnumUtils.parseEnum(typeString, EntityType.class);
			if (type == null) {
				plugin.getLogger().info(ChatColor.RED + "Skipping unknown entity type: " + typeString.toUpperCase());
				continue;
			}
			result.remove(type);
		}
		
		return Collections.unmodifiableSet(result);
	}
	
}
