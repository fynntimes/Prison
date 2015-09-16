package me.sirfaizdat.prison.mines.cmds;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.mines.Mine;
import me.sirfaizdat.prison.mines.Mines;

/**
 * @author SirFaizdat
 */
public class CommandSetSpawn extends Command {

	public CommandSetSpawn() {
		super("setspawn");
		addRequiredArg("mine");
		mustBePlayer(true);
	}

	@Override
	protected void execute() {
		Mine m = Mines.i.mm.getMine(args[1]);
		if (m == null) {
			sender.sendMessage(MessageUtil.get("mines.notFound"));
			return;
		}
		if(m.worldMissing) {
			sender.sendMessage(MessageUtil.get("mines.missingWorld"));
			return;
		}
		Player p = (Player) sender;
		Location pLoc = p.getLocation();
		if(!pLoc.getWorld().getName().equals(m.world.getName())) {
			sender.sendMessage(MessageUtil.get("mines.wrongWorld", m.world.getName()));
			return;
		}
		m.spawnX = pLoc.getBlockX();
		m.spawnY = pLoc.getBlockY();
		m.spawnZ = pLoc.getBlockZ();
		m.save();
		sender.sendMessage(MessageUtil.get("mines.setSpawn"));
	}

	@Override
	public String description() {
		return "Sets the mine's spawn.";
	}

}
