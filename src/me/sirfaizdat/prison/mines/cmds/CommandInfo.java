/**
  	Copyright (C) 2014 SirFaizdat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
