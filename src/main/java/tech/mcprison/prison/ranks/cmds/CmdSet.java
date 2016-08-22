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
import tech.mcprison.prison.core.Prison;
import tech.mcprison.prison.ranks.Rank;
import tech.mcprison.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdSet extends Command {

    public CmdSet() {
        super("set");
        addRequiredArg("rank");
        addRequiredArg("id|price|prefix");
        addRequiredArg("value");
    }

    @Override protected void execute() {
        if (!Ranks.i.isLoadedRank(args[1])) {
            sender.sendMessage(MessageUtil.get("ranks.notARank"));
            return;
        }
        Rank rank = Ranks.i.getRank(args[1]);
        String val = args[3];

        switch (args[2]) {
            case "id":
                int id;
                try {
                    id = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    sender.sendMessage(MessageUtil.get("general.invalidInteger"));
                    return;
                }

                if (id < 0 || id > Ranks.i.ranks.size() - 1) {
                    sender.sendMessage(MessageUtil.get("general.valueTooHigh", "value", "0",
                        String.valueOf(Ranks.i.ranks.size() - 1)));
                    return;
                }

                Ranks.i.ranks.remove(rank);
                Ranks.i.ranks.add(id, rank);
                for (int i = 0; i < Ranks.i.ranks.size(); i++) {
                    Ranks.i.ranks.get(i).setId(i);
                    Ranks.i.saveRank(Ranks.i.ranks.get(i));
                }
                sender.sendMessage(MessageUtil.get("ranks.valueSet", "id", val, rank.getName()));
                break;
            case "prefix":
                rank.setPrefix(Prison.color(val));
                Ranks.i.saveRank(rank);
                sender
                    .sendMessage(MessageUtil.get("ranks.valueSet", "prefix", val, rank.getName()));
                break;
            case "price":
                double price;
                try {
                    val = val.replace("$", "").replaceAll(",", "");
                    price = Double.parseDouble(val);
                } catch (NumberFormatException e) {
                    sender.sendMessage(MessageUtil.get("ranks.invalidPrice"));
                    return;
                }
                rank.setPrice(price);
                Ranks.i.saveRank(rank);
                sender.sendMessage(MessageUtil.get("ranks.valueSet", "price", val, rank.getName()));
                break;
            default:
                sender.sendMessage(MessageUtil.get("general.cmdNotFound", "/prisonranks set"));
                break;
        }
    }

    @Override public String description() {
        return "Set the price or prefix of a rank.";
    }
}
