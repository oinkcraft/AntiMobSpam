package org.oinkcraft.antimobspam.util;

// Config shortcut file //

import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	JavaPlugin pl;
	public Config(JavaPlugin plugin) {
		this.pl = plugin;
	}
	
	public void loadDefaultConfigFile() {
		this.pl.saveDefaultConfig();
	}
	
	public void setConfig(String path, String value) {
        this.pl.getConfig().set(path, (Object)value);
        this.pl.saveConfig();
    }

    public void setConfig(String path, boolean value) {
        this.pl.getConfig().set(path, (Object)value);
        this.pl.saveConfig();
    }

    public void setConfig(String path, int value) {
        this.pl.getConfig().set(path, (Object)value);
        this.pl.saveConfig();
    }

    public void addDefaults(String path, String value) {
        this.pl.getConfig().addDefault(path, (Object)value);
        this.pl.getConfig().options().copyDefaults(true);
        this.pl.saveConfig();
    }

    public void addDefaults(String path, boolean value) {
        this.pl.getConfig().addDefault(path, (Object)value);
        this.pl.getConfig().options().copyDefaults(true);
        this.pl.saveConfig();
    }

    public String getConfigStr(String path) {
        return this.pl.getConfig().getString(path);
    }

    public boolean getConfigBool(String path) {
        return this.pl.getConfig().getBoolean(path);
    }

    public int getConfigInt(String path) {
        return this.pl.getConfig().getInt(path);
    }

    public void reload() {
        this.pl.reloadConfig();
    }
    
}
