package org.oinkcraft.antimobspam.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Consumer;
import org.oinkcraft.antimobspam.AntiMobSpam;
import org.oinkcraft.antimobspam.util.CountdownTimer;

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
		if (e.getItem().getType() == Material.MONSTER_EGG) 
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
						// Remove key if exists //
						if (SpawnEggListener.spammyPlayers.containsKey(p))
							SpawnEggListener.spammyPlayers.remove(p);
					}
				},
		        // Runs when timer is complete //
		        new Runnable() {
					@Override
					public void run() {
						
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
		// If player is in list, cancel event //
		if (SpawnEggListener.spammyPlayers.containsKey(e.getPlayer())) 
			e.setCancelled(true);
		
		// If eggs used in last 3 seconds is >= the spawn limit, start an egg ban timer on them //
		if (SpawnEggListener.eggsUsed.containsKey(e.getPlayer()) && !SpawnEggListener.spammyPlayers.containsKey(e.getPlayer())) {
			if (SpawnEggListener.eggsUsed.get(e.getPlayer()) >= plugin.config.getConfigInt("antimobspawn.spawnlimit")) {
				createEggBanTimer(e.getPlayer());
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
		
	}
	
	
}
