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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import se.ranzdo.bukkit.methodcommand.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.CommandArgument;
import se.ranzdo.bukkit.methodcommand.TransformError;


public class EntityTypeArgumentHandler extends ArgumentHandler<EntityType> {
    public EntityTypeArgumentHandler() {
        setMessage("parse_error", "There is no entity named %1");
        setMessage("include_error", "There is no entity named %1");
        setMessage("exclude_error", "There is no entity named %1");
    }

    @Override
    public EntityType transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        try {
            return EntityType.fromId(Integer.parseInt(value));
        } catch (NumberFormatException e) {
        }

        EntityType t = EntityType.fromName(value);

        if (t != null) {
            return t;
        }

        throw new TransformError(argument.getMessage("parse_error", value));
    }

}
