package me.sirfaizdat.prison.ranks.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.Rank;
import me.sirfaizdat.prison.ranks.Ranks;

import java.text.NumberFormat;
import java.text.ParseException;

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
        String val = args[3];

        switch(args[2]) {
            case "id":
                int id;
                try {
                    id = Integer.parseInt(val);
                } catch(NumberFormatException e) {
                    sender.sendMessage(MessageUtil.get("general.invalidInteger"));
                    return;
                }

                if(id < 0 || id > Ranks.i.ranks.size() - 1) {
                    sender.sendMessage(MessageUtil.get("general.valueTooHigh", "value", "0", String.valueOf(Ranks.i.ranks.size() - 1)));
                    return;
                }

                Ranks.i.ranks.remove(rank);
                Ranks.i.ranks.add(id, rank);
                for(int i = 0; i < Ranks.i.ranks.size(); i++) {
                    Ranks.i.ranks.get(i).setId(i);
                    Ranks.i.saveRank(Ranks.i.ranks.get(i));
                }
                sender.sendMessage(MessageUtil.get("ranks.valueSet", "id", val, rank.getName()));
                break;
            case "prefix":
                rank.setPrefix(Prison.color(val));
                Ranks.i.saveRank(rank);
                sender.sendMessage(MessageUtil.get("ranks.valueSet", "prefix", val, rank.getName()));
                break;
            case "price":
                double price;
                try {
                    price = NumberFormat.getCurrencyInstance().parse(val).doubleValue();
                } catch (ParseException e) {
                    sender.sendMessage(MessageUtil.get("ranks.invalidPrice"));
                    return;
                }
                rank.setPrice(price);
                Ranks.i.saveRank(rank);
                sender.sendMessage(MessageUtil.get("ranks.valueSet", "price", val, rank.getName()));
                break;
            default:
                sender.sendMessage(MessageUtil.get("general.cmdNotFound", "/prisonranks set"));
                break;
        }
    }

    @Override
    public String description() {
        return "Set the price or prefix of a rank.";
    }
}
