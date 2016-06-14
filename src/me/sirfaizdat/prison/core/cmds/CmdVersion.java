/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;

/**
 * @author SirFaizdat
 */
class CmdVersion extends Command {

    CmdVersion() {
        super("version");
    }

    @Override
    protected void execute() {
        sender.sendMessage("&7============ &3Prison v" + Prison.i().getDescription().getVersion() + " &7============");
        if(Prison.i().getDescription().getVersion().contains("-SNAPSHOT")) sender.sendMessage("&cThis is a development build and may be unstable.");
        sender.sendMessage("&7Written by &3SirFaizdat&7.");
        sender.sendMessage("&7Mines enabled? &3" + Prison.i().mines.isEnabled());
        sender.sendMessage("&7Ranks enabled? &3" + Prison.i().ranks.isEnabled());
        sender.sendMessage("&7=======================================");
    }

    @Override
    public String description() {
        return "View information about Prison.";
    }


}
