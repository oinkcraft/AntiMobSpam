package org.oinkcraft.antimobspam.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.oinkcraft.antimobspam.AntiMobSpam;
import org.oinkcraft.antimobspam.util.Vector3;

public class CRadius implements CommandExecutor {

	AntiMobSpam plugin;
	String prefix;
	
	public CRadius(AntiMobSpam plugin) {
		this.plugin = plugin;
		this.prefix = plugin.config.getConfigStr("org.oinkcraft.antimobspam.prefix");
	}
	
	// Radius command //
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		// Check permissions //
		if (player.hasPermission("AMS.radius")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &aScanning for nearby mob spawns..."));
			// Get vector radius //
			ArrayList<Vector3> mobSpawns = plugin.dM.getMobSpawns(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getLocation().getWorld().getName());
			// Loop through each mob spawn //
			for (Vector3 v : mobSpawns) {
				// Get location from first vector //
				Vector3 loc = (Vector3) v.v1;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &aMob spawned by "+v.v2+" at location X: "+loc.v1+", Y: "+loc.v2+", Z: "+loc.v3));
			}
		} else {
			// No permission message //
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &cInsufficient Permissions."));
		}
		return true;
	}

}
