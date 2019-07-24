package org.oinkcraft.antimobspam;

import org.bukkit.plugin.java.JavaPlugin;
import org.oinkcraft.antimobspam.util.Config;

public class AntiMobSpam extends JavaPlugin {

	// Custom configuration utility //
	public Config config;
	
	@Override
	public void onEnable() {
		// Setup config //
		config = new Config(this);
		
		// Load default configuration file. //
		config.loadDefaultConfigFile();
	}
	
	@Override
	public void onDisable() {
		// Save Configuration //
		saveConfig();
	}
	
}
