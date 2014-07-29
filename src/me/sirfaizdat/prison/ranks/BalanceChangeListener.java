/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

import java.util.HashMap;

import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.events.BalanceChangeEvent;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author SirFaizdat
 */
class BalanceChangeListener implements Listener {

	private static Economy eco = Ranks.i.eco;
	HashMap<String, Double> balance = new HashMap<String, Double>();

	public BalanceChangeListener() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			balance.put(p.getName(), eco.getBalance(p));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, Prison.i());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Prison.i(),
				new Runnable() {
					@Override
					public void run() {
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							if (eco.getBalance(p) != balance.get(p.getName())) {
								Bukkit.getServer()
										.getPluginManager()
										.callEvent(
												new BalanceChangeEvent(
														p,
														balance.get(p.getName()),
														eco.getBalance(p)));
								balance.remove(p.getName());
								balance.put(p.getName(), eco.getBalance(p));
							}
						}
					}
				}, 5 * 20L, 5 * 20L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		balance.put(event.getPlayer().getName(),
				eco.getBalance(event.getPlayer()));
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (balance.containsKey(event.getPlayer().getName()))
			balance.remove(event.getPlayer().getName());
	}

}
