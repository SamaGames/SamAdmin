package net.samagames.samadmin.players;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

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
public class PlayerShop
{
    private final int id;
    private final UUID playerUUID;
    private final String category;
    private final String key;
    private final String equiped;
    private final String[] value;

    public PlayerShop(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.playerUUID = UUID.fromString(resultSet.getString("playerUUID"));
        this.category = resultSet.getString("category");
        this.key = resultSet.getString("key");
        this.equiped = resultSet.getString("equipped");

        JsonArray jsonValue = new JsonParser().parse(resultSet.getString("value")).getAsJsonArray();

        this.value = new String[jsonValue.size()];

        for (int i = 0; i < jsonValue.size(); i++)
            this.value[i] = jsonValue.get(i).getAsString();
    }

    public int getID()
    {
        return this.id;
    }

    public UUID getPlayerUUID()
    {
        return this.playerUUID;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getKey()
    {
        return this.key;
    }

    public String getEquiped()
    {
        return this.equiped;
    }

    public String[] getValue()
    {
        return this.value;
    }
}
