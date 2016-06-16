package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

/**
 * @author SirFaizdat
 */
public class CmdSet extends Command {

    public CmdSet() {
        super("set");
        addRequiredArg("rank");
        addRequiredArg("id|price|prefix");
        addRequiredArg("value");
    }

    @Override
    protected void execute() {
        if (!Ranks.i.isLoadedRank(args[1])) {
            sender.sendMessage(MessageUtil.get("ranks.notARank"));
            return;
        }
        Rank rank = Ranks.i.getRank(args[1]);

        switch(args[2]) {
            case "id":
                String val = args[3];
                int id;
                try {
                    id = Integer.parseInt(val);
                } catch(NumberFormatException e) {
                    sender.sendMessage(MessageUtil.get("general.integerValue"));
                    return;
                }
                Ranks.i.ranks.remove(rank);
                Ranks.i.ranks.add(id, rank);
                for(int i = 0; i < Ranks.i.ranks.size(); i++) {
                    Ranks.i.ranks.get(i).setId(i);
                    Ranks.i.saveRank(Ranks.i.ranks.get(i));
                }
                break;
            default:
                sender.sendMessage("Not supported yet.");
                break;
        }
    }

    @Override
    public String description() {
        return "Set the price or prefix of a rank.";
    }
}
