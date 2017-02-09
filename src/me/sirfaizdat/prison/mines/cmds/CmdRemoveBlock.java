/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.sirfaizdat.prison.mines.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.ItemManager.ItemSet;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Block;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.mines.entities.Mine;
import org.bukkit.Material;

/**
 * @author SirFaizdat
 */
public class CmdRemoveBlock extends Command {

    public CmdRemoveBlock() {
        super("removeblock");
        addRequiredArg("mine");
        addRequiredArg("block");
    }

    @SuppressWarnings("deprecation") public void execute() {
        if (!Prison.i().im.isLoaded()) {
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

            Integer id;
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
                    sender.sendMessage(MessageUtil.get("mines.mineDoesntHaveThisBlock"));
                    return;
                }
            } catch (NullPointerException ignored) {
            }
            set = new ItemSet(mat.getId(), b);
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
                if (m.blocks.get(block.toString()) == null) {
                    sender.sendMessage(MessageUtil.get("mines.mineDoesntHaveThisBlock"));
                    return;
                }
            } catch (NullPointerException ignored) {
            }

        }

        // End block recognition

        m.blocks.remove(block.toString());
        m.save();
        if (Prison.i().im.isLoaded()) {
            sender.sendMessage(
                MessageUtil.get("mines.removedBlock", Prison.i().im.getName(set), m.name));
        } else {
            sender.sendMessage(
                MessageUtil.get("mines.removedBlock", set.id + ":" + set.data, m.name));
        }
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
