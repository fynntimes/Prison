/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import org.bukkit.Material;

/**
 * @author SirFaizdat
 */
public class CommandRemoveBlock extends Command {

	public CommandRemoveBlock() {
		super("removeblock");
		addRequiredArg("mine");
		addRequiredArg("block");
	}

	//
	// public void execute() {
	// Mine m = Mines.i.mm.getMine(args[1]);
	// if (m == null) {
	// sender.sendMessage(MessageUtil.get("mines.notFound"));
	// return;
	// }
	// String passedBlockID = args[2];
	// if (!passedBlockID.contains(":")) {
	// passedBlockID = passedBlockID + ":0";
	// }
	// Block b = m.blocks.get(passedBlockID);
	// if (b == null) {
	// sender.sendMessage(MessageUtil.get("mines.mineDoesntHaveThisBlock"));
	// return;
	// }
	// m.blocks.remove(b);
	// m.blocks.remove(passedBlockID);
	// m.save();
	// sender.sendMessage(MessageUtil.get("mines.removedBlock", passedBlockID,
	// m.name));
	// }

	public void execute() {
		if (!Core.i().im.isLoaded()) {
			sender.sendMessage(MessageUtil.get("general.itemManagerNotLoaded"));
			return;
		}
		Mine m = Mines.i.mm.getMine(args[1]);
		if (m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}

		Block block;
		ItemSet set;
		if (isAnInt(args[2].replaceAll(":", ""))) {
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
				if (m.blocks.get(block.toString()) == null) {
					sender.sendMessage(MessageUtil
							.get("mines.mineDoesntHaveThisBlock"));
					return;
				}
			} catch (NullPointerException e) {
			}
			set = new ItemSet(mat.getId(), b);
			// End "IF IT IS AN ID"
		} else {
			// Begin "IF IT IS A WORD"
			set = Core.i().im.getItem(args[2].toLowerCase());
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
			byte data = set.data;
			block = new Block(mat.getId(), data);
			try {
				if (m.blocks.get(block.toString()) == null) {
					sender.sendMessage(MessageUtil
							.get("mines.mineDoesntHaveThisBlock"));
					return;
				}
			} catch (NullPointerException e) {
			}

		}

		// End block recognition

		m.blocks.remove(block.toString());
		m.save();
		sender.sendMessage(MessageUtil.get("mines.removedBlock",
				Core.i().im.getName(set), m.name));
	}

	public boolean isAnInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public String description() {
		return "Removes a block from a mine.";
	}

}
