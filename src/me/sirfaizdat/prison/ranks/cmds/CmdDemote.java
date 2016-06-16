/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.ranks.Ranks;
import org.bukkit.entity.Player;

/**
 * @author SirFaizdat
 */
public class CmdDemote extends Command {

    public CmdDemote() {
        super("demote");
        addRequiredArg("user");
        mustBePlayer(true);
    }

    public void execute() {
        Ranks.i.demote((Player) sender, args[1]);
    }

    public String description() {
        return "Demote another user. NO REFUNDS!";
    }
}
