/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import java.util.Map;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.ItemManager.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

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

		sender.sendMessage(Prison.colorize("&6===========&c[&2" + m.name + "&c]&6==========="));
		String worldName = null;
		if (m.worldMissing) {
			worldName = "&4ERROR: &cWorld is missing!";
		} else {
			worldName = "&c" + m.world.getName();
		}
		sender.sendMessage(Prison.colorize("&6World: " + worldName));
		sender.sendMessage(Prison.colorize("&6Size: &c" + ((m.maxX - m.minX) + 1) + "&6x&c" + ((m.maxZ - m.minZ) + 1) + "   &6Height: &c" + ((m.maxY - m.minY) + 1)));
		sender.sendMessage(Prison.colorize("&6Coordinates: &cFrom " + m.minX + "x," + m.minY + "y," + m.minZ + "z to " + m.maxX + "x," + m.maxY + "y," + m.maxZ + "z."));
		sender.sendMessage(Prison.colorize("&6Composition:"));
		for (Map.Entry<String, Block> entry : m.blocks.entrySet()) {
			if (Prison.i().im.isLoaded()) {
				sender.sendMessage(Prison.colorize("  &c" + (entry.getValue().getChance() * 100) + "% &6of &c" + Prison.i().im.getName(new ItemSet(entry.getValue().getId(), entry.getValue().getData()))));
			} else {
				sender.sendMessage(Prison.colorize("&6block  &c" + entry.getValue().getId() + ":" + entry.getValue().getData()));
			}
		}
		sender.sendMessage(Prison.colorize("&6Associated Ranks:"));
		StringBuilder sb = new StringBuilder();
		for (String rank : m.ranks) {
			sb.append("   &c" + rank + "\n");
		}
		String returnVal = sb.toString();
		sender.sendMessage(Prison.colorize(returnVal));
		sender.sendMessage(Prison.colorize("&6================================"));
	}

	@Override
	public String description() {
		return "Get information about a mine.";
	}

}
