/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.ItemManager;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;
import org.bukkit.Material;

import java.util.Map;

/**
 * @author SirFaizdat
 */
public class CommandAddBlock extends Command {

    public CommandAddBlock() {
        super("addblock");
        addRequiredArg("mine");
        addRequiredArg("block");
        addRequiredArg("percentage");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void execute() {
        if (!Prison.i().im.isLoaded()) {
            sender.sendMessage(MessageUtil.get("general.itemManagerNotLoaded"));
            return;
        }
        Mine m = Mines.i.mm.getMine(args[1]);
        if (m == null) {
            sender.sendMessage(MessageUtil.get("mines.notFound"));
            return;
        }
        // Begin block recognition
        Block block;
        ItemManager.ItemSet set;
        if (Prison.i().im.isAnInt(args[2].replaceAll(":", ""))) {
            // Begin "if it is an ID"
            String[] blocky = args[2].split(":");
            String data;
            if (blocky.length == 2) {
                data = blocky[1];
            } else {
                data = "0";
            }

            Integer id = null;
            try {
                id = Integer.parseInt(blocky[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(MessageUtil.get("mines.mustSpecifyID"));
                return;
            }

            Material mat = Material.getMaterial(id);
            if (mat == null) {
                sender.sendMessage(MessageUtil.get("mines.invalidId"));
                return;
            }
            if (!mat.isBlock()) {
                sender.sendMessage(MessageUtil.get("mines.notABlock"));
                return;
            }
            byte b;
            try {
                b = Byte.valueOf(data);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(MessageUtil.get("mines.invalidId"));
                return;
            }

            block = new Block(mat.getId(), b);
            try {
                if (m.blocks.get(block.toString()) != null) {
                    sender.sendMessage(MessageUtil.get("mines.alreadyInMine"));
                    return;
                }
            } catch (NullPointerException e) {
            }
            set = new ItemManager.ItemSet(mat.getId(), b);
            // End "IF IT IS AN ID"
        } else {
            // Begin "IF IT IS A WORD"
            set = Prison.i().im.getItem(args[2].toLowerCase());
            if (set == null) {
                sender.sendMessage(MessageUtil.get("mines.blockNotExist"));
                return;
            }
            Material mat = Material.getMaterial(set.id);
            if (mat == null) {
                sender.sendMessage(MessageUtil.get("mines.blockNotExist"));
                return;
            }
            if (!mat.isBlock()) {
                sender.sendMessage(MessageUtil.get("mines.notABlock"));
                return;
            }
            short data = set.data;
            block = new Block(mat.getId(), data);
            try {
                if (m.blocks.get(block.toString()) != null) {
                    sender.sendMessage(MessageUtil.get("mines.alreadyInMine"));
                    return;
                }
            } catch (NullPointerException e) {
            }

        }

        // End block recognition
        String percent = args[3];
        percent = percent.replaceAll("%", "").replaceAll("percent", "");
        double percentage = 0;
        try {
            percentage = Double.valueOf(percent);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(MessageUtil.get("mines.invalidPercent"));
            return;
        }
        if (percentage > 100 || percentage <= 0) {
            sender.sendMessage(MessageUtil.get("mines.invalidPercent"));
            return;
        }
        percentage = percentage / 100;

        double oldPercent = percentage;
        double total = 0;
        for (Map.Entry<String, Block> entry : m.blocks.entrySet()) {
            total += entry.getValue().getChance();
        }
        total += oldPercent;

        if (total > 1) {
            sender.sendMessage(MessageUtil.get("mines.mineFull"));
            return;
        }

        m.addBlock(block, percentage);
        m.save();
        if (Prison.i().im.isLoaded()) {
            sender.sendMessage(MessageUtil.get("mines.addSuccess", m.name,
                    (percentage * 100) + "%", Prison.i().im.getName(set)));
        } else {
            sender.sendMessage(MessageUtil.get("mines.addSuccess", m.name,
                    (percentage * 100) + "%", set.id + ":" + set.data));
        }
    }

    @Override
    public String description() {
        return "Adds a block to the mine.";
    }

}
