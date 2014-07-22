/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.sirfaizdat.prison.ranks.cmds;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import me.sirfaizdat.prison.core.AbstractCommandManager;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SirFaizdat
 */
public class RanksCommandManager extends AbstractCommandManager {

	public RanksCommandManager() {
		super(Ranks.i, "prisonranks");
	}

	// Override the onCommand method to add /ranks and /rankup
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("ranks")) {
			if (!sender.hasPermission("prison.ranks.list")) {
				sender.sendMessage(MessageUtil.get("general.noPermission"));
				return true;
			}
			sender.sendMessage(Core
					.colorize("&6===========&c{&2Ranks&c}&6==========="));
			for (Rank rank : Ranks.i.ranks) {
				double amountNeededD = rank.getPrice();
				String amountNeeded;
				if (amountNeededD < 1) {
					amountNeeded = "&6$0.00";
				} else {
					amountNeeded = "&6$"
							+ new DecimalFormat("#,###.00")
									.format(new BigDecimal(amountNeededD));
				}
				sender.sendMessage(Core.colorize(rank.getPrefix() + " &f- "
						+ amountNeeded));
			}
			sender.sendMessage(Core.colorize("&6============================"));
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
	}

}
