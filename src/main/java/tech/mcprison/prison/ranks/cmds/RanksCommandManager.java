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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.core.AbstractCommandManager;
import tech.mcprison.prison.core.MessageUtil;
import tech.mcprison.prison.core.Prison;
import tech.mcprison.prison.ranks.Rank;
import tech.mcprison.prison.ranks.Ranks;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author SirFaizdat
 */
public class RanksCommandManager extends AbstractCommandManager {

    public static String Scales[] =
        {"Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintillion", "Sextillion",
            "Septillion", "Octiliion", "Decillion"};

    public RanksCommandManager() {
        super(Ranks.i, "prisonranks");
    }

    public String numberFormatter(double amount) {

        int exponent = 0;
        String scale = "";
        while (exponent < Scales.length && amount >= 1000) {
            scale = Scales[exponent];
            exponent += 1;
            amount = amount / 1000;

        }

        return new DecimalFormat("###").format(new BigDecimal(amount)) + " " + scale;
    }

    // Override the onCommand method to add /ranks and /rankup
    @Override public boolean onCommand(CommandSender sender, Command command, String label,
        String[] args) {
        if (sender instanceof Player && Prison.i().config.enableMultiworld) {
            if (!isInProperWorld(sender)) {
                return true;
            }
        }
        if (label.equalsIgnoreCase("ranks")) {
            if (!sender.hasPermission("prison.ranks.list")) {
                sender.sendMessage(MessageUtil.get("general.noPermission"));
                return true;
            }
            sender.sendMessage(Prison.color("&7=========== &3Ranks &7==========="));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Ranks.i.ranks.size(); i++) {
                Rank rank = Ranks.i.getRankById(i);
                double amountNeededD = rank.getPrice();
                String amountNeeded;
                if (amountNeededD < 1) {
                    amountNeeded = "&7$0.00";
                } else {
                    amountNeeded = "&7$" + numberFormatter(amountNeededD);
                }
                sb.append(Prison.color(rank.getPrefix() + " &8- " + amountNeeded) + "\n");
            }
            sender.sendMessage(sb.toString());
            sender.sendMessage(Prison.color("&7============================"));
            return true;
        } else if (label.equalsIgnoreCase("rankup")) {
            if (!sender.hasPermission("prison.ranks.purchase")) {
                sender.sendMessage(MessageUtil.get("general.noPermission"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageUtil.get("general.mustBePlayer"));
                return true;
            }
            Ranks.i.promote(sender.getName(), true);
            return true;
        }
        return super.onCommand(sender, command, label, args);
    }

    public void registerCommands() {
        commands.put("add", new CmdAddRank());
        commands.put("remove", new CmdRemoveRank());
        commands.put("promote", new CmdPromote());
        commands.put("demote", new CmdDemote());
        commands.put("set", new CmdSet());
    }

}
