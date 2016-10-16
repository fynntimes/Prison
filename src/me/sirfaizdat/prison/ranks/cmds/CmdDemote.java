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

package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdDemote extends Command {

    public CmdDemote() {
        super("demote");
        addRequiredArg("user");
        addOptionalArg("refund");
        mustBePlayer(true);
    }

    public void execute() {
        boolean refund = false;
        if (args[2] != null) {
            String a2 = args[2].toLowerCase();
            if (a2.equals("yes") || a2.equals("true") || a2.equals("1"))
                refund = true;
        }

        Ranks.i.demote(sender, args[1], refund);
    }

    public String description() {
        return "Demote another user.";
    }
}
