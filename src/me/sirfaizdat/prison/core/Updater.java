package me.sirfaizdat.prison.core;

import java.net.URL;

/**
 * A modified version of the DMP9 Labs bukkit update script.
 *
 * @author DMP9
 * @version 1.0.0
 * @since Prison v2.4
 */
public class Updater {
    private static final String QUERY = "https://api.curseforge.com/servermods/files?projectIds=";
    private static final String USER_AGENT = "DMP9Labs/PluginUpdater";
    private static final int BYTE_SIZE = 1024;
    private URL rssConn;
    private Thread thread; // Updater thread
    private int pluginid = 76155;
    private Update newVersion;
    private Update[] versions;

    public class Update {
        public Update(){}
        public String downloadUrl;
        public String fileName;
        public String fileUrl;
        public String gameVersion;
        public String md5;
        public String name;
        public String projectId;
        public String releaseType;
        public boolean install(){
            return true;
        }
    }

    public boolean checkForUpdates(){
        versions = null;
        return false;
    }

    public Update getUpdate(){
        return null;
    }

    public Update[] getVersions(){
        return versions;
    }
}