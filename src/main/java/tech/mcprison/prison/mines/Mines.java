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

package tech.mcprison.prison.mines;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import tech.mcprison.prison.core.Component;
import tech.mcprison.prison.core.FailedToStartException;
import tech.mcprison.prison.core.Prison;

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

    WorldEditPlugin worldEdit =
        (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
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
        if (oldUnecessaryFile.exists()) {
            oldUnecessaryFile.delete();
        }

        mm = new MinesManager();
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

}
