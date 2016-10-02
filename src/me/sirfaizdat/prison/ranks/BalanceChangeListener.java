/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.sirfaizdat.prison.ranks;

import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.events.BalanceChangeEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class BalanceChangeListener implements Listener {

    private static Economy eco;
    private HashMap<String, Double> balance;

    public BalanceChangeListener() {
        eco = Ranks.i.eco;
        balance = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            balance.put(p.getName(), eco.getBalance(p));
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, Prison.i());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Prison.i(), new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (eco.getBalance(p) != balance.get(p.getName())) {
                        Bukkit.getServer().getPluginManager().callEvent(new BalanceChangeEvent(p, balance.get(p.getName()), eco.getBalance(p)));
                        balance.remove(p.getName());
                        balance.put(p.getName(), eco.getBalance(p));
                    }
                }
            }
        }, 5 * 20L, 5 * 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        balance.put(event.getPlayer().getName(), eco.getBalance(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (balance.containsKey(event.getPlayer().getName())) balance.remove(event.getPlayer().getName());
    }

}
