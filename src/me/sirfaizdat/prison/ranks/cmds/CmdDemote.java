/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdDemote extends Command {

	public CmdDemote() {
		super("demote");
		addRequiredArg("user");
	}

	public void execute() {
		Ranks.i.demote(args[1]);
	}

	public String description() {
		return "Demote another user. NO REFUNDS!";
	}
}
