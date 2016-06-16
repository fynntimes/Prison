/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdPromote extends Command {

    public CmdPromote() {
        super("promote");
        addRequiredArg("user");
        mustBePlayer(true);
    }

    public void execute() {
        Ranks.i.promote(args[1], false);
    }

    public String description() {
        return "Promote another user, free of charge.";
    }
}