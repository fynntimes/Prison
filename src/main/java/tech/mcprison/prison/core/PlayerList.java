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

package tech.mcprison.prison.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class PlayerList implements Listener {

    private static HashMap<String, Player> players = new HashMap<String, Player>();

    public PlayerList() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.put(p.getName(), p);
        }
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        players.put(e.getPlayer().getName(), e.getPlayer());
    }

    @EventHandler public void onPlayerLeave(PlayerQuitEvent e) {
        if (players.get(e.getPlayer().getName()) != null) {
            players.remove(e.getPlayer().getName());
        }
    }

    @EventHandler public void onPlayerKick(PlayerKickEvent e) {
        if (players.get(e.getPlayer().getName()) != null) {
            players.remove(e.getPlayer().getName());
        }
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

}
