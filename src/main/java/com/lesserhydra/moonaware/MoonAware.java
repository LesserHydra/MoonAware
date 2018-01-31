package com.lesserhydra.moonaware;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class MoonAware extends JavaPlugin implements Listener {
	
	private static final Random rand = new Random();
	private ConfigOptions options;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(this, this);
		options = new ConfigOptions(this, getConfig());
	}
	
	//Reload command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) return false;
		
		saveDefaultConfig();
		reloadConfig();
		options = new ConfigOptions(this, getConfig());
		
		sender.sendMessage(ChatColor.GREEN + "Reloaded MoonAware");
		return true;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.NATURAL) return;
		if (!(event.getEntity() instanceof Monster)) return;
		
		//Decide if spawn should be canceled
		event.setCancelled(!shouldSpawn(event.getLocation().getBlock(), event.getEntity()));
	}
	
	private boolean shouldSpawn(Block block, LivingEntity entity) {
		MoonPhase moonPhase = MoonPhase.fromWorld(block.getWorld());
		
		if (block.getWorld().getEnvironment() != Environment.NORMAL) return true;
		if (!options.isEntityEnabled(moonPhase, entity.getType())) return true;
		
		int lightThreshold = options.getLightThreshold(moonPhase);
		int skyLight = block.getLightFromSky();
		int blockLight = block.getLightFromBlocks();
		
		//Block spawns in light
		if (skyLight + blockLight >= lightThreshold) return false;
		//Reduce spawns in skylight
		if (rand.nextDouble() * (skyLight / 15.0) >= options.getSurfaceScarcity(moonPhase)) return false;
		
		return true;
	}
	
}
