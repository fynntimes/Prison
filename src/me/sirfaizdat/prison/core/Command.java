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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Abstract command class
 *
 * @author SirFaizdat
 */
public abstract class Command {

    protected CommandSender sender;
    protected String[] args;
    protected int amountOfOptionalArgs;
    String name;
    ArrayList<String> requiredArgs, optionalArgs;
    String permission;
    boolean mustBePlayer = false;
    Component c;

    public Command(String name) {
        this.name = name;
        requiredArgs = new ArrayList<>();
        optionalArgs = new ArrayList<>();
        // Default permission
        permission = "prison." + name;
    }

    public void addRequiredArg(String name) {
        requiredArgs.add(name);
    }

    public void addOptionalArg(String name) {
        optionalArgs.add(name);
    }

    public void setComponent(Component c) {
        if (c == null)
            return;
        this.c = c;
        permission = "prison." + c.getName().toLowerCase() + "." + name;
    }

    public void mustBePlayer(boolean mustBePlayer) {
        this.mustBePlayer = mustBePlayer;
    }

    public void run(CommandSender sender, String[] args) {
        if (mustBePlayer && !(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.get("general.mustBePlayer"));
            return;
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(MessageUtil.get("general.noPermission"));
            return;
        }
        int length = args.length - 1;
        if (length < requiredArgs.size()) {
            sender.sendMessage(MessageUtil
                    .get("general.notEnoughArgs", usage()));
            return;
        }
        amountOfOptionalArgs = length - requiredArgs.size();
        this.sender = sender;
        this.args = args;

        execute();
    }

    protected abstract void execute();

    public String usage() {
        StringBuilder usage = new StringBuilder();
        usage.append("&3/" + c.getBaseCommand() + " " + name + " &3");
        for (String s : requiredArgs) {
            usage.append("<" + s + "> ");
        }
        for (String s : optionalArgs) {
            usage.append("[" + s + "] ");
        }
        return usage.toString();
    }

    public abstract String description();

}
