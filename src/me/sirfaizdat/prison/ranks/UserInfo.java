/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

import org.bukkit.entity.Player;

/**
 * @author SirFaizdat
 */
public class UserInfo {

    private Player player;
    private Rank previousRank;
    private Rank currentRank;
    private Rank nextRank;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Rank getPreviousRank() {
        return previousRank;
    }

    public void setPreviousRank(Rank previousRank) {
        this.previousRank = previousRank;
    }

    public Rank getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Rank currentRank) {
        this.currentRank = currentRank;
    }

    public Rank getNextRank() {
        return nextRank;
    }

    public void setNextRank(Rank nextRank) {
        this.nextRank = nextRank;
    }

}
