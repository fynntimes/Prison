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

package me.sirfaizdat.prison.mines.events;

import me.sirfaizdat.prison.mines.entities.Mine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Camouflage100
 */
public class MineResetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Mine mine;

    public MineResetEvent(Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return this.mine;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
