package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

public class CommandAddRank extends Command {

	public CommandAddRank() {
		super("addrank");
		addRequiredArg("mine");
		addRequiredArg("rank");
	}

	@Override
	protected void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if (m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		Rank r = Ranks.i.getRank(args[2]);
		if(r == null) {
			sender.sendMessage(MessageUtil.get("ranks.notARank"));
			return;
		}
		for(String s : m.ranks) {
			if(s.equalsIgnoreCase(args[2])) {
				sender.sendMessage(MessageUtil.get("mines.rankAlreadyAdded"));
				return;
			}
		}
		m.ranks.add(r.getName());
		m.save();
		sender.sendMessage(MessageUtil.get("mines.addedRank", r.getPrefix(), m.name));
	}

	@Override
	public String description() {
		return "Associates a rank with this mine. Only the ranks that are added to this mine can break blocks in it.";
	}

}
