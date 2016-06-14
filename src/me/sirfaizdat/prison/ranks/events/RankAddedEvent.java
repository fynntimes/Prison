/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.events;

import me.sirfaizdat.prison.ranks.Rank;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author SirFaizdat
 */
public class RankAddedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Rank newRank;

    public RankAddedEvent(Rank newRank) {
        this.newRank = newRank;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Rank getRank() {
        return newRank;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
