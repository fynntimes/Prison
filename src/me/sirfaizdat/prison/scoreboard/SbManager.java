/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.scoreboard;

import java.util.HashMap;
import java.util.Map;

import me.sirfaizdat.prison.core.Config;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Ranks;
import me.sirfaizdat.prison.ranks.UserInfo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author SirFaizdat
 */
public class SbManager implements Listener {

	ScoreboardManager manager;

	Map<String, PScoreboard> scoreboards;

	public SbManager() {
		this.manager = Bukkit.getScoreboardManager();
		this.scoreboards = new HashMap<String, PScoreboard>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getScoreboard(p.getName()) != null) {
				return;
			}
			createScoreboard(p.getName());
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, Core.i());

		// Refresh
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.i(),
				new Runnable() {

					@Override
					public void run() {
						for (PScoreboard scoreboard : scoreboards.values()) {
							scoreboard.updateScoreboard(getScores(scoreboard
									.getPlayer().getName()));
						}
					}
				}, 0, Config.updateInterval * 1200L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (getScoreboard(e.getPlayer().getName()) != null) {
			return;
		}
		createScoreboard(e.getPlayer().getName());
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (getScoreboard(e.getPlayer().getName()) == null) {
			return;
		}
		e.getPlayer().setScoreboard(manager.getNewScoreboard());
		scoreboards.remove(e.getPlayer().getName());
	}

	public void createScoreboard(String player) {
		if (getScoreboard(player) != null)
			return;
		PScoreboard sb = new PScoreboard(manager.getNewScoreboard(),
				Core.i().playerList.getPlayer(player));
		generate(sb);
	}

	public void generate(PScoreboard sb) {
		UserInfo info = Ranks.i.getUserInfo(sb.getPlayer().getName());
		if (info == null)
			return;
		scoreboards.put(sb.getPlayer().getName(), sb);
		sb.generateScoreboard(getScores(sb.getPlayer().getName()));
	}

	public HashMap<String, ScoreboardEntry> getScores(String player) {
		HashMap<String, ScoreboardEntry> returnVal = new HashMap<String, ScoreboardEntry>();
		UserInfo info = Ranks.i.getUserInfo(player);
		if (info == null)
			return null;
		if (Mines.i.isEnabled()) {
			returnVal.put(
					"resetTime",
					new ScoreboardEntry(Core.colorize("&aNext reset:"), Core
							.colorize("&6" + Mines.i.mm.resetTimeCounter
									+ "mins")));
		}
		if (Ranks.i.isEnabled()) {
			String nextRankVal;
			if (info.getNextRank() == null) {
				if (Ranks.i.ranks.size() == 0) {
					nextRankVal = "&r&cNone";
				} else {
					nextRankVal = "&6Highest";
				}
			} else {
				nextRankVal = info.getNextRank().getPrefix();
			}
			returnVal.put(
					"nextRank",
					new ScoreboardEntry(Core.colorize("&aNext rank:"), Core
							.colorize(nextRankVal)));
			String currentRankVal;
			if (info.getCurrentRank() == null) {
				currentRankVal = "&cNone";
			} else {
				currentRankVal = info.getCurrentRank().getPrefix();
			}
			returnVal.put(
					"currentRank",
					new ScoreboardEntry(Core.colorize("&aCurrent rank:"), Core
							.colorize(currentRankVal)));
		}
		return returnVal;
	}

	public PScoreboard getScoreboard(String player) {
		return scoreboards.get(player);
	}

}
