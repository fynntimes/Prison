/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        players.put(e.getPlayer().getName(), e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (players.get(e.getPlayer().getName()) != null) {
            players.remove(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if (players.get(e.getPlayer().getName()) != null) {
            players.remove(e.getPlayer().getName());
        }
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

}
