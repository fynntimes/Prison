/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;
import me.sirfaizdat.prison.ranks.events.RankAddedEvent;
import org.bukkit.Bukkit;

/**
 * @author SirFaizdat
 */
public class CmdAddRank extends Command {

    public CmdAddRank() {
        super("add");
        addRequiredArg("rank");
        addRequiredArg("price");
        addOptionalArg("prefix");
    }

    @Override
    protected void execute() {
        String rankName = args[1];
        if (!Ranks.i.isRank(rankName)) {
            sender.sendMessage(MessageUtil.get("ranks.notAGroup", Prison.i().getPermissions().getName()));
            return;
        }
        if (Ranks.i.isLoadedRank(rankName)) {
            sender.sendMessage(MessageUtil.get("ranks.alreadyLoaded"));
            return;
        }
        Rank rank = new Rank();
        rank.setName(rankName);

        double price = 0;
        try {
            price = Double.parseDouble(args[2].replaceAll("$", "").trim());
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtil.get("ranks.invalidPrice"));
            return;
        }
        rank.setPrice(price);
        if (amountOfOptionalArgs >= 1) {
            rank.setPrefix(args[3]);
        } else {
            rank.setPrefix("&3" + rank.getName());
        }
        boolean success = Ranks.i.addRank(rank);
        if (success) {
            sender.sendMessage(MessageUtil.get("ranks.addSuccess", rank.getPrefix()));
            Bukkit.getServer().getPluginManager().callEvent(new RankAddedEvent(rank));
        } else {
            sender.sendMessage(MessageUtil.get("ranks.addFail", rankName));
        }
    }

    @Override
    public String description() {
        return "Adds a rank to Ranks.";
    }

}
