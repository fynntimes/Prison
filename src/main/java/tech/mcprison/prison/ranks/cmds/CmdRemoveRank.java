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

package tech.mcprison.prison.ranks.cmds;

import tech.mcprison.prison.core.Command;
import tech.mcprison.prison.core.MessageUtil;
import tech.mcprison.prison.ranks.Rank;
import tech.mcprison.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdRemoveRank extends Command {

    public CmdRemoveRank() {
        super("remove");
        addRequiredArg("rank");
    }

    @Override protected void execute() {
        if (!Ranks.i.isLoadedRank(args[1])) {
            sender.sendMessage(MessageUtil.get("ranks.notARank"));
            return;
        }
        Rank rank = Ranks.i.getRank(args[1]);
        boolean success = Ranks.i.removeRank(rank);
        if (success) {
            sender.sendMessage(MessageUtil.get("ranks.removeSuccess", rank.getPrefix()));
        } else {
            sender.sendMessage(MessageUtil.get("ranks.removeFail", rank.getPrefix()));
        }
    }

    @Override public String description() {
        return "Removes a rank from Ranks.";
    }

}
