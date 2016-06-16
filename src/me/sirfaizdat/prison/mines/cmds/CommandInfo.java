/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.ItemManager.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import java.util.Map;

import static me.sirfaizdat.prison.core.Prison.l;

/**
 * @author SirFaizdat
 */
public class CommandInfo extends Command {

    public CommandInfo() {
        super("info");
        addRequiredArg("mine");
    }

    @Override
    protected void execute() {
        Mine m = Mines.i.mm.getMine(args[1]);
        if (m == null) {
            sender.sendMessage(MessageUtil.get("mines.notFound"));
            return;
        }

        sender.sendMessage(Prison.color("&7=========== &3" + m.name + " &7==========="));
        String worldName;
        if (m.worldMissing) {
            worldName = "&cWorld is missing :O";
        } else {
            worldName = "&3" + m.world.getName();
        }
        sender.sendMessage(Prison.color("&7World: " + worldName));
        sender.sendMessage(Prison.color("&7Size: &3" + ((m.maxX - m.minX) + 1) + "&8x&3" + ((m.maxZ - m.minZ) + 1) + "   &7Height: &3" + ((m.maxY - m.minY) + 1)));
        sender.sendMessage(Prison.color("&7Coordinates: &3From " + m.minX + "x," + m.minY + "y," + m.minZ + "z to " + m.maxX + "x," + m.maxY + "y," + m.maxZ + "z"));
        sender.sendMessage(Prison.color("&7Composition:"));
        for (Map.Entry<String, Block> entry : m.blocks.entrySet()) {
            if (Prison.i().im.isLoaded()) {
                sender.sendMessage(Prison.color("  &3" + (entry.getValue().getChance() * 100) + "% &7of &3" + Prison.i().im.getName(new ItemSet(entry.getValue().getId(), entry.getValue().getData()))));
            } else {
                sender.sendMessage(Prison.color("&3block  &c" + entry.getValue().getId() + ":" + entry.getValue().getData()));
            }
        }
        sender.sendMessage(Prison.color("&7================================"));
    }

    @Override
    public String description() {
        return "Get information about a mine.";
    }

}
