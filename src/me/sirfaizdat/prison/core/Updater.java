package me.sirfaizdat.prison.core;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A modified version of the DMP9 Labs bukkit update script.
 *
 * @author DMP9
 * @version 1.0.0
 * @since Prison v2.4
 */
public class Updater {

    /*
     * Declarations
     */

    private static final String QUERY = "https://api.curseforge.com/servermods/files?projectIds=";
    private static final int BYTE_SIZE = 1024;
    private URL rssConn;
    private Thread thread; // Updater thread
    private int pluginid = 76155;
    private Update newVersion;
    private Update[] versions;
    private boolean threaded = false;

    public Updater() {
        try {
            rssConn = new URL(QUERY + pluginid);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Utils
     */


    public class Update {
        public Update() {
        }

        public String downloadUrl;
        public String fileName;
        public String fileUrl;
        public String gameVersion;
        public String md5;
        public String name;
        public String projectId;
        public String releaseType;

        private URL followRedirects(String location) throws IOException {
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

        public boolean install() {
            try {
                if (!new File(Prison.i().getDataFolder(), "/updates/").exists()){
                    new File(Prison.i().getDataFolder(), "/updates/").mkdir();
                }
                File file = new File(Prison.i().getDataFolder(), "/updates/" + fileName);
                URL website = followRedirects(downloadUrl);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(file);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                if (fileName.endsWith(".zip")) {
                    ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file));
                    ZipEntry entry = zipIn.getNextEntry();
                    while (entry != null) {
                        String filePath =
                            new File(Prison.i().getDataFolder().getParentFile(), "/update/")
                                .getPath() + File.separator + entry.getName();
                        if (!entry.isDirectory()) {
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
                        entry = zipIn.getNextEntry();
                    }
                    zipIn.close();
                    file.delete();
                } else if (fileName.endsWith(".jar")) {
                    File path = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                    Files.move(file, new File(path.getParentFile(), "/update/"+path.getName()));
                } else {
                    Files.move(file, new File(Prison.i().getDataFolder().getParentFile(),
                        "/update/" + fileName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean isNew(String currentVersion) {
            String v = getVersion();
            if (v == null || v.isEmpty()) {
                return false;
            }
            if (currentVersion.length() == 3){
                currentVersion += ".0";
            }
            String[] split = v.split("\\.");
            String[] split2 = currentVersion.split("\\.");
            if (split[0].equalsIgnoreCase(split2[0])) {
                if (split[1].equalsIgnoreCase(split2[1])) {
                    if (split[2].equalsIgnoreCase(split2[2])) {
                        return false;
                    }
                }
            }
            return true;
        }

        public String getVersion() {
            for (String s : name.split(" ")) {
                if (s.contains("v") && s.length() == 6) {
                    return s.split("v")[1];
                }
            }
            throw new IllegalArgumentException(
                "Version hasn't followed Semantic versioning. All version names should follow: ProjectName vX.X.X");
        }
    }

    public Updater checkForUpdates(final boolean install) {
        threaded = true;
        thread = new Thread(new Runnable() {
            @Override public void run() {
                if (getUpdateList()) {
                    if (getUpdate().isNew(Prison.i().getDescription().getVersion())) {
                        if (install) {
                            getUpdate().install();
                        }
                    }
                }
            }
        });
        thread.start();
        return this;
    }

    @Deprecated
    public Updater _SYNC_checkForUpdates(boolean install) {
        if (getUpdateList()) {
            if (getUpdate().isNew(Prison.i().getDescription().getVersion())) {
                if (install) {
                    getUpdate().install();
                }
            }
        }
        return this;
    }

    public boolean getUpdateList() {
        try {
            final URLConnection conn = rssConn.openConnection();
            conn.setConnectTimeout(5000);
            conn.addRequestProperty("User-Agent", "DMP9Labs/DboUpdate");

            conn.setDoOutput(true);

            final BufferedReader reader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();
            Update[] array;
            try {
                array = new Gson().fromJson(response, Update[].class);
            } catch (JsonSyntaxException e) {
                Prison.i().getLogger().severe("The remote server sent an invalid response!");
                return false;
            }
            if (array.length == 0) {
                Prison.i().getLogger()
                    .warning("The updater could not find any files for " + pluginid);
                return false;
            }
            newVersion = array[array.length - 1];
            return true;
        } catch (final IOException e) {
            Prison.i().getLogger()
                .severe("The updater could not contact the remote server (is it down?)");
            Prison.i().getLogger().log(Level.SEVERE, null, e);
            return false;
        }
    }

    public Update getUpdate() {
        return newVersion;
    }

    public Updater waitForCheck() {
        if ((this.thread != null) && this.thread.isAlive()) {
            try {
                this.thread.join();
            } catch (final InterruptedException e) {
                Prison.i().getLogger().log(Level.SEVERE, null, e);
            }
        }
        return this;
    }
    public Update[] getVersions() {
        return versions;
    }
}