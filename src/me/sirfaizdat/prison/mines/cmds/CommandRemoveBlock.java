/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

/**
 * @author SirFaizdat
 */
public class CommandRemoveBlock extends Command {

	public CommandRemoveBlock() {
		super("removeblock");
		addRequiredArg("mine");
		addRequiredArg("block");
	}

	public void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if (m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		String passedBlockID = args[2];
		if(!passedBlockID.contains(":")) {
			passedBlockID = passedBlockID + ":0";
		}
		Block b = m.blocks.get(passedBlockID);
		if (b == null) {
			sender.sendMessage(MessageUtil.get("mines.mineDoesntHaveThisBlock"));
			return;
		}
		m.blocks.remove(b);
		m.blocks.remove(passedBlockID);
		m.save();
		sender.sendMessage(MessageUtil.get("mines.removedBlock", passedBlockID, m.name));
	}

	public String description() {
		return "Removes a block from a mine.";
	}

}
