package org.oinkcraft.antimobspam.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.oinkcraft.antimobspam.AntiMobSpam;

public class CReloadConfig implements CommandExecutor {

	AntiMobSpam plugin;
	String prefix;
	
	public CReloadConfig(AntiMobSpam plugin) {
		this.plugin = plugin;
		this.prefix = plugin.config.getConfigStr("antimobspam.prefix");
	}
	
	
	// Reload config command //
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		// Check if player has * permission //
		if (player.hasPermission("AMS.reload")) {
			// Reload config //
			plugin.config.reload();
			this.prefix = plugin.config.getConfigStr("antimobspam.prefix");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &aConfig reloaded."));
		} else {
			// No permission message //
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cInsufficient Permission."));
		}
		return true;
	}
}
