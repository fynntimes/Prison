/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;

/**
 * @author SirFaizdat
 */
public class CmdVersion extends Command {


    public CmdVersion() {
        super("version");
    }

    @Override
    protected void execute() {
        sender.sendMessage(MessageUtil.get("general.versionString"));
    }

    @Override
    public String description() {
        return "View information about Prison.";
    }


}
