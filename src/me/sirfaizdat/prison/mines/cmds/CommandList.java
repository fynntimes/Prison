/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import java.util.Map;

/**
 * @author SirFaizdat
 */
public class CommandList extends Command {

    public CommandList() {
        super("list");
    }

    public void execute() {
        if (Mines.i.mm.getMines().size() < 1) {
            sender.sendMessage(MessageUtil.get("mines.noMinesLoaded"));
            return;
        }
        sender.sendMessage(Prison.color("&7=========== &3Mines &7==========="));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Mine> mine : Mines.i.mm.getMines().entrySet()) {
            sb.append("&3" + mine.getKey() + "&8, ");
        }
        String returnVal = sb.toString();
        returnVal = returnVal.substring(0, returnVal.length() - 2); // Get rid of last comma
        sender.sendMessage(Prison.color(returnVal));
    }

    public String description() {
        return "Lists all loaded mines.";
    }

}
