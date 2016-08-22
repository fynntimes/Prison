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
 */

package se.ranzdo.bukkit.methodcommand.handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.ranzdo.bukkit.methodcommand.*;


public class PlayerArgumentHandler extends ArgumentHandler<Player> {
    public PlayerArgumentHandler() {
        setMessage("player_not_online", "The player %1 is not online");

        addVariable("sender", "The command executor", new ArgumentVariable<Player>() {
            @Override
            public Player var(CommandSender sender, CommandArgument argument, String varName)
                throws CommandError {
                if (!(sender instanceof Player)) {
                    throw new CommandError(argument.getMessage("cant_as_console"));
                }

                return ((Player) sender);
            }
        });
    }

    @Override public Player transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        Player p = Bukkit.getPlayer(value);
        if (p == null) {
            throw new TransformError(argument.getMessage("player_not_online", value));
        }

        return p;
    }
}
