/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.ranks;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.ranks.cmds.RanksCommandManager;
import me.sirfaizdat.prison.ranks.events.DemoteEvent;
import me.sirfaizdat.prison.ranks.events.RankupEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the ranks component.
 *
 * @author SirFaizdat
 */
public class Ranks implements Component {

    public static Ranks i;
    public Economy eco;
    public List<Rank> ranks = new ArrayList<Rank>();
    public File rankFolder;
    Permission permission;
    private boolean enabled = true;

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

        permission = Prison.i().getPermissions();
        eco = Prison.i().getEconomy();

        rankFolder = new File(Prison.i().getDataFolder(), "/ranks");
        if (!rankFolder.exists()) {
            if (!rankFolder.mkdirs()) {
                Prison.l.severe("Failed to generate ranks folder. Will not load ranks.");
                throw new FailedToStartException("Failed to generate ranks folder.");
            }
        }

        load();
        RanksCommandManager rcm = new RanksCommandManager();
        Prison.i().getCommand("prisonranks").setExecutor(rcm);
        Prison.i().getCommand("ranks").setExecutor(rcm);
        Prison.i().getCommand("rankup").setExecutor(rcm);

        Bukkit.getScheduler().runTaskLater(Prison.i(), new Runnable() {
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

    private boolean load() {
        File oldRanksListFile = new File(rankFolder, "ranksList.txt");
        if (oldRanksListFile.exists()) oldRanksListFile.delete();

        int offsetCounter = -1;
        boolean needsSave = false;
        File[] files = rankFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rank");
            }
        });
        for (File file : files) {
            SerializableRank sr;
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                sr = (SerializableRank) in.readObject();
                in.close();
                fileIn.close();
            } catch (ClassNotFoundException e) {
                Prison.l.severe("An unexpected error occured. Check to make sure your copy of the plugin is not corrupted.");
                return false;
            } catch (IOException e) {
                Prison.l.warning("There was an error in loading the file " + file.getName());
                e.printStackTrace();
                continue;
            }

            Rank rank = new Rank();
            offsetCounter++;
            if (sr.id == 0 && getRankById(0) != null) {
                sr.id = offsetCounter;
                needsSave = true;
            }
            Prison.l.info("Rank " + sr.name + " ID: " + sr.id);
            rank.setId(sr.id);
            rank.setName(sr.name);
            rank.setPrefix(sr.prefix);
            rank.setPrice(sr.price);
            ranks.add(rank);
            if (needsSave) {
                saveRank(rank);
                needsSave = false;
            }
        }

        return true;
    }

    private String getGroup(String world, Player player) {

        String[] groups = permission.getPlayerGroups(world, player);

        for (String group : groups) {
            for (Rank rank : ranks) {
                if (group.equals(rank.getName())) {
                    return group;
                }
            }
        }

        return null;
    }

    public UserInfo getUserInfo(String name) {
        UserInfo info = null;
        Player player = Prison.i().playerList.getPlayer(name);
        if (player != null) {
            info = new UserInfo();
            info.setPlayer(player);

            Rank currentRank = null;
            Rank previousRank;
            Rank nextRank;

//            for (Rank rank : ranks) {
//                String primaryGroup = getGroup(player.getWorld().getName(), player);
//                if (primaryGroup != null) {
//                    if (currentRank != null) {
//                        nextRank = rank;
//                        break;
//                    }
//
//                    if (primaryGroup.equalsIgnoreCase(rank.getName())) {
//                        currentRank = rank;
//                    }
//
//                    if (currentRank == null) {
//                        previousRank = rank;
//                    }
//                } else {
//                    nextRank = ranks.get(0);
//                }
//            }
//
//            if (previousRank != null && currentRank == null) {
//                previousRank = null;
//            }

            for (Rank rank : ranks) {
                String group = getGroup(player.getWorld().getName(), player);
                if (rank.getName().equals(group)) {
                    currentRank = rank;
                    break;
                }
            }

            previousRank = getRankById(currentRank.getId() - 1);
            nextRank = getRankById(currentRank.getId() + 1);

            info.setCurrentRank(currentRank);
            info.setPreviousRank(previousRank);
            info.setNextRank(nextRank);
        }
        return info;
    }

    public void promote(String name, boolean buy) {
        if (ranks.size() == 0) {
            Prison.i().playerList.getPlayer(name).sendMessage(MessageUtil.get("ranks.noRanksLoaded"));
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
                info.getPlayer().sendMessage(buy ? MessageUtil.get("ranks.highestRank") : MessageUtil.get("ranks.highestRankOther"));
                return;
            }
            if (!isRank(nextRank.getName())) {
                info.getPlayer().sendMessage(MessageUtil.get("ranks.notAGroup"));
                return;
            }
            if (nextRank != null) {
                boolean paid = true;
                if (buy) {
                    if (nextRank.getPrice() != 0) {
                        if (eco.has(info.getPlayer(), nextRank.getPrice())) {
                            eco.withdrawPlayer(info.getPlayer(), nextRank.getPrice());
                        } else {
                            if (info.getPlayer() != null) {
                                double amountNeededD = nextRank.getPrice() - eco.getBalance(info.getPlayer());
                                String amountNeeded = new DecimalFormat("#,###.00").format(new BigDecimal(amountNeededD));
                                info.getPlayer().sendMessage(MessageUtil.get("ranks.notEnoughMoney", amountNeeded, nextRank.getPrefix()));
                                paid = false;
                            }
                        }
                    }
                }
                if (paid) {
                    changeRank(info.getPlayer(), currentRank, nextRank);
                    info.getPlayer().sendMessage(MessageUtil.get("ranks.rankedUp", nextRank.getPrefix()));
                    Bukkit.broadcastMessage(MessageUtil.get("ranks.rankedUpBroadcast", info.getPlayer().getName(), nextRank.getPrefix()));
                    // Launch a firework! Yay!
                    Firework fw = info.getPlayer().getWorld().spawn(info.getPlayer().getLocation(), Firework.class);
                    FireworkMeta data = fw.getFireworkMeta();
                    data.addEffects(FireworkEffect.builder().withColor(Color.BLUE).with(Type.BALL_LARGE).build());
                    data.setPower(3);
                    fw.setFireworkMeta(data);
                    fw.detonate();
                    // End firework code
                    Bukkit.getServer().getPluginManager().callEvent(new RankupEvent(info.getPlayer(), buy));
                }
            }
        }
    }

    public void demote(Player sender, String name) {
        if (ranks.size() == 0) {
            Prison.i().playerList.getPlayer(name).sendMessage(MessageUtil.get("ranks.noRanksLoaded"));
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
            info.getPlayer().sendMessage(MessageUtil.get("ranks.demoteSuccess", info.getPlayer().getName(), previousRank.getPrefix()));
            sender.sendMessage(MessageUtil.get("ranks.demoteSuccess", info.getPlayer().getName(), previousRank.getPrefix()));
            Bukkit.getServer().getPluginManager().callEvent(new DemoteEvent(info.getPlayer()));
        } else {
            sender.sendMessage(MessageUtil.get("ranks.notAPlayer"));
        }
    }

    public boolean addRank(Rank rank) {
        if (isLoadedRank(rank.getName())) {
            return false;
        }
        rank.setId(ranks.size());
        ranks.add(rank);
        return saveRank(rank);
    }

    public boolean saveRank(Rank rank) {
        File rankFile = new File(rankFolder, rank.getName() + ".rank");
        SerializableRank sr = new SerializableRank();
        sr.id = rank.getId();
        sr.name = rank.getName();
        sr.prefix = rank.getPrefix();
        sr.price = rank.getPrice();
        if (rankFile.exists()) {
            if (!rankFile.delete()) {
                Prison.l.severe("Failed to save file " + rankFile.getName() + " - Could not delete existing copy.");
                return false;
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(rankFile);
            ObjectOutputStream oOut = new ObjectOutputStream(out);
            oOut.writeObject(sr);
            oOut.close();
            out.close();
        } catch (FileNotFoundException e) {
            Prison.l.severe("Failed to save rank " + rank.getName() + ".");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Prison.l.severe("Failed to save rank " + rank.getName() + ".");
            e.printStackTrace();
            return false;
        }
        return true;
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
        File rankFile = new File(rankFolder, rank.getName() + ".rank");
        if (rankFile.exists()) {
            boolean successful = rankFile.delete();
            if (!successful) {
                // Won't effect the plugin, as long as rankFile.txt successfully
                // removes the rank from it's list.
                Prison.l.warning("Failed to delete rank file" + rank.getName() + ".");
                return true;
            }
        }

        return true;
    }

    public void changeRank(Player player, Rank currentRank, Rank newRank) {
        if (Prison.i().config.rankWorlds.size() == 0 || !Prison.i().config.enableMultiworld) {
            permission.playerAddGroup(null, player, newRank.getName());
            if (currentRank != null) {
                permission.playerRemoveGroup(null, player, currentRank.getName());
            }
            return;
        }
        for (String world : Prison.i().config.rankWorlds) {
            if (Prison.i().getServer().getWorld(world) != null) {
                permission.playerAddGroup(world, player, newRank.getName());
                if (currentRank != null) {
                    permission.playerRemoveGroup(world, player, currentRank.getName());
                }
            } else {
                Prison.l.warning("One of the worlds specified in the ranks multiworld configuration does not exist. It has been ignored.");
            }
        }
    }

    public boolean isLoadedRank(String rankName) {
        try {
            if (ranks.size() < 1) {
                return false;
            }
            for (int i = 0; i < ranks.size(); i++) {
                if (ranks.get(i) == null) return false;
                if (ranks.get(i).getName().equalsIgnoreCase(rankName)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
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

    /**
     * Returns null if no rank was found.
     */
    public Rank getRank(String r) {
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i).getName().equalsIgnoreCase(r)) {
                return ranks.get(i);
            }
        }
        return null;
    }

    public Rank getRankById(int id) {
        for (Rank rank : ranks) if (rank.getId() == id) return rank;
        return null;
    }

    @Override
    public String getBaseCommand() {
        return "prisonranks";
    }

}
