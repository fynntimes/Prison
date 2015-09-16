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
public class BalanceChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private double oldBalance;
	private double newBalance;

	public BalanceChangeEvent(Player player, double oldBalance,
			double newBalance) {
		this.player = player;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
	}

	public Player getPlayer() {
		return player;
	}

	public double getOldBalance() {
		return oldBalance;
	}

	public double getNewBalance() {
		return newBalance;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
