/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import me.sirfaizdat.prison.mines.Mines;
import me.sirfaizdat.prison.ranks.Ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Basic colorized logger.
 * 
 * @author SirFaizdat
 */
public class CoreLogger {

	String coloredLogPrefix = "&6[&4Prison&6]";

	public static enum Level {
		INFO, WARN, SEVERE;
	}

	/**
	 * Logs a message to the server.
	 * 
	 * @param level
	 *            The log level from Logger.Level
	 * @param message
	 *            The log message.
	 */
	public void log(Level level, String message) {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		if (level.equals(Level.INFO)) {
			if (sender != null) {
				String fullMsg = Core
						.colorize(coloredLogPrefix + " " + message);
				sender.sendMessage(fullMsg);
			} else {
				String fullMsg = Core
						.colorize(coloredLogPrefix + " " + message);
				Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
			}
		} else if (level.equals(Level.WARN)) {
			message = ChatColor.stripColor(message);
			if (sender != null) {
				String fullMsg = Core.colorize(coloredLogPrefix + " &6"
						+ message);
				sender.sendMessage(fullMsg);
			} else {
				String fullMsg = Core.colorize(coloredLogPrefix + " &6"
						+ message);
				Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
			}
		} else if (level.equals(Level.SEVERE)) {
			message = ChatColor.stripColor(message);
			if (sender != null) {
				String fullMsg = Core.colorize(coloredLogPrefix + " &c"
						+ message);
				sender.sendMessage(fullMsg);
			} else {
				String fullMsg = Core.colorize(coloredLogPrefix + " &c"
						+ message);
				Bukkit.getLogger().info(ChatColor.stripColor(fullMsg));
			}
		}
	}

	/**
	 * Logs a message with Level INFO.
	 * 
	 * @param message
	 *            The message you wish to send.
	 */
	public void info(String message) {
		log(Level.INFO, message);
	}

	/**
	 * Logs a message with Level WARN.
	 * 
	 * @param message
	 *            The message you wish to send.
	 */
	public void warning(String message) {
		log(Level.WARN, message);
	}

	/**
	 * Logs a message with Level SEVERE.
	 * 
	 * @param message
	 *            The message you wish to send.
	 */
	public void severe(String message) {
		log(Level.SEVERE, message);
	}

}
