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

package me.sirfaizdat.prison.ranks;

import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.json.JsonRank;
import me.sirfaizdat.prison.ranks.cmds.RanksCommandManager;
import me.sirfaizdat.prison.ranks.events.DemoteEvent;
import me.sirfaizdat.prison.ranks.events.RankupEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Camouflage100
 * @author SirFaizdat
 */
public class Ranks implements Component {

    public static Ranks i;
    public Economy economy;
    @Deprecated
    public List<Rank> ranks = new ArrayList<>();
    private File ranksFolder;
    private Permission permissions;
    private boolean enabled = true;

    // List of permission plugins that we aren't supporting!
    private List<String> denyPermission = Arrays.asList("superperms");

    @Override
    public String getName() {
        return "Ranks";
    }

    @Override
    public String getBaseCommand() {
        return "prisonranks";
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void reload() {
        this.getRanks().clear();
        load();
    }

    @Override
    public void enable() throws FailedToStartException {
        i = this;

        permissions = Prison.i().getPermissions();
        if (permissions == null || denyPermission.contains(permissions.getName().toLowerCase())) {
            Prison.l.severe("No permissions plugin found (such as PermissionsEx). You must have one in order to start ranks!");
            setEnabled(false);
            throw new FailedToStartException("No permission plugin found.");
        }

        economy = Prison.i().getEconomy();
        if (economy == null) {
            Prison.l.severe("No economy plugin found (such as Essentials or iConomy). You must have one in order to start ranks.");
            setEnabled(false);
            throw new FailedToStartException("No economy plugin found.");
        }

        ranksFolder = new File(Prison.i().getDataFolder(), "/ranks");
        if (!ranksFolder.exists()) {
            if (!ranksFolder.mkdirs()) {
                Prison.l.severe("Failed to generate ranks folder. Will not load ranks.");
                throw new FailedToStartException("Failed to generate ranks folder.");
            }
        }

        if(Prison.i().config.useJson){convertRanks();}

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

    @Override
    public void disable() {
        getRanks().clear();
    }

    private boolean load() {

        File[] files = ranksFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(Prison.i().config.useJson ? ".json" : ".rank");
            }
        });

        // Make sure that we have files to look at :P
        assert files != null;
        for (File file : files) {
            Rank rank = new Rank();
            if (Prison.i().config.useJson) {
                JsonRank jr = new JsonRank();
                Gson gson = new Gson();
                try {
                    gson.fromJson(new FileReader(file), JsonRank.class);
                } catch (FileNotFoundException e) {
                    // Don't even know how this is even possible
                    Prison.l.info("Couldn't load rank file " + file.getName() + " because it no longer exists");
                    continue;
                }
                rank.setId(jr.id);
                rank.setName(jr.name);
                rank.setPrefix(jr.prefix);
                rank.setPrice(jr.price);
            }
            else {
                SerializableRank jr = new SerializableRank();
                rank.setId(jr.id);
                rank.setName(jr.name);
                rank.setPrefix(jr.prefix);
                rank.setPrice(jr.price);
            }
            this.getRanks().add(rank);
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public List<Rank> getRanks() {
        return this.ranks;
    }

    public boolean addRank(Rank rank) {
        if (isLoadedRank(rank.getName())) {
            return false;
        }
        rank.setId(this.getRanks().size());
        this.getRanks().add(rank);
        return saveRank(rank);
    }

    public boolean saveRank(Rank rank) {
        File rankFile = new File(ranksFolder, rank.getName() + (Prison.i().config.useJson ? ".json" : ".rank"));
        if (Prison.i().config.useJson) {
            JsonRank jr = new JsonRank();

            jr.id = rank.getId();
            jr.name = rank.getName();
            jr.prefix = rank.getPrefix();
            jr.price = rank.getPrice();

            if (rankFile.exists()) {
                if (!rankFile.delete()) {
                    Prison.l.severe("Failed to save file " + rankFile.getName() + " - Could not delete existing copy.");
                    return false;
                }
            }

            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jr, new FileWriter(rankFile));
            } catch (IOException e) {
                Prison.l.severe("Failed to save rank " + rank.getName() + ".");
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            SerializableRank jr;
            try {
                FileInputStream fileIn = new FileInputStream(rankFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                jr = (SerializableRank) in.readObject();
                in.close();
                fileIn.close();
            } catch (ClassNotFoundException e) {
                Prison.l.severe("An unexpected error occurred. Check to make sure your copy of the plugin is not corrupted.");
                return false;
            } catch (IOException e) {
                Prison.l.warning("There was an error in loading the file " + rankFile.getName());
                e.printStackTrace();
                return false;
            }
            jr.id = rank.getId();
            jr.name = rank.getName();
            jr.prefix = rank.getPrefix();
            jr.price = rank.getPrice();
            return true;
        }
        return true;
    }

    private void convertRanks() {

        File[] files = ranksFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rank");
            }
        });

        // Make sure that we have files to look at :P
        assert files != null;
        for (File file : files) {
            SerializableRank sr;
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                sr = (SerializableRank) in.readObject();
                in.close();
                fileIn.close();
            } catch (ClassNotFoundException e) {
                Prison.l.severe("An unexpected error occurred. Check to make sure your copy of the plugin is not corrupted.");
                return;
            } catch (IOException e) {
                Prison.l.warning("There was an error in loading the file " + file.getName());
                e.printStackTrace();
                continue;
            }

            Rank rank = new Rank();
            rank.setId(sr.id);
            rank.setName(sr.name);
            rank.setPrefix(sr.prefix);
            rank.setPrice(sr.price);

            file.renameTo(new File(Prison.i().getDataFolder(),"/mines/"+file.getName().replace(".rank", ".oldrank")));
            saveRank(rank);
        }

    }

    public boolean removeRank(Rank rank) {
        if (!isLoadedRank(rank.getName())) {
            return false;
        }

        for (int i = 0; i < this.getRanks().size(); i++) {
            Rank r = this.getRanks().get(i);
            if (r.getName().equalsIgnoreCase(rank.getName())) {
                this.getRanks().remove(i);
            }
        }

        File rankFile = new File(ranksFolder, rank.getName() + ".json");
        if (rankFile.exists()) {
            boolean successful = rankFile.delete();
            if (!successful) {
                Prison.l.warning("Failed to delete rank file" + rank.getName() + ".");
                return true;
            }
        }

        return true;
    }

    public void promote(String name, boolean pay) {
        if (this.getRanks().size() == 0) {
            Prison.i().playerList.getPlayer(name).sendMessage(MessageUtil.get("ranks.noRanksLoaded"));
            return;
        }
        Rank currentRank;
        Rank nextRank;

        UserInfo info = getUserInfo(name);
        if (info != null) {
            currentRank = info.getCurrentRank();
            nextRank = info.getNextRank();

            if (nextRank == null && currentRank == null) {
                nextRank = this.getRanks().get(0);
            }
            if (nextRank == null) {
                info.getPlayer().sendMessage(pay ? MessageUtil.get("ranks.highestRank") : MessageUtil.get("ranks.highestRankOther"));
                return;
            }
            if (!isRank(nextRank.getName())) {
                info.getPlayer().sendMessage(MessageUtil.get("ranks.notAGroup"));
                return;
            }
            boolean paid = true;
            if (pay) {
                if (nextRank.getPrice() != 0) {
                    if (economy.has(info.getPlayer(), nextRank.getPrice())) {
                        economy.withdrawPlayer(info.getPlayer(), nextRank.getPrice());
                    } else {
                        if (info.getPlayer() != null) {
                            double amountNeededD = nextRank.getPrice() - economy.getBalance(info.getPlayer());
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
                if (Prison.i().config.fireworksOnRankup) launchFireworks(info.getPlayer());
                // End firework code
                Bukkit.getServer().getPluginManager().callEvent(new RankupEvent(info.getPlayer(), pay));
            }
        }
    }

    public void demote(CommandSender sender, String target, boolean refund) {
        if (getRanks().size() == 0) {
            Prison.i().playerList.getPlayer(target).sendMessage(MessageUtil.get("ranks.noRanksLoaded"));
            return;
        }

        Rank currentRank;
        Rank previousRank;

        UserInfo info = getUserInfo(target);
        if (info != null) {
            currentRank = info.getCurrentRank();
            previousRank = info.getPreviousRank();
            if (previousRank == null) {
                sender.sendMessage(MessageUtil.get("ranks.lowestRank"));
                return;
            }

            if (refund)
                economy.depositPlayer(sender.getName(), previousRank.getPrice());

            changeRank(info.getPlayer(), currentRank, previousRank);
            info.getPlayer().sendMessage(MessageUtil.get("ranks.demoteSuccess", info.getPlayer().getName(), previousRank.getPrefix()));
            sender.sendMessage(MessageUtil.get("ranks.demoteSuccess", info.getPlayer().getName(), previousRank.getPrefix()));
            Bukkit.getServer().getPluginManager().callEvent(new DemoteEvent(info.getPlayer()));
        } else {
            sender.sendMessage(MessageUtil.get("ranks.notAPlayer"));
        }
    }

    private String getGroup(String worldName, OfflinePlayer player) {
        String[] groups = permissions.getPlayerGroups(worldName, player);

        for (String group : groups) {
            for (Rank rank : this.getRanks())
                if (group.equals(rank.getName())) return group;
        }

        return null;
    }

    public void changeRank(OfflinePlayer player, Rank currentRank, Rank newRank) {
        if (Prison.i().config.worlds.size() == 0 || !Prison.i().config.enableMultiworld) {
            permissions.playerAddGroup(null, player, newRank.getName());
            if (currentRank != null) {
                permissions.playerRemoveGroup(null, player, currentRank.getName());
            }
            return;
        }

        for (String world : Prison.i().config.worlds) {
            if (Prison.i().getServer().getWorld(world) != null) {
                permissions.playerAddGroup(world, player, newRank.getName());
                if (currentRank != null) {
                    permissions.playerRemoveGroup(world, player, currentRank.getName());
                }
            } else {
                Prison.l.warning("One of the worlds specified in the ranks multiworld configuration does not exist. It has been ignored.");
            }
        }
    }

    public UserInfo getUserInfo(String playerName) {
        UserInfo info = null;
        Player player = Prison.i().playerList.getPlayer(playerName);

        if (player != null) {
            info = new UserInfo();
            info.setPlayer(player);

            Rank currentRank = null;
            Rank previousRank = null;
            Rank nextRank = null;

            for (Rank rank : this.getRanks()) {
                String group = getGroup(player.getWorld().getName(), player);
                if (rank.getName().equals(group)) {
                    currentRank = rank;
                    break;
                }
            }

            if (currentRank != null) {
                previousRank = getRankById(currentRank.getId() - 1);
                nextRank = getRankById(currentRank.getId() + 1);
            }

            info.setCurrentRank(currentRank);
            info.setPreviousRank(previousRank);
            info.setNextRank(nextRank);
        }

        return info;
    }

    public boolean isLoadedRank(String rankName) {
        try {
            if (this.getRanks().size() < 1) return false;

            for (Rank rank : this.getRanks()) {
                if (rank == null) return false;
                if (rank.getName().equalsIgnoreCase(rankName))
                    return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean isRank(String rankName) {
        for (String groupName : permissions.getGroups())
            if (groupName.equalsIgnoreCase(rankName)) return true;
        return false;
    }

    public Rank getRank(String rankName) {
        for (Rank rank : this.getRanks())
            if (rank.getName().equalsIgnoreCase(rankName)) return rank;
        return null;
    }

    public Rank getRankById(int id) {
        for (Rank rank : this.getRanks()) if (rank.getId() == id) return rank;
        return null;
    }

    // Start Fireworks
    private void launchFireworks(Player p) {
        //Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Our random generator
        Random r = new Random();

        //Get the type
        int rt = r.nextInt(5) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colors
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }
    // End Fireworks

}
