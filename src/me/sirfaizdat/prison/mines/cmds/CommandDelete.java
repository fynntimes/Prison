/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

/**
 * @author SirFaizdat
 */
public class CommandDelete extends Command {

	public CommandDelete() {
		super("delete");
		addRequiredArg("mine");
	}

	public void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if(m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		Mines.i.mm.removeMine(m.name);
		sender.sendMessage(MessageUtil.get("mines.deletedMine"));
	}

	public String description() {
		return "Delete a mine.";
	}

}
