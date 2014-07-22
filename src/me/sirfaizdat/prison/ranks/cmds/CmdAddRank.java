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

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;
import me.sirfaizdat.prison.ranks.events.RankAddedEvent;

import org.bukkit.Bukkit;

/**
 * @author SirFaizdat
 */
public class CmdAddRank extends Command {

	public CmdAddRank() {
		super("add");
		addRequiredArg("rank");
		addRequiredArg("price");
		addOptionalArg("prefix");
	}

	@Override
	protected void execute() {
		String rankName = args[1];
		if(!Ranks.i.isRank(rankName)) {
			sender.sendMessage(MessageUtil.get("ranks.notARank"));
			return;
		}
		if(Ranks.i.isLoadedRank(rankName)) {
			sender.sendMessage(MessageUtil.get("ranks.alreadyLoaded"));
			return;
		}
		Rank rank = new Rank();
		rank.setName(rankName);
		
		double price = 0;
		try {
			price = Double.parseDouble(args[2].replaceAll("$", "").trim());
		} catch(NumberFormatException e) {
			sender.sendMessage(MessageUtil.get("ranks.invalidPrice"));
			return;
		}
		rank.setPrice(price);
		if(amountOfOptionalArgs >= 1) {
			rank.setPrefix(args[3]);
		} else {
			rank.setPrefix("&6" + rank.getName());
		}
		boolean success = Ranks.i.addRank(rank);
		if(success) {
			sender.sendMessage(MessageUtil.get("ranks.addSuccess", rank.getPrefix()));
			Bukkit.getServer().getPluginManager().callEvent(new RankAddedEvent(rank));
		} else {
			sender.sendMessage(MessageUtil.get("ranks.addFail", rankName));
		}
	}

	@Override
	public String description() {
		return "Adds a rank to Ranks.";
	}

}
