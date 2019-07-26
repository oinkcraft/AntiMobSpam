package org.oinkcraft.antimobspam.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.oinkcraft.antimobspam.AntiMobSpam;
import org.oinkcraft.antimobspam.util.Logger;

public class CRLogfile implements CommandExecutor {

	AntiMobSpam plugin;
	String prefix;
	
	public CRLogfile(AntiMobSpam plugin) {
		this.plugin = plugin;
		this.prefix = plugin.config.getConfigStr("antimobspam.prefix");
	}
	
	
	// Clear log command //
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		// Check if player has * permission //
		if (player.hasPermission("*")) {
			// Clear log file //
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &aLog and database cleared."));
			Logger.clearLogFile();
			plugin.dM.clearDatabase();
		} else {
			// No permission message //
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cInsufficient Permission."));
		}
		return true;
	}

}
