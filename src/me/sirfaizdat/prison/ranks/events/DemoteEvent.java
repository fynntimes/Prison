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
public class DemoteEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	
	public DemoteEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
