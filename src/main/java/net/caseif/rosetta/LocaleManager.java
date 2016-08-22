/*
 * New BSD License (BSD-new)
 *
 * Copyright (c) 2015-2016 Max Roncace
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.caseif.rosetta;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Provides localization support for a particular {@link Plugin}.
 *
 * <p>Locales are loaded as <code>.properties</code> files from the
 * {@code /lang} directory of the archive of the plugin owning this
 * {@link LocaleManager}.</p>
 *
 * @author Max Roncacé
 * @version 1.0.0
 * @since 1.0
 */
public class LocaleManager {

    private static final String DEFAULT_LOCALE = "en_US";
    private static final String LOCALE_FOLDER = "lang";

    static final HashMap<String, List<String>> ALTERNATIVES = new HashMap<>();

    static final Logger LOGGER = Logger.getLogger("Rosetta");

    private final Plugin owner;
    private String defaultLocale = DEFAULT_LOCALE;

    HashMap<String, Properties> configs = new HashMap<>();

    static {
        // English dialects
        ALTERNATIVES.put("en_AU", Arrays.asList("en_GB", "en_CA", "en_US"));
        ALTERNATIVES.put("en_CA", Arrays.asList("en_GB", "en_AU", "en_US"));
        ALTERNATIVES.put("en_GB", Arrays.asList("en_GB", "en_AU", "en_US"));
        ALTERNATIVES.put("en_PT", Arrays.asList("en_US", "en_CA", "en_GB", "en_AU")); // idk
        ALTERNATIVES.put("en_US", Arrays.asList("en_CA", "en_GB", "en_AU"));

        // Spanish dialects (not sure how accurate this is, mostly guesswork)
        ALTERNATIVES.put("es_AR", Arrays.asList("es_UY", "es_VE", "es_MX", "es_ES"));
        ALTERNATIVES.put("es_ES", Arrays.asList("es_MX", "es_AR", "es_UY", "es_VE"));
        ALTERNATIVES.put("es_MX", Arrays.asList("es_ES", "es_AR", "es_UY", "es_VE"));
        ALTERNATIVES.put("es_UY", Arrays.asList("es_AR", "es_VE", "es_MX", "es_ES"));
        ALTERNATIVES.put("es_VE", Arrays.asList("es_AR", "es_UY", "es_MX", "es_ES"));

        // French dialects
        ALTERNATIVES.put("fr_CA", Arrays.asList("fr_FR"));
        ALTERNATIVES.put("fr_FR", Arrays.asList("fr_CA"));

        // Norwegian dialects (not sure how accurate this is)
        ALTERNATIVES.put("nb_NO", Arrays.asList("no_NO", "nn_NO"));
        ALTERNATIVES.put("nn_NO", Arrays.asList("no_NO", "nb_NO"));
        ALTERNATIVES.put("no_NO", Arrays.asList("nb_NO", "nn_NO"));

        // Portugese dialects
        ALTERNATIVES.put("pt_BR", Arrays.asList("pt_PT"));
        ALTERNATIVES.put("pt_PT", Arrays.asList("pt_BR"));
    }

    /**
     * Constructs a new {@link LocaleManager} owned by the given {@link Plugin}.
     *
     * @param plugin The plugin owning the new {@link LocaleManager}.
     * @since 1.0
     */
    public LocaleManager(Plugin plugin) {
        this.owner = plugin;
        loadShippedLocales();
        loadCustomLocales(); // custom locales will override
    }

    private void loadCustomLocales() {
        File dataFolder = getOwningPlugin().getDataFolder();
        if (dataFolder.isDirectory()) {
            File localeFolder = new File(dataFolder, LOCALE_FOLDER);
            if (localeFolder.exists()) {
                if (localeFolder.isDirectory()) {
                    File[] contents = localeFolder.listFiles();
                    if (contents != null) {
                        for (File locale : contents) {
                            if (!locale.isDirectory()) {
                                if (locale.getName().endsWith(".properties")) {
                                    try {
                                        loadLocale(locale.getName().replace(".properties", ""),
                                                new FileInputStream(locale), false);
                                    } catch (IOException ex) {
                                        LOGGER.warning("Failed to load custom locale \"" + locale.getName()
                                                + "\" for plugin " + getOwningPlugin() + " (" + ex.getClass().getName()
                                                + ")");
                                    }
                                }
                            } else {
                                LOGGER.warning("Found subfolder \"" + locale.getName() + "\" within locale folder \""
                                        + LOCALE_FOLDER + "\" in data folder for plugin " + getOwningPlugin()
                                        + " - not loading");
                            }
                        }
                    }
                } else {
                    LOGGER.warning("Locale folder \"" + LOCALE_FOLDER + "\" in data folder for plugin "
                            + getOwningPlugin() + " is not a directory - not loading custom locales");
                }
            }
        }
    }

    private void loadShippedLocales() {
        CodeSource cs = getOwningPlugin().getClass().getProtectionDomain().getCodeSource();
        if (cs != null) {
            try {
                URL jar = cs.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.startsWith(LOCALE_FOLDER + "/") && entryName.endsWith(".properties")) {
                        String[] arr = entryName.split("/");
                        String localeName = arr[arr.length - 1].replace(".properties", "");
                        loadLocale(localeName, zip, true);
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to initialize LocaleManager for plugin " + getOwningPlugin()
                        + " - Rosetta cannot continue!", ex);
            }
        } else {
            throw new RuntimeException("Failed to load code source for plugin " + getOwningPlugin()
                    + " - Rosetta cannot continue!");
        }
    }

    private void loadLocale(String name, InputStream is, boolean printStackTrace) {
        try {
            Properties temp = new Properties();
            temp.load(is);
            Properties config;
            if (configs.containsKey(name)) {
                config = configs.get(name);
                for (Map.Entry<Object, Object> e : temp.entrySet()) {
                    config.put(e.getKey(), e.getValue());
                }
            } else {
                config = temp;
            }
            configs.put(name, config);
        } catch (IOException ex) {
            if (printStackTrace) {
                ex.printStackTrace();
            }
            LOGGER.warning("Failed to load locale " + name + " for plugin " + getOwningPlugin() + " - skipping");
        }
    }

    /**
     * Gets the plugin owning this {@link LocaleManager}.
     *
     * @return The plugin owning this {@link LocaleManager}
     * @since 1.0
     */
    public Plugin getOwningPlugin() {
        return owner;
    }

    /**
     * Gets the default locale of this {@link LocaleManager}.
     *
     * @return A string representing the default locale. This should
     *     follow the {@code ISO 639-1} and {@code ISO 3166-1} standards,
     *     respectively (e.g. {@code en_US}) and defaults to {@code en_US}.
     * @since 1.0
     */
    public String getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Sets the default locale of this {@link LocaleManager}.
     *
     * @param locale A string representing the default locale. This should
     *     follow the {@code ISO 639-1} and {@code ISO 3166-1} standards,
     *     respectively (e.g. {@code en_US} or {@code enUS}) and defaults to
     *     {@code en_US}.
     * @since 1.0
     */
    public void setDefaultLocale(String locale) {
        this.defaultLocale = locale;
    }

    /**
     * Gets the {@link Localizable} associated with the given key.
     *
     * @param key The key of the message to retrieve
     * @return The retrieved message as a {@link Localizable}
     * @since 1.0
     */
    public Localizable getLocalizable(String key) {
        return new Localizable(this, key);
    }

    String getLocale(Player player) {
        if (NmsHelper.hasSupport()) {
            try {
                return NmsHelper.getLocale(player);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
                LOGGER.warning("Could not get locale of player " + player.getName());
            }
        }
        return getDefaultLocale();
    }

    /**
     * Backwards-compatible getOnlinePlayers().
     *
     * @return Current online player list
     */
    @SuppressWarnings("unchecked")
    Collection<? extends Player> getOnlinePlayers() {
        try {
            Object obj = NmsHelper.BUKKIT_GETONLINEPLAYERS.invoke(null);
            if (obj instanceof Collection) {
                return (Collection<? extends Player>) obj; // new method
            } else {
                return Lists.newArrayList((Player[]) obj);
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static class NmsHelper {

        private static final boolean SUPPORT;

        private static final String PACKAGE_VERSION;

        private static final Method CRAFTPLAYER_GETHANDLE;
        private static final Method BUKKIT_GETONLINEPLAYERS;

        private static final Field ENTITY_PLAYER_LOCALE;
        private static final Field LOCALE_LANGUAGE_WRAPPED_STRING;

        static {
            String[] array = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            PACKAGE_VERSION = array.length == 4 ? array[3] + "." : "";

            Method craftPlayer_getHandle = null;
            Method bukkit_getOnlinePlayers = null;
            Field entityPlayer_locale = null;
            Field localeLanguage_wrappedString = null;
            try {
                bukkit_getOnlinePlayers = Bukkit.class.getMethod("getOnlinePlayers");

                craftPlayer_getHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");

                entityPlayer_locale = getNmsClass("EntityPlayer").getDeclaredField("locale");
                entityPlayer_locale.setAccessible(true);
                if (entityPlayer_locale.getType().getSimpleName().equals("LocaleLanguage")) {
                    // On versions prior to 1.6, the locale is stored as a LocaleLanguage object.
                    // The actual locale string is wrapped within it.
                    // On 1.5, it's stored in field "e".
                    // On 1.3 and 1.4, it's stored in field "d".
                    try { // try for 1.5
                        localeLanguage_wrappedString = entityPlayer_locale.getType().getDeclaredField("e");
                    } catch (NoSuchFieldException ex) { // we're pre-1.5
                        localeLanguage_wrappedString = entityPlayer_locale.getType().getDeclaredField("d");
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException ex) {
                ex.printStackTrace();
                LOGGER.severe("Cannot initialize NMS components - per-player localization "
                        + "disabled");
            }
            CRAFTPLAYER_GETHANDLE = craftPlayer_getHandle;
            BUKKIT_GETONLINEPLAYERS = bukkit_getOnlinePlayers;
            ENTITY_PLAYER_LOCALE = entityPlayer_locale;
            LOCALE_LANGUAGE_WRAPPED_STRING = localeLanguage_wrappedString;
            SUPPORT = CRAFTPLAYER_GETHANDLE != null;
        }

        private static boolean hasSupport() {
            return SUPPORT;
        }

        private static String getLocale(Player player) throws IllegalAccessException, InvocationTargetException {
            Object entityPlayer = CRAFTPLAYER_GETHANDLE.invoke(player);
            Object locale = ENTITY_PLAYER_LOCALE.get(entityPlayer);
            if (LOCALE_LANGUAGE_WRAPPED_STRING != null) {
                return (String) LOCALE_LANGUAGE_WRAPPED_STRING.get(locale);
            } else {
                return (String) locale;
            }
        }

        private static Class<?> getCraftClass(String className) throws ClassNotFoundException {
            return Class.forName("org.bukkit.craftbukkit." + PACKAGE_VERSION + className);
        }

        private static Class<?> getNmsClass(String className) throws ClassNotFoundException {
            return Class.forName("net.minecraft.server." + PACKAGE_VERSION + className);
        }

    }

}
