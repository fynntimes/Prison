/**
 * (C) 2014 SirFaizdat
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
			if(!sender.hasPermission("prison.ranks.list")) {
				sender.sendMessage(MessageUtil.get("general.noPermission"));
				return true;
			}
			sender.sendMessage(Core
					.colorize("&6===========&c{&2Ranks&c}&6==========="));
			for (Rank rank : Ranks.i.ranks) {
				double amountNeededD = rank.getPrice();
				String amountNeeded = new DecimalFormat("#,###.00")
						.format(new BigDecimal(amountNeededD));
				sender.sendMessage(Core.colorize(rank.getPrefix() + " &f- &6$"
						+ amountNeeded));
			}
			sender.sendMessage(Core
					.colorize("&6============================"));
			return true;
		} else if (label.equalsIgnoreCase("rankup")) {
			if(!sender.hasPermission("prison.ranks.purchase")) {
				sender.sendMessage(MessageUtil.get("general.noPermission"));
				return true;
			}
			if(!(sender instanceof Player)) {
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
