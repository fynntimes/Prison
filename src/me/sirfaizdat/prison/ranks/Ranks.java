/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.Core;
import me.sirfaizdat.prison.core.FailedToStartException;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.ranks.cmds.RanksCommandManager;
import me.sirfaizdat.prison.ranks.events.DemoteEvent;
import me.sirfaizdat.prison.ranks.events.RankupEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Manages the ranks component.
 * 
 * @author SirFaizdat
 */
public class Ranks implements Component {

	private boolean enabled = true;

	public static Ranks i;

	RanksConfig conf;
	Permission permission;
	public Economy eco;

	public List<Rank> ranks = new ArrayList<Rank>();

	public String getName() {
		return "Ranks";
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() throws FailedToStartException {
		i = this;
		conf = new RanksConfig();
		conf.saveDefaultConfig();

		permission = Core.i().getPermissions();
		eco = Core.i().getEconomy();

		load();
		RanksCommandManager rcm = new RanksCommandManager();
		Core.i().getCommand("prisonranks").setExecutor(rcm);
		Core.i().getCommand("ranks").setExecutor(rcm);
		Core.i().getCommand("rankup").setExecutor(rcm);

		Bukkit.getScheduler().runTaskLater(Core.i(), new Runnable() {
			@Override
			public void run() {
				new BalanceChangeListener();
			}
		}, 0);
		
	}

	public void reload() {
		ranks.clear();
		load();
	}

	public void disable() {
		ranks.clear();
	}

	private void load() {
		List<String> rankList = conf.getConfig().getStringList("ranklist");

		int count = 0;
		for (String s : rankList) {
			if (isRank(s)) {
				Rank rank = new Rank();
				rank.setId(count);
				rank.setName(s);
				rank.setPrice(conf.getConfig().getDouble(
						"ranks." + s + ".price"));
				rank.setPrefix(conf.getConfig().getString(
						"ranks." + s + ".prefix"));
				ranks.add(rank);
				count++;
			}
		}
		Core.l.info("&2Loaded " + count + " ranks.");
	}

	public UserInfo getUserInfo(String name) {
		UserInfo info = null;
		Player player = Core.i().playerList.getPlayer(name);
		if (player != null) {
			info = new UserInfo();
			info.setPlayer(player);

			Rank currentRank = null;
			Rank previousRank = null;
			Rank nextRank = null;

			for (Rank rank : ranks) {
				String primaryGroup = permission.getPrimaryGroup(player
						.getWorld().getName(), player);
				if (currentRank != null) {
					nextRank = rank;
					break;
				}

				if (primaryGroup.equalsIgnoreCase(rank.getName())) {
					currentRank = rank;
				}

				if (currentRank == null) {
					previousRank = rank;
				}
			}

			if (previousRank != null && currentRank == null) {
				previousRank = null;
			}

			info.setCurrentRank(currentRank);
			info.setPreviousRank(previousRank);
			info.setNextRank(nextRank);
		}
		return info;
	}

	public void promote(String name, boolean buy) {
		if (ranks.size() == 0) {
			Core.i().playerList.getPlayer(name).sendMessage(
					MessageUtil.get("ranks.noRanksLoaded"));
			return;
		}
		Rank currentRank = null;
		Rank nextRank = null;

		UserInfo info = getUserInfo(name);
		if (info != null) {
			currentRank = info.getCurrentRank();
			nextRank = info.getNextRank();

			if (nextRank == null && currentRank == null) {
				nextRank = ranks.get(0);
			}
			if (nextRank == null) {
				info.getPlayer().sendMessage(
						buy ? MessageUtil.get("ranks.highestRank")
								: MessageUtil.get("ranks.highestRank.other"));
				return;
			}
			if (nextRank != null) {
				boolean paid = true;
				if (buy) {
					if (nextRank.getPrice() != 0) {
						if (eco.has(info.getPlayer(), nextRank.getPrice())) {
							eco.withdrawPlayer(info.getPlayer(),
									nextRank.getPrice());
						} else {
							if (info.getPlayer() != null) {
								double amountNeededD = nextRank.getPrice()
										- eco.getBalance(info.getPlayer());
								String amountNeeded = new DecimalFormat(
										"#,###.00").format(new BigDecimal(
										amountNeededD));
								info.getPlayer().sendMessage(
										MessageUtil.get("ranks.notEnoughMoney",
												amountNeeded,
												nextRank.getPrefix()));
								paid = false;
							}
						}
					}
				}
				if (paid) {
					changeRank(info.getPlayer(), currentRank, nextRank);
					info.getPlayer().sendMessage(
							MessageUtil.get("ranks.rankedUp",
									nextRank.getPrefix()));
					Bukkit.broadcastMessage(MessageUtil.get(
							"ranks.rankedUpBroadcast", info.getPlayer()
									.getName(), nextRank.getPrefix()));
					Bukkit.getServer().getPluginManager()
							.callEvent(new RankupEvent(info.getPlayer(), buy));
				}
			}
		}
	}

	public void demote(Player sender, String name) {
		if (ranks.size() == 0) {
			Core.i().playerList.getPlayer(name).sendMessage(
					MessageUtil.get("ranks.noRanksLoaded"));
			return;
		}
		Rank currentRank = null;
		Rank previousRank = null;

		UserInfo info = getUserInfo(name);
		if (info != null) {
			currentRank = info.getCurrentRank();
			previousRank = info.getPreviousRank();
			if (previousRank == null) {
				sender.sendMessage(MessageUtil.get("ranks.lowestRank"));
				return;
			}
			changeRank(info.getPlayer(), currentRank, previousRank);
			info.getPlayer().sendMessage(
					MessageUtil.get("ranks.demoteSuccess", info.getPlayer()
							.getName(), previousRank.getPrefix()));
			sender.sendMessage(MessageUtil.get("ranks.demoteSuccess", info
					.getPlayer().getName(), previousRank.getPrefix()));
			Bukkit.getServer().getPluginManager()
					.callEvent(new DemoteEvent(info.getPlayer()));
		} else {
			sender.sendMessage(MessageUtil.get("ranks.notAPlayer"));
		}
	}

	public boolean addRank(Rank rank) {
		if (isLoadedRank(rank.getName())) {
			return false;
		}
		rank.setId(ranks.size() + 1);
		ranks.add(rank);
		List<String> oldRankList = conf.getConfig().getStringList("ranklist");
		oldRankList.add(rank.getName());
		conf.getConfig().set("ranklist", oldRankList);
		conf.getConfig().set("ranks." + rank.getName() + ".price",
				rank.getPrice());
		conf.getConfig().set("ranks." + rank.getName() + ".prefix",
				rank.getPrefix());
		boolean success = conf.save();
		return success ? true : false;
	}

	public boolean removeRank(Rank rank) {
		if (!isLoadedRank(rank.getName())) {
			return false;
		}
		for (int i = 0; i < ranks.size(); i++) {
			Rank r = ranks.get(i);
			if (r.getName().equalsIgnoreCase(rank.getName())) {
				ranks.remove(i);
			}
		}
		List<String> oldRankList = conf.getConfig().getStringList("ranklist");
		oldRankList.remove(rank.getName());
		conf.getConfig().set("ranklist", oldRankList);
		conf.getConfig().set("ranks." + rank.getName(), null);
		boolean success = conf.save();
		return success ? true : false;
	}

	public void changeRank(Player player, Rank currentRank, Rank newRank) {
		if (Core.i().config.rankWorlds.size() == 0
				|| !Core.i().config.enableMultiworld) {
			permission.playerAddGroup(null, player, newRank.getName());
			if (currentRank != null) {
				permission.playerRemoveGroup(null, player,
						currentRank.getName());
			}
			return;
		}
		for (String world : Core.i().config.rankWorlds) {
			if (Bukkit.getWorld(world) != null) {
				permission.playerAddGroup(world, player, newRank.getName());
				if (currentRank != null) {
					permission.playerRemoveGroup(world, player,
							currentRank.getName());
				}
			} else {
				Core.l.warning("One of the worlds specified in the ranks multiworld configuration does not exist. It has been ignored.");
			}
		}
	}

	public boolean isLoadedRank(String rankName) {
		for (int i = 0; i < ranks.size(); i++) {
			if (ranks.get(i).getName().equalsIgnoreCase(rankName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRank(String rankName) {
		String[] groups = permission.getGroups();
		for (int i = 0; i < groups.length; i++) {
			String groupName = groups[i];
			if (groupName.equalsIgnoreCase(rankName)) {
				return true;
			}
		}
		return false;
	}

	/** Returns null if no rank was found. */
	public Rank getRank(String r) {
		for (int i = 0; i < ranks.size(); i++) {
			if (ranks.get(i).getName().equalsIgnoreCase(r)) {
				return ranks.get(i);
			}
		}
		return null;
	}

	@Override
	public String getBaseCommand() {
		return "prisonranks";
	}

}
