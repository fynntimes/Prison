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
package me.sirfaizdat.prison.core;

/**
 * @author SirFaizdat
 */
public class MessageUtil {

    private static MessageConfiguration messageConfiguration;

    public MessageUtil() {
        messageConfiguration = new MessageConfiguration();
        messageConfiguration.saveDefaultConfig();
    }

    public static String get(String key) {
        String returnVal = messageConfiguration.getConfig().getString(key);
        if (returnVal == null) returnVal = Prison.color("&cInvalid message key - &6" + key + "&c.");
        return Prison.color(Prison.i().config.serverPrefix + returnVal);
    }

    public static String get(String key, String... replace) {
        String returnVal = get(key);
        for (int i = 0; i < replace.length; i++) returnVal = returnVal.replace("%" + i, Prison.color(replace[i]));
        return returnVal;
    }

    public static void reload() {
        messageConfiguration.reload();
    }

    private class MessageConfiguration extends Configuration {

        MessageConfiguration() {
            super("messages.yml");
        }

    }

}
