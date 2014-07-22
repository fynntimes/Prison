/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.sirfaizdat.prison.ranks.events;

import java.util.HashMap;

import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.ranks.Ranks;
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
public class BalanceChangeListener implements Listener {

	private static Economy eco = Ranks.i.eco;
	HashMap<String, Double> balance = new HashMap<String, Double>();

	public BalanceChangeListener() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			balance.put(p.getName(), eco.getBalance(p));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.i());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.i(),
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
