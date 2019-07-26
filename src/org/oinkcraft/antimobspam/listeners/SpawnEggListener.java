package org.oinkcraft.antimobspam.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Consumer;
import org.oinkcraft.antimobspam.AntiMobSpam;
import org.oinkcraft.antimobspam.util.CountdownTimer;
import org.oinkcraft.antimobspam.util.Logger;

public class SpawnEggListener implements Listener {

	// List of players not allowed to use 
	public static HashMap<Player, Integer> spammyPlayers = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> eggsUsed = new HashMap<Player, Integer>();
	
	AntiMobSpam plugin;
	
	public SpawnEggListener(AntiMobSpam pl) {
		this.plugin = pl;
		// Register events //
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}
	
	// Check if player interacted with spawn egg, if so call 'onSpawnEggUsed'. //
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		// Check if player used monster egg //
		Material item = e.getItem().getType();
		if (item.name().endsWith("_SPAWN_EGG"))
			onSpawnEggUsed(e);
	}
	
	public void createEggBanTimer(final Player p) {
		CountdownTimer timer = new CountdownTimer(
				this.plugin,
		        plugin.config.getConfigInt("antimobspam.spampreventiontime"),
		        // Runs before timer starts //
		        new Runnable() {
					@Override
					public void run() {
						// Remove eggsUsed value //
						if (SpawnEggListener.eggsUsed.containsKey(p)) 
							SpawnEggListener.eggsUsed.remove(p);
					}
				},
		        // Runs when timer is complete //
		        new Runnable() {
					@Override
					public void run() {
						// Remove key if exists //
						if (SpawnEggListener.spammyPlayers.containsKey(p))
							SpawnEggListener.spammyPlayers.remove(p);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getConfigStr("antimobspam.messages.allowed").replace("%prefix%", plugin.config.getConfigStr("antimobspam.prefix"))));
					}
				},
		        // Runs every second of timer //
		        new Consumer<CountdownTimer>() {
					@Override
					public void accept(CountdownTimer t) {
						// Remove key if exists //
						if (SpawnEggListener.spammyPlayers.containsKey(p))
							SpawnEggListener.spammyPlayers.remove(p);
						// Set key to seconds left //
						SpawnEggListener.spammyPlayers.put(p, t.getSecondsLeft());
					}
				}

		);

		// Start scheduling, don't use the "run" method unless you want to skip a second
		timer.scheduleTimer();
	}
	
	// Player has used egg //
	public void onSpawnEggUsed(final PlayerInteractEvent e) {
		
		// If eggs used in last 3 seconds is >= the spawn limit, start an egg ban timer on them //
		if (SpawnEggListener.eggsUsed.containsKey(e.getPlayer()) && !SpawnEggListener.spammyPlayers.containsKey(e.getPlayer()) && !e.getPlayer().hasPermission("AMS.bypass")) {
			if (SpawnEggListener.eggsUsed.get(e.getPlayer()) >= plugin.config.getConfigInt("antimobspam.spawnlimit")) {
				createEggBanTimer(e.getPlayer());
			}
		}
		// If eggs used in last 3 seconds WHILE ALREADY NOT ALLOWED TO SPAM, is >= the spawn limit, remove the item from their hand
		else if (SpawnEggListener.eggsUsed.containsKey(e.getPlayer()) && SpawnEggListener.spammyPlayers.containsKey(e.getPlayer())) {
			if (SpawnEggListener.eggsUsed.get(e.getPlayer()) >= plugin.config.getConfigInt("antimobspam.spamwhileblockedlimit")) {
				// Remove item from inventory
				try {
					e.getPlayer().getInventory().removeItem(e.getItem());
				} catch (Exception e1){ }
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getConfigStr("antimobspam.messages.removemessage").replace("%prefix%", plugin.config.getConfigStr("antimobspam.prefix"))));
			}
		}
				
		if (!eggsUsed.containsKey(e.getPlayer())) {
			// Set 3 second timer to clear once time's up //
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				 public void run() {
				      SpawnEggListener.eggsUsed.remove(e.getPlayer());
				 }
			}, 60L);
			eggsUsed.put(e.getPlayer(), 1);
		} else {
			SpawnEggListener.eggsUsed.put(e.getPlayer(), eggsUsed.get(e.getPlayer()) + 1);
		}
		
		// If player is in list, cancel event //
		if (SpawnEggListener.spammyPlayers.containsKey(e.getPlayer())) {
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getConfigStr("antimobspam.messages.preventionmessage").replace("%prefix%", plugin.config.getConfigStr("antimobspam.prefix")).replace("%time_left%", spammyPlayers.get(e.getPlayer()).toString())));
			e.setCancelled(true);
		} else {
			Logger.logToFile(e.getPlayer().getName() + " has used a spawn egg at location: X: {x}, Y: {y}, Z: {z}".replace("{x}", Integer.toString(e.getPlayer().getLocation().getBlockX())).replace("{y}", Integer.toString(e.getPlayer().getLocation().getBlockY())).replace("{z}", Integer.toString(e.getPlayer().getLocation().getBlockZ())));
			plugin.dM.addMobSpawn(e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY(), e.getPlayer().getLocation().getBlockZ(), e.getPlayer().getName(), "Coming Soon", e.getPlayer().getLocation().getWorld().getName());
		}
		
	}
	
	
}
