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

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.ItemManager.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;

import java.util.Map;

/**
 * @author SirFaizdat
 */
public class CmdInfo extends Command {

    public CmdInfo() {
        super("info");
        addRequiredArg("mine");
    }

    @Override
    protected void execute() {
        Mine m = Mines.i.mm.getMine(args[1]);
        if (m == null) {
            sender.sendMessage(MessageUtil.get("mines.notFound"));
            return;
        }

        sender.sendMessage(Prison.color("&7=========== &3" + m.name + " &7==========="));
        String worldName;
        if (m.worldMissing) {
            worldName = "&cWorld is missing :O";
        } else {
            worldName = "&3" + m.world.getName();
        }

        sender.sendMessage(Prison.color("&7World: " + worldName));
        sender.sendMessage(Prison.color("&7Size: &3" + ((m.maxX - m.minX) + 1) + "&8x&3" + ((m.maxZ - m.minZ) + 1) + "   &7Height: &3" + ((m.maxY - m.minY) + 1)));
        sender.sendMessage(Prison.color("&7Coordinates: &3From " + m.minX + "x," + m.minY + "y," + m.minZ + "z to " + m.maxX + "x," + m.maxY + "y," + m.maxZ + "z"));
        sender.sendMessage(Prison.color("&7Composition:"));
        for (Map.Entry<String, Block> entry : m.blocks.entrySet()) {
            if (Prison.i().im.isLoaded()) {
                sender.sendMessage(Prison.color("  &3" + (entry.getValue().getChance() * 100) + "% &7of &3" + Prison.i().im.getName(new ItemSet(entry.getValue().getId(), entry.getValue().getData()))));
            } else {
                sender.sendMessage(Prison.color("&3block  &c" + entry.getValue().getId() + ":" + entry.getValue().getData()));
            }
        }
        sender.sendMessage(Prison.color("&7================================"));
    }

    @Override
    public String description() {
        return "Get information about a mine.";
    }

}
