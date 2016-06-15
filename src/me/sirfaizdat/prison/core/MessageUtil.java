/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

import java.util.HashMap;

/**
 * @author SirFaizdat
 */
public class MessageUtil {

    private static MessageConfiguration messageConfiguration;

    public MessageUtil() {
        messageConfiguration = new MessageConfiguration();
        messageConfiguration.saveDefaultConfig();;
    }

    public static String get(String key) {
        String returnVal = messageConfiguration.getConfig().getString(key);
        if (returnVal == null) returnVal = Prison.colorize("&cInvalid message key - &6" + key + "&c.");
        return Prison.colorize(Prison.i().config.serverPrefix + returnVal);
    }

    public static String get(String key, String... replace) {
        String returnVal = get(key);
        for (int i = 0; i < replace.length; i++) returnVal = returnVal.replaceAll("%" + i, Prison.colorize(replace[i]));
        return returnVal;
    }

    private class MessageConfiguration extends Configuration {

        MessageConfiguration() {
            super("messages.yml");
        }

    }

}
