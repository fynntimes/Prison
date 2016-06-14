package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;

// TODO Finish for 2.1 release
public class CmdSetPositition extends Command {

    public CmdSetPositition() {
        super("setposition");
        addRequiredArg("rank");
        addRequiredArg("position");
    }

    @Override
    protected void execute() {
        String rankName = args[1];
    }

    @Override
    public String description() {
        return "Sets the position of this rank. When a user ranks up, it will rank that player up to the rank with the next number.";
    }

}
