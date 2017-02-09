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
package me.sirfaizdat.prison.core.cmds;

import me.sirfaizdat.prison.core.Command;
import me.sirfaizdat.prison.core.Component;
import me.sirfaizdat.prison.core.Prison;

/**
 * @author SirFaizdat
 */
public class CmdVersion extends Command {

    CmdVersion() {
        super("version");
    }

    @Override protected void execute() {
        sender.sendMessage(Prison.color(
            "&7============ &3Prison v" + Prison.i().getDescription().getVersion()
                + " &7============"));
        if (Prison.i().getDescription().getVersion().contains("-SNAPSHOT")) {
            sender.sendMessage(Prison.color("&cThis is a development build and may be unstable."));
        }
        sender.sendMessage(Prison.color("&7Author: &3SirFaizdat &7& &3Camouflage100"));
        sender
            .sendMessage(Prison.color("&7Website: &3" + Prison.i().getDescription().getWebsite()));
        sender.sendMessage(Prison.color("&7Mines are " + getEnabledString(Prison.i().mines)));
        sender.sendMessage(Prison.color("&7Ranks are " + getEnabledString(Prison.i().ranks)));
        sender.sendMessage(Prison.color("&7======================================="));
    }

    private String getEnabledString(Component c) {
        return c.isEnabled() ? "&2enabled" : "&cdisabled";
    }

    @Override public String description() {
        return "View information about Prison.";
    }


}
