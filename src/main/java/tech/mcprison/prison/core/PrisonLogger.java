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

package tech.mcprison.prison.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Basic colorized logger.
 *
 * @author SirFaizdat
 */
public class PrisonLogger {

    String coloredLogPrefix = "&6[&4Prison&6]";

    /**
     * Logs a message to the server.
     *
     * @param level   The log level from Logger.Level
     * @param message The log message.
     */
    public void log(Level level, String message) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (level.equals(Level.INFO)) {
            if (sender != null) {
                String fullMsg = Prison.color(coloredLogPrefix + " " + message);
                sender.sendMessage(fullMsg);
            } else {
                String fullMsg = Prison.color(coloredLogPrefix + " " + message);
                Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
            }
        } else if (level.equals(Level.WARN)) {
            message = ChatColor.stripColor(message);
            if (sender != null) {
                String fullMsg = Prison.color(coloredLogPrefix + " &6" + message);
                sender.sendMessage(fullMsg);
            } else {
                String fullMsg = Prison.color(coloredLogPrefix + " &6" + message);
                Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
            }
        } else if (level.equals(Level.SEVERE)) {
            message = ChatColor.stripColor(message);
            if (sender != null) {
                String fullMsg = Prison.color(coloredLogPrefix + " &c" + message);
                sender.sendMessage(fullMsg);
            } else {
                String fullMsg = Prison.color(coloredLogPrefix + " &c" + message);
                Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
            }
        }
    }

    /**
     * Logs a message with Level INFO.
     *
     * @param message The message you wish to send.
     */
    public void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with Level WARN.
     *
     * @param message The message you wish to send.
     */
    public void warning(String message) {
        log(Level.WARN, message);
    }

    /**
     * Logs a message with Level SEVERE.
     *
     * @param message The message you wish to send.
     */
    public void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static enum Level {
        INFO, WARN, SEVERE;
    }

}
