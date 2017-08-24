package net.samagames.samadmin.groups;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
public class Group
{
    private final int id;
    private final String name;
    private final int rank;
    private final String[] permissions;
    private final HashMap<String, Object> options;

    public Group(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.rank = resultSet.getInt("rank");

        JsonArray jsonPermissions = new JsonParser().parse(resultSet.getString("permissions")).getAsJsonArray();
        this.permissions = new String[jsonPermissions.size()];

        for (int i = 0; i < jsonPermissions.size(); i++)
            this.permissions[i] = jsonPermissions.get(i).getAsString();

        JsonObject jsonOptions = new JsonParser().parse(resultSet.getString("options")).getAsJsonObject();
        this.options = new HashMap<>();

        if (jsonOptions.has("display"))
            this.options.put("display", jsonOptions.get("display").getAsString());

        if (jsonOptions.has("prefix"))
            this.options.put("prefix", jsonOptions.get("prefix").getAsString());

        if (jsonOptions.has("suffix"))
            this.options.put("suffix", jsonOptions.get("suffix").getAsString());

        if (jsonOptions.has("multiplier"))
            this.options.put("multiplier", jsonOptions.get("multiplier").getAsString());

        if (jsonOptions.has("parents"))
        {
            JsonArray jsonOptionsParents = jsonOptions.get("parents").getAsJsonArray();
            int[] parents = new int[jsonOptionsParents.size()];

            for (int i = 0; i < jsonOptionsParents.size(); i++)
                parents[i] = jsonOptionsParents.get(i).getAsInt();

            this.options.put("parents", parents);
        }
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getRank()
    {
        return this.rank;
    }

    public String[] getPermissions()
    {
        return this.permissions;
    }

    public HashMap<String, Object> getOptions()
    {
        return this.options;
    }
}
