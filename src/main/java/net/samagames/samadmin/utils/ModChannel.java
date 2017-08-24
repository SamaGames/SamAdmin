package net.samagames.samadmin.utils;

/*
 * This file is part of SamAdmin.
 *
 * SamAdmin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamAdmin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamAdmin.  If not, see <http://www.gnu.org/licenses/>.
 */
public enum ModChannel
{
    INFORMATION(ChatColor.GREEN, "Information"),
    DISCUSSION(ChatColor.DARK_AQUA, "Discussion"),
    SANCTION(ChatColor.RED, "Sanction"),
    REPORT(ChatColor.GOLD, "Signalement"),
    ;

    private final ChatColor color;
    private final String name;

    ModChannel(ChatColor color, String name)
    {
        this.color = color;
        this.name = name;
    }

    public ChatColor getColor()
    {
        return this.color;
    }

    public String getName()
    {
        return this.name;
    }
}