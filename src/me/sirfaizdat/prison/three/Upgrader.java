/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2017  SirFaizdat
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

package me.sirfaizdat.prison.three;

import java.util.ArrayList;
import me.sirfaizdat.prison.core.Prison;
import me.sirfaizdat.prison.core.PrisonLogger;
import me.sirfaizdat.prison.core.Updater;
import me.sirfaizdat.prison.mines.MinesManager;
import me.sirfaizdat.prison.mines.entities.Mine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class Upgrader {

    public static boolean upgrade(CommandSender sender) {
        sender.sendMessage(
            Prison.color(Prison.i().config.serverPrefix + "&bStep 1: &7Downloading update..."));
        Updater updater = new Updater();
        updater.checkForUpdates(true).waitForCheck();
        String oldPath;
        try {
            oldPath = Prison.i().getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
                .getPath();
        } catch (URISyntaxException e) {
            sender.sendMessage(Prison.color(
                Prison.i().config.serverPrefix + "&cFailed get path of old installation!"));
            sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
                + "&cUpgrade cannot continue, reverting changes..."));
            Prison.i().getLogger()
                .log(Level.SEVERE, "Failed to get the jar file of Prison 2", e);
            new File(Prison.i().getDataFolder().getParentFile(), "/update/").delete();
            sender.sendMessage(Prison
                .color(Prison.i().config.serverPrefix + "&cUpgrade failed; no changes were made."));
            return false;
        }
        try {
            Files.write(
                new File(Prison.i().getDataFolder().getParentFile(), "/update/"+new File(oldPath).getName()).toPath(),
                "This file is going to be deleted upon the first run of prison 3. Chances are if you're reading this and this file is in your plugins folder, the upgrade procedure hasn't worked. Please delete this file so that bukkit doesn't go mad every time the server starts"
                    .getBytes());
        } catch (IOException e) {
            sender.sendMessage(Prison.color(
                Prison.i().config.serverPrefix + "&cFailed to write placeholder Prison.jar!"));
            sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
                + "&cUpgrade cannot continue, reverting changes..."));
            Prison.i().getLogger()
                .log(Level.SEVERE, "Failed to write placeholder Prison.jar in update folder", e);
            new File(Prison.i().getDataFolder().getParentFile(), "/update/").delete();
            sender.sendMessage(Prison
                .color(Prison.i().config.serverPrefix + "&cUpgrade failed; no changes were made."));
            return false;
        }
        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
            + "&bStep 2: &7Backing up old data ready for upgrade..."));
        Prison.i().config.useJson = true;
        if (Prison.i().mines.isEnabled()) {
            Prison.i().mines.mm.getMines().values().forEach(Mine::save);
        }
        if (Prison.i().ranks.isEnabled()) {
            Prison.i().ranks.getRanks().forEach(x -> Prison.i().ranks.saveRank(x));
        }
        Prison.i().getDataFolder()
            .renameTo(new File(Prison.i().getDataFolder().getParentFile(), "/Prison.old/"));
        try {
            Path write = Files.write(new File(Prison.i().getDataFolder(), "/upgrade.txt/").toPath(),
                oldPath.getBytes());
        } catch (IOException e) {
            sender.sendMessage(
                Prison.color(Prison.i().config.serverPrefix + "&cFailed to write upgrade path!"));
            sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
                + "&cUpgrade cannot continue, reverting changes..."));
            Prison.i().getLogger().log(Level.SEVERE, "Failed to write upgrade path", e);
            new File(Prison.i().getDataFolder().getParentFile(), "/update/").delete();
            new File(Prison.i().getDataFolder().getParentFile(), "/Prison.old/")
                .renameTo(Prison.i().getDataFolder());
            sender.sendMessage(Prison
                .color(Prison.i().config.serverPrefix + "&cUpgrade failed; no changes were made."));
            return false;
        }
        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
            + "&aUpgrade is ready! Please restart your server to continue."));
        return true;
    }

    public static boolean upgradeForPlayer(Player sender) throws IOException, URISyntaxException {
        sender.sendMessage(
            Prison.color(Prison.i().config.serverPrefix + "&bStep 1: &7Downloading update..."));
        Updater updater = new Updater();
        updater.checkForUpdates(false).waitForCheck();
        File file = new File(Prison.i().getDataFolder(),
            "/updates/" + updater.getUpdate().fileName);
        URL website = followRedirects(updater.getUpdate().downloadUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file));
        ArrayList<String> entries = new ArrayList<>();
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            entries.add(entry.getName());
        }
        final HashMap<String, Boolean>[] modules = new HashMap[1];
        ModuleSelect ms = ModuleSelect.initializeForPlayer(sender,x -> modules[0] = x,);
        String oldPath = Prison.i().getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            .getPath();
        try {
            Files.write(
                new File(Prison.i().getDataFolder().getParentFile(), "/update/"+new File(oldPath).getName()).toPath(),
                "This file is going to be deleted upon the first run of prison 3. Chances are if you're reading this and this file is in your plugins folder, the upgrade procedure hasn't worked. Please delete this file so that bukkit doesn't go mad every time the server starts"
                    .getBytes());
        } catch (IOException e) {
            sender.sendMessage(Prison.color(
                Prison.i().config.serverPrefix + "&cFailed to write placeholder Prison.jar!"));
            sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
                + "&cUpgrade cannot continue, reverting changes..."));
            Prison.i().getLogger()
                .log(Level.SEVERE, "Failed to write placeholder Prison.jar in update folder", e);
            new File(Prison.i().getDataFolder().getParentFile(), "/update/").delete();
            sender.sendMessage(Prison
                .color(Prison.i().config.serverPrefix + "&cUpgrade failed; no changes were made."));
            return false;
        }
        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
            + "&bStep 2: &7Backing up old data ready for upgrade..."));
        if (Prison.i().mines.isEnabled()) {
            Prison.i().mines.mm.getMines().values().forEach(Mine::save);
        }
        if (Prison.i().ranks.isEnabled()) {
            Prison.i().ranks.getRanks().forEach(x -> Prison.i().ranks.saveRank(x));
        }
        Prison.i().getDataFolder()
            .renameTo(new File(Prison.i().getDataFolder().getParentFile(), "/Prison.old/"));
        try {
            Files.write(new File(Prison.i().getDataFolder(), "/upgrade.txt/").toPath(),
                oldPath.getBytes());
        } catch (IOException e) {
            sender.sendMessage(
                Prison.color(Prison.i().config.serverPrefix + "&cFailed to write upgrade path!"));
            sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
                + "&cUpgrade cannot continue, reverting changes..."));
            Prison.i().getLogger().log(Level.SEVERE, "Failed to write upgrade path", e);
            new File(Prison.i().getDataFolder().getParentFile(), "/update/").delete();
            new File(Prison.i().getDataFolder().getParentFile(), "/Prison.old/")
                .renameTo(Prison.i().getDataFolder());
            sender.sendMessage(Prison
                .color(Prison.i().config.serverPrefix + "&cUpgrade failed; no changes were made."));
            return false;
        }
        sender.sendMessage(
            Prison.color(Prison.i().config.serverPrefix + "&bStep 3: &7Extracting update..."));
        if (updater.getUpdate().fileName.endsWith(".zip")) {
            ZipEntry e = zipIn.getNextEntry();
            while (e != null) {
                String filePath =
                    new File(Prison.i().getDataFolder().getParentFile(), "/update/")
                        .getPath() + File.separator + e.getName();
                if (!e.isDirectory()) {
                    BufferedOutputStream bos =
                        new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] bytesIn = new byte[4096];
                    int read = 0;
                    while ((read = zipIn.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                    bos.close();
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                e = zipIn.getNextEntry();
            }
            zipIn.close();
            file.delete();
        }
        sender.sendMessage(Prison.color(Prison.i().config.serverPrefix
            + "&aUpgrade is ready! Please restart your server to continue."));
        return true;
    }

    private static URL followRedirects(String location) throws IOException {
        URL resourceUrl, base, next;
        HttpURLConnection conn;
        String redLoc;
        while (true) {
            resourceUrl = new URL(location);
            conn = (HttpURLConnection) resourceUrl.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0...");

            switch (conn.getResponseCode()) {
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    redLoc = conn.getHeaderField("Location");
                    base = new URL(location);
                    next = new URL(base, redLoc);  // Deal with relative URLs
                    location = next.toExternalForm();
                    continue;
            }
            break;
        }
        return conn.getURL();
    }
}
