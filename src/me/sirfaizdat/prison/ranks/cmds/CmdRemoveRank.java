/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdRemoveRank extends Command {

	public CmdRemoveRank() {
		super("remove");
		addRequiredArg("rank");
	}
	
	@Override
	protected void execute() {
		if(!Ranks.i.isLoadedRank(args[1])) {
			sender.sendMessage(MessageUtil.get("ranks.notARank"));
			return;
		}
		Rank rank = Ranks.i.getRank(args[1]);
		boolean success = Ranks.i.removeRank(rank);
		if(success) {
			sender.sendMessage(MessageUtil.get("ranks.removeSuccess", rank.getPrefix()));
		} else {
			sender.sendMessage(MessageUtil.get("ranks.removeFail", rank.getPrefix()));
		}
	}

	@Override
	public String description() {
		return "Removes a rank from Ranks.";
	}

}
