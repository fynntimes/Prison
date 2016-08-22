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

import org.bukkit.Bukkit;
import tech.mcprison.prison.core.Command;
import tech.mcprison.prison.core.MessageUtil;
import tech.mcprison.prison.core.Prison;
import tech.mcprison.prison.ranks.Rank;
import tech.mcprison.prison.ranks.Ranks;
import tech.mcprison.prison.ranks.events.RankAddedEvent;

/**
 * @author SirFaizdat
 */
public class CmdAddRank extends Command {

    public CmdAddRank() {
        super("add");
        addRequiredArg("rank");
        addRequiredArg("price");
        addOptionalArg("colorfulName");
    }

    @Override protected void execute() {
        String rankName = args[1];
        if (!Ranks.i.isRank(rankName)) {
            sender.sendMessage(
                MessageUtil.get("ranks.notAGroup", Prison.i().getPermissions().getName()));
            return;
        }
        if (Ranks.i.isLoadedRank(rankName)) {
            sender.sendMessage(MessageUtil.get("ranks.alreadyLoaded"));
            return;
        }
        Rank rank = new Rank();
        rank.setName(rankName);

        double price;
        try {
            String val = args[2];
            val = val.replace("$", "").replaceAll(",", "");
            price = Double.parseDouble(val);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtil.get("ranks.invalidPrice"));
            return;
        }
        rank.setPrice(price);
        if (amountOfOptionalArgs >= 1) {
            rank.setPrefix(args[3]);
        } else {
            rank.setPrefix("&3" + rank.getName());
        }
        boolean success = Ranks.i.addRank(rank);
        if (success) {
            sender.sendMessage(MessageUtil.get("ranks.addSuccess", rank.getPrefix()));
            Bukkit.getServer().getPluginManager().callEvent(new RankAddedEvent(rank));
        } else {
            sender.sendMessage(MessageUtil.get("ranks.addFail", rankName));
        }
    }

    @Override public String description() {
        return "Adds a rank to Ranks.";
    }

}
