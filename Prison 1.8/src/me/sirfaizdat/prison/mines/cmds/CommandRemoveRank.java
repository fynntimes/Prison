package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

public class CommandRemoveRank extends Command {
	
	public CommandRemoveRank() {
		super("removerank");
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
		for(int i = 0; i < m.ranks.size(); i++) {
			if(args[2].equalsIgnoreCase(m.ranks.get(i))) {
				m.ranks.remove(i);
				sender.sendMessage(MessageUtil.get("mines.removedRank", args[2], m.name));
				m.save();
				return;
			}
		}
		sender.sendMessage(MessageUtil.get("mines.removeFailure"));
	}

	@Override
	public String description() {
		return "Removes a previously associated rank from this mine.";
	}

}
