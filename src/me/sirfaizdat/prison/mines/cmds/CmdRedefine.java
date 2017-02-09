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
package me.sirfaizdat.prison.mines.cmds;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.mines.entities.Mine;

/**
 * @author SirFaizdat
 */
public class CmdRedefine extends Command {

    public CmdRedefine() {
        super("redefine");
        addRequiredArg("mine");
    }

    @Override protected void execute() {
        Mine m = Mines.i.mm.getMine(args[1]);
        if (m == null) {
            sender.sendMessage(MessageUtil.get("mines.notFound"));
            return;
        }
        Selection s =
            Mines.i.getWE().getSelection(Prison.i().playerList.getPlayer(sender.getName()));
        if (s == null) {
            sender.sendMessage(MessageUtil.get("mines.makeWESel"));
            return;
        }
        m.minX = s.getMinimumPoint().getBlockX();
        m.minY = s.getMinimumPoint().getBlockY();
        m.minZ = s.getMinimumPoint().getBlockZ();
        m.maxX = s.getMaximumPoint().getBlockX();
        m.maxY = s.getMaximumPoint().getBlockY();
        m.maxZ = s.getMaximumPoint().getBlockZ();
        m.save();
        Mines.i.mm.mines.remove(args[1]);
        Mines.i.mm.addMine(m);
        sender.sendMessage(MessageUtil.get("mines.redefineSuccess", m.name));
    }

    @Override public String description() {
        return "Redefine a mine's area.";
    }


}
