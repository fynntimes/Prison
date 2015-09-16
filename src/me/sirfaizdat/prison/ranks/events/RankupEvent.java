/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author SirFaizdat
 */
public class RankupEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private Player player;
    private boolean bought;
 
    public RankupEvent(Player player, boolean bought) {
        this.player = player;
        this.bought = bought;
    }
 
    public Player getPlayer() {
        return player;
    }
    
    public boolean wasBought() {
    	return bought;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
