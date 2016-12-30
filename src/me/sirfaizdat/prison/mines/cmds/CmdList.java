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
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.mines.entities.Mine;

import java.util.Map;

/**
 * @author SirFaizdat
 */
public class CmdList extends Command {

    public CmdList() {
        super("list");
    }

    public void execute() {
        if (Mines.i.mm.getMines().size() < 1) {
            sender.sendMessage(MessageUtil.get("mines.noMinesLoaded"));
            return;
        }
        sender.sendMessage(Prison.color("&7=========== &3Mines &7==========="));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Mine> mine : Mines.i.mm.getMines().entrySet()) {
            sb.append("&3" + mine.getKey() + "&8, ");
        }
        String returnVal = sb.toString();
        returnVal = returnVal.substring(0, returnVal.length() - 2); // Get rid of last comma
        sender.sendMessage(Prison.color(returnVal));
    }

    public String description() {
        return "Lists all loaded mines.";
    }

}
