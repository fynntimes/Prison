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
package me.sirfaizdat.prison.core.cmds;

import java.net.URISyntaxException;
import java.util.logging.Level;
import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.MessageUtil;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.core.Updater;
import me.sirfaizdat.prison.three.Upgrader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/**
 * @author SirFaizdat
 */
public class CmdUpdate extends Command {

    private boolean consoleUpgradeInTime = false;

    CmdUpdate() {
        super("update");
    }

    @Override protected void execute() {
        if (Prison.i().config.checkUpdates) {
            if (Prison.i().getDescription().getVersion().contains("SNAPSHOT")) {
                sender.sendMessage(MessageUtil.get("general.devBuild"));
                return;
            }

            Updater updater = new Updater();
            updater.checkForUpdates(false).waitForCheck();
            if (updater.getUpdate().name.toLowerCase().contains("prison 3 v")){
                if (sender instanceof Player){
                    try {
                        Upgrader.upgradeForPlayer((Player)sender);
                    } catch (IOException e) {
                        sender.sendMessage("An unknown error occurred during upgrade. Please check the log");
                        Prison.i().getLogger().log(Level.SEVERE,"I/O Error during upgrade",e);
                    } catch (URISyntaxException e) {
                        sender.sendMessage("An unknown error occurred during upgrade. Please check the log");
                        Prison.i().getLogger().log(Level.SEVERE,"URL parsing error during upgrade",e);
                    }
                }else{
                    if (!consoleUpgradeInTime) {
                        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix + "&6It's best that a player starts the Prison 3 upgrade because you can choose what modules you want to install."));
                        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix + "&6To continue as console and install all modules, please type &b/prison update &6again within 10 seconds."));
                        consoleUpgradeInTime = true;
                        Bukkit.getScheduler().runTaskLater(Prison.i(), () -> consoleUpgradeInTime = false, 200);
                    }else{
                        consoleUpgradeInTime = false;
                        Upgrader.upgrade(sender);
                    }
                }
            }else {
                if (!updater.getUpdate().isNew(Prison.i().getDescription().getVersion())) {
                    sender.sendMessage(MessageUtil.get("general.noUpdate"));
                } else if (updater.getUpdate().install()) {
                    sender.sendMessage(
                            MessageUtil.get("general.updated", Prison.i().updater.getUpdate().name));
                } else {
                    sender.sendMessage(MessageUtil.get("general.updateFailed"));
                }
            }
        } else {
            sender.sendMessage(MessageUtil.get("general.notAllowedToCheck"));
        }
    }

    @Override public String description() {
        return "Downloads the latest version of Prison.";
    }

}
