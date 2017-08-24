package net.samagames.samadmin.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
public class User
{
    private final int id;
    private final String username;
    private final UUID uuid;
    private final String hashedPassword;
    private final UserPermissionLevel permissionLevel;

    public User(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.username = resultSet.getString("username");
        this.uuid = UUID.fromString(resultSet.getString("uuid"));
        this.hashedPassword = resultSet.getString("password");
        this.permissionLevel = UserPermissionLevel.valueOf(resultSet.getString("permission").toUpperCase());
    }

    public int getID()
    {
        return this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public String getHashedPassword()
    {
        return this.hashedPassword;
    }

    public UserPermissionLevel getPermissionLevel()
    {
        return this.permissionLevel;
    }
}
