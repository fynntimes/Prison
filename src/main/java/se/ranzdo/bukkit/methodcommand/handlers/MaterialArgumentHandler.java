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

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.CommandArgument;
import se.ranzdo.bukkit.methodcommand.TransformError;


public class MaterialArgumentHandler extends ArgumentHandler<Material> {

    public MaterialArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a valid material.");
        setMessage("include_error", "There is no material named %1");
        setMessage("exclude_error", "There is no material named %1");
    }

    @Override
    public Material transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        Material m = null;
        try {
            m = Material.getMaterial(Integer.parseInt(value));
        } catch (NumberFormatException e) {
        }

        if (m != null) {
            return m;
        }

        m = Material.getMaterial(value);

        if (m != null) {
            return m;
        }


        throw new TransformError(argument.getMessage("parse_error"));
    }
}
