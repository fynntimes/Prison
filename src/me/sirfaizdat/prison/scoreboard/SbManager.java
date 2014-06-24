/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.scoreboard;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import me.sirfaizdat.prison.core.Config;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.ranks.Ranks;
import me.sirfaizdat.prison.ranks.UserInfo;
import me.sirfaizdat.prison.ranks.events.BalanceChangeEvent;
import me.sirfaizdat.prison.ranks.events.DemoteEvent;
import me.sirfaizdat.prison.ranks.events.RankupEvent;

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
		// Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.i(),
		// new Runnable() {
		//
		// @Override
		// public void run() {
		// for (PScoreboard scoreboard : scoreboards.values()) {
		// scoreboard.updateScoreboard(getScores(scoreboard
		// .getPlayer().getName()));
		// }
		// }
		// }, 0, Config.updateInterval * 1200L);
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

	@EventHandler
	public void onRankup(RankupEvent e) {
		PScoreboard board = getScoreboard(e.getPlayer().getName());
		if (board != null)
			board.updateScoreboard(getScores(board.getPlayer().getName()));
	}

	@EventHandler
	public void onDemote(DemoteEvent e) {
		PScoreboard board = getScoreboard(e.getPlayer().getName());
		if (board != null)
			board.updateScoreboard(getScores(board.getPlayer().getName()));
	}

	@EventHandler
	public void onBalanceChange(BalanceChangeEvent e) {
		PScoreboard board = getScoreboard(e.getPlayer().getName());
		if (board != null)
			board.updateScoreboard(getScores(board.getPlayer().getName()));
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

	public LinkedHashMap<String, ScoreboardEntry> getScores(String player) {
		LinkedHashMap<String, ScoreboardEntry> returnVal = new LinkedHashMap<String, ScoreboardEntry>();
		UserInfo info = Ranks.i.getUserInfo(player);
		if (info == null)
			return null;
		if (Ranks.i.isEnabled()) {
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
			double balanceD = Ranks.i.eco.getBalance(info.getPlayer());
			String balance;
			if (balanceD < 0.01) {
				balance = "&6$0.00";
			} else {
				balance = "&6$"
						+ new DecimalFormat("#,###.00").format(new BigDecimal(
								balanceD));
			}
			returnVal.put(
					"balance",
					new ScoreboardEntry(Core.colorize("&aBalance:"), Core
							.colorize(balance)));
			double amountNeededD;
			if (info.getNextRank() == null) {
				amountNeededD = 0.00;
			} else {
				amountNeededD = info.getNextRank().getPrice()
						- Ranks.i.eco.getBalance(info.getPlayer());
			}
			String amountNeeded;
			if (amountNeededD < 0.01) {
				amountNeeded = "&6$0.00";
			} else {
				amountNeeded = "&6$"
						+ new DecimalFormat("#,###.00").format(new BigDecimal(
								amountNeededD));
			}
			returnVal.put(
					"amountNeeded",
					new ScoreboardEntry(Core.colorize("&aAmount Needed:"), Core
							.colorize(amountNeeded)));
		}
		return returnVal;
	}

	public PScoreboard getScoreboard(String player) {
		return scoreboards.get(player);
	}

}
