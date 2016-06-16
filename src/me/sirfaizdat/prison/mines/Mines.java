/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.mines;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.FailedToStartException;
import me.sirfaizdat.prison.core.Prison;
import org.bukkit.Bukkit;

import java.io.File;

/**
 * Manages the Mines component.
 *
 * @author SirFaizdat
 */
public class Mines implements Component {

    public static Mines i;
    public MinesManager mm;
    Prison core = Prison.i();

    MinesCommandManager mcm;
    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
    private boolean enabled = true;

    public String getName() {
        return "Mines";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() throws FailedToStartException {
        i = this;

        File oldUnecessaryFile = new File(Prison.i().getDataFolder(), "minesupdated.txt");
        if(oldUnecessaryFile.exists()) oldUnecessaryFile.delete();

        mm = new MinesManager();
        mcm = new MinesCommandManager(this);
        core.getCommand("mines").setExecutor(mcm);
    }

    public void reload() {
        mm.mines.clear();
        mm.load();
        Bukkit.getScheduler().cancelTask(mm.autoResetID);
        mm.timer();
    }

    public void disable() {
        mm.mines.clear();
        Bukkit.getScheduler().cancelTask(mm.autoResetID);
    }

    public WorldEditPlugin getWE() {
        return worldEdit;
    }

    @Override
    public String getBaseCommand() {
        return "mines";
    }

}
