/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * @author SirFaizdat
 */
public class CommandRedefine extends Command {

	public CommandRedefine() {
		super("redefine");
		addRequiredArg("mine");
	}

	@Override
	protected void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if(m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		Selection s = Mines.i.getWE().getSelection(Prison.i().playerList.getPlayer(sender.getName()));
		if(s == null) {
			sender.sendMessage(MessageUtil.get("mines.makeWESel"));
			return;
		}
		m.minX = s.getMinimumPoint().getBlockX();
		m.minY = s.getMinimumPoint().getBlockY();
		m.minZ = s.getMinimumPoint().getBlockZ();
		m.maxX = s.getMaximumPoint().getBlockX();
		m.maxY = s.getMaximumPoint().getBlockY();
		m.maxZ = s.getMaximumPoint().getBlockZ();
		m.save();
		Mines.i.mm.mines.remove(args[1]);
		Mines.i.mm.addMine(m);
	}

	@Override
	public String description() {
		return "Redefine a mine's area.";
	}
	
	

	
}
