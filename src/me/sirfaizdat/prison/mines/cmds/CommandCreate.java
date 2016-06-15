/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines.cmds;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

import java.util.ArrayList;

/**
 * @author SirFaizdat
 */
public class CommandCreate extends Command {

    public CommandCreate() {
        super("create");
        addRequiredArg("name");
        mustBePlayer(true);
    }

    @Override
    protected void execute() {
        Selection s = Mines.i.getWE().getSelection(Prison.i().playerList.getPlayer(sender.getName()));
        if (s == null) {
            sender.sendMessage(MessageUtil.get("mines.makeWESel"));
            return;
        }
        String name = args[1];
        if (Mines.i.mm.getMine(name) != null) {
            sender.sendMessage(MessageUtil.get("mines.alreadyExists"));
            return;
        }
        String world = s.getWorld().getName();
        int minX = s.getMinimumPoint().getBlockX();
        int minY = s.getMinimumPoint().getBlockY();
        int minZ = s.getMinimumPoint().getBlockZ();
        int maxX = s.getMaximumPoint().getBlockX();
        int maxY = s.getMaximumPoint().getBlockY();
        int maxZ = s.getMaximumPoint().getBlockZ();

        int spawnY;
        if (minY < maxY) {
            spawnY = maxY;
        } else {
            spawnY = minY;
        }
        spawnY += 5;

        Mine m = new Mine(name, world, minX, minY, minZ, maxX, maxY, maxZ, new ArrayList<String>());
        m.spawnX = minX;
        m.spawnY = spawnY;
        m.spawnZ = minZ;
        m.spawnPitch = 0;
        m.spawnYaw = 0;
        try {
            Mines.i.mm.addMine(m);
            sender.sendMessage(MessageUtil.get("mines.created", m.name));
        } catch (Exception e) {
            sender.sendMessage(MessageUtil.get("mines.failedToCreate"));
            e.printStackTrace();
        }
    }

    public String description() {
        return "Creates a new mine based on your current WorldEdit selection.";
    }

}
