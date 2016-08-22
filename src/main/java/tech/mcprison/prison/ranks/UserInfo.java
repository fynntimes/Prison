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

package tech.mcprison.prison.ranks;

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
