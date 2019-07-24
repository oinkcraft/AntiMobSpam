package org.oinkcraft.antimobspam;

import org.bukkit.plugin.java.JavaPlugin;
import org.oinkcraft.antimobspam.commands.*;
import org.oinkcraft.antimobspam.listeners.SpawnEggListener;
import org.oinkcraft.antimobspam.util.Config;
import org.oinkcraft.antimobspam.util.DataManager;
import org.oinkcraft.antimobspam.util.Logger;

public class AntiMobSpam extends JavaPlugin {

	public Config config;
	public DataManager dM;
	
	@Override
	public void onEnable() {
		// Setup config //
		config = new Config(this);
		config.loadDefaultConfigFile();
		
		// Create instance of listener //
		new SpawnEggListener(this);
		
		// Set static plugin in logger file //
		Logger.plugin = this;
		
		// Create instance of data manager //
		dM = new DataManager(this);
		
		// Register commands //
		getCommand("AMSReload").setExecutor(new CReloadConfig(this));
		getCommand("AMSRadius").setExecutor(new CRadius(this));
		getCommand("AMSResetLog").setExecutor(new CRLogfile(this));
		
	}
	
	@Override
	public void onDisable() {
		// Save Configuration //
		saveConfig();
	}
	
}
