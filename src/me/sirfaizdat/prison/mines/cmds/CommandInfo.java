/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import java.util.Map;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import org.bukkit.Material;

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
			Material blockMat = Material.getMaterial(entry.getValue().getId());
			if (entry.getValue().getData() != 0) {
				sender.sendMessage(Core.colorize("  &6"
						+ blockMat.toString().toLowerCase() + ":"
						+ entry.getValue().getData()));
			} else {
				sender.sendMessage(Core.colorize("  &6"
						+ blockMat.toString().toLowerCase()));
			}
		}
		sender.sendMessage(Core.colorize("&6================================"));
	}

	@Override
	public String description() {
		return "Get information about a mine.";
	}

}
