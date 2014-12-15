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
public class CommandReset extends Command {

	public CommandReset() {
		super("reset");
		addRequiredArg("mine");

	}

	@Override
	protected void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if (m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		if (m.reset()) {
			sender.sendMessage(MessageUtil.get("mines.resetSuccess", m.name));
		} else {
			sender.sendMessage(MessageUtil.get("mines.resetFailed", m.name));
		}
	}

	@Override
	public String description() {
		return "Reset a mine";
	}

}
