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

package tech.mcprison.prison.mines.cmds;

import com.sk89q.worldedit.bukkit.selections.Selection;
import tech.mcprison.prison.core.Command;
import tech.mcprison.prison.core.MessageUtil;
import tech.mcprison.prison.core.Prison;
import tech.mcprison.prison.mines.Mine;
import tech.mcprison.prison.mines.Mines;

import java.util.ArrayList;

/**
 * @author SirFaizdat
 */
public class CmdCreate extends Command {

    public CmdCreate() {
        super("create");
        addRequiredArg("name");
        mustBePlayer(true);
    }

    @Override protected void execute() {
        Selection s =
            Mines.i.getWE().getSelection(Prison.i().playerList.getPlayer(sender.getName()));
        if (s == null) {
            sender.sendMessage(MessageUtil.get("mines.makeWESel"));
            return;
        }
        String name = args[1];
        if (Mines.i.mm.getMine(name) != null) {
            sender.sendMessage(MessageUtil.get("mines.alreadyExists"));
            return;
        }
        String world = s.getWorld().getName();
        int minX = s.getMinimumPoint().getBlockX();
        int minY = s.getMinimumPoint().getBlockY();
        int minZ = s.getMinimumPoint().getBlockZ();
        int maxX = s.getMaximumPoint().getBlockX();
        int maxY = s.getMaximumPoint().getBlockY();
        int maxZ = s.getMaximumPoint().getBlockZ();

        Mine m = new Mine(name, world, minX, minY, minZ, maxX, maxY, maxZ, new ArrayList<String>());
        try {
            Mines.i.mm.addMine(m);
            sender.sendMessage(MessageUtil.get("mines.created", m.name));
        } catch (Exception e) {
            sender.sendMessage(MessageUtil.get("mines.failedToCreate"));
            e.printStackTrace();
        }
    }

    public String description() {
        return "Creates a new mine based on your current WorldEdit selection.";
    }

}
