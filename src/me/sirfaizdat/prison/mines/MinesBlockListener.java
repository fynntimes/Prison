package me.sirfaizdat.prison.mines;

import me.sirfaizdat.prison.ranks.Ranks;
import me.sirfaizdat.prison.ranks.UserInfo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.google.common.primitives.Booleans;

public class MinesBlockListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().hasPermission("prison.mines.bypassprotection")) {
			return;
		}
		for (Mine m : Mines.i.mm.getMines().values()) {
			if (m.withinMine(e.getPlayer().getLocation())) {
				if (m.ranks == null) break;
				if (m.ranks.size() < 1) break;
				UserInfo info = Ranks.i.getUserInfo(e.getPlayer().getName());
				if (info == null) break;
				boolean[] conditions = new boolean[m.ranks.size()];
				for (int i = 0; i < m.ranks.size(); i++) {
					conditions[i] = info.getCurrentRank().getName().equals(m.ranks.get(i));
				}
				if (!Booleans.contains(conditions, true)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().hasPermission("prison.mines.bypassprotection")) {
			return;
		}
		for (Mine m : Mines.i.mm.getMines().values()) {
			if (m.withinMine(e.getPlayer().getLocation())) {
				if (m.ranks == null) break;
				if (m.ranks.size() < 1) break;
				UserInfo info = Ranks.i.getUserInfo(e.getPlayer().getName());
				if (info == null) break;
				boolean[] conditions = new boolean[m.ranks.size()];
				for (int i = 0; i < m.ranks.size(); i++) {
					conditions[i] = info.getCurrentRank().getName().equals(m.ranks.get(i));
				}
				if (!Booleans.contains(conditions, true)) {
					e.setCancelled(true);
				}
			}
		}
	}

}
