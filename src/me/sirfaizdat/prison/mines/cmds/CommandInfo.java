/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import java.util.Map;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.ItemManager.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
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

		sender.sendMessage(Core.colorize("&6===========&c[&2" + m.name
				+ "&c]&6==========="));
		sender.sendMessage(Core.colorize("&6World: &c" + m.world.getName()));
		sender.sendMessage(Core.colorize("&6Size: &cFrom " + m.minX + "x,"
				+ m.minY + "y," + m.minZ + "z to " + m.maxX + "x," + m.maxY
				+ "y," + m.maxZ + "z."));
		sender.sendMessage(Core.colorize("&6Composition:"));
		for (Map.Entry<String, Block> entry : m.blocks.entrySet()) {
			if (Core.i().im.isLoaded()) {
				sender.sendMessage(Core.colorize("  &6"
						+ Core.i().im.getName(new ItemSet(entry.getValue()
								.getId(), entry.getValue().getData()))));
			} else {
				sender.sendMessage(Core.colorize("  &6"
						+ entry.getValue().getId() + ":" + entry.getValue().getData()));
			}
		}
		sender.sendMessage(Core.colorize("&6================================"));
	}

	@Override
	public String description() {
		return "Get information about a mine.";
	}

}
