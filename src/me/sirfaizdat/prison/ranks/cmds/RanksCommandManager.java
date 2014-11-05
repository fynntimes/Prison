/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import me.sirfaizdat.prison.core.AbstractCommandManager;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

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
			sender.sendMessage(Prison
					.colorize("&6===========&c{&2Ranks&c}&6==========="));
			StringBuilder sb = new StringBuilder();
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
				sb.append(Prison.colorize(rank.getPrefix() + " &f- "
						+ amountNeeded) + "\n");
			}
			int page = 0;
			if(args.length >= 1) {
				try {
					page = Integer.parseInt(args[0]);
				} catch(NumberFormatException e) {
					page = 0;
				}
			}
			sender.sendMessage(ChatPaginator.paginate(sb.toString(), page, 56, 8).getLines());
			sender.sendMessage(Prison.colorize("&cType /ranks " + (page + 1) + ""));
			sender.sendMessage(Prison.colorize("&6============================"));
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
