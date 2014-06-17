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
import me.sirfaizdat.prison.core.PlayerList;
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
	Economy eco;

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
	}

	private void load() {
		List<String> rankList = conf.getConfig().getStringList("ranklist");

		int count = -1;
		for (String s : rankList) {
			if (isRank(s)) {
				Rank rank = new Rank();
				rank.setId(count + 1);
				rank.setName(s);
				rank.setPrice(conf.getConfig().getDouble(
						"ranks." + s + ".price"));
				rank.setPrefix(conf.getConfig().getString(
						"ranks." + s + ".prefix"));
				ranks.add(rank);
				count++;
			}
		}
		Core.l.info("&2Loaded " + (count + 1) + " ranks.");
	}

	public UserInfo getUserInfo(String name) {
		UserInfo info = null;
		Player player = PlayerList.getPlayer(name);
		if (player != null) {
			info = new UserInfo();
			info.setPlayer(player);

			Rank currentRank = null;
			Rank previousRank = null;
			Rank nextRank = null;

			for (Rank rank : ranks) {
				String primaryGroup = permission.getPrimaryGroup(player.getWorld().getName(), player);
				if (rank.getName().equalsIgnoreCase(primaryGroup)) {
					currentRank = rank;
					if (currentRank.getId() - 1 <= ranks.size()) {
						previousRank = null;
					} else {
						previousRank = ranks.get(currentRank.getId() - 1);
					}
					if (currentRank.getId() + 1 >= ranks.size()) {
						nextRank = null;
					} else {
						nextRank = ranks.get(currentRank.getId() + 1);
					}
				}
			}
			info.setCurrentRank(currentRank);
			info.setPreviousRank(previousRank);
			info.setNextRank(nextRank);
		}
		return info;
	}

	public void promote(String name, boolean buy) {
		if (ranks.size() == 0) {
			PlayerList.getPlayer(name).sendMessage(
					MessageUtil.get("ranks.noRanksLoaded"));
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
						if (eco.has(info.getPlayer(),
								nextRank.getPrice())) {
							eco.withdrawPlayer(info.getPlayer(),
									nextRank.getPrice());
						} else {
							if (info.getPlayer() != null) {
								double amountNeededD = nextRank.getPrice()
										- eco.getBalance(info.getPlayer());
								String amountNeeded = new DecimalFormat("#,###.00").format(new BigDecimal(amountNeededD));
								info.getPlayer().sendMessage(MessageUtil.get("ranks.notEnoughMoney", amountNeeded, nextRank.getPrefix()));
								paid = false;
							}
						}
					}
				}
				if(paid) {
					changeRank(info.getPlayer(), currentRank, nextRank, info.getPlayer().getWorld().getName());
					info.getPlayer().sendMessage(MessageUtil.get("ranks.rankedUp", nextRank.getPrefix()));
					Bukkit.broadcastMessage(MessageUtil.get("ranks.rankedUpBroadcast", info.getPlayer().getName(), nextRank.getPrefix()));
				}
			}
		}
	}
	
	public boolean addRank(Rank rank) {
		if(isLoadedRank(rank.getName())) {
			return false;
		}
		rank.setId(ranks.size() + 1);
		ranks.add(rank);
		conf.getConfig().set("ranks." + rank.getName() + ".price", rank.getPrice());
		conf.getConfig().set("ranks." + rank.getName() + ".prefix", rank.getPrefix());
		boolean success = conf.save();
		if(!success) {
			return false;
		}
		return true;
	}
	
	public void changeRank(Player player, Rank currentRank, Rank newRank, String world) {
		permission.playerAddGroup(world, player, newRank.getName());
		if(currentRank != null) {
			permission.playerRemoveGroup(world, player,currentRank.getName());
		}
	}
	
	public boolean isLoadedRank(String rankName) {
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).getName().equals(rankName)) {
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

}
