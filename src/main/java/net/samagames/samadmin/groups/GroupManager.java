package net.samagames.samadmin.groups;

import com.google.gson.Gson;
import javafx.util.Pair;
import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.AbstractObjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class GroupManager extends AbstractObjectManager<GroupCache>
{
    public GroupManager(SamAdmin instance)
    {
        super(instance, GroupCache.class);
    }

    public Pair<Boolean, String> addGroup(String name, int rank, String[] permissions, HashMap<String, Object> options)
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_groups WHERE name = ?");
            pStatement.setString(1, name);

            ResultSet result = pStatement.executeQuery();

            if(result.next() && result.getString("name") != null)
                return new Pair<>(false, "Nom déjà utilisé pour ce groupe !");

            pStatement = sql.prepareStatement("INSERT INTO sg_groups (`name`, `rank`, `permissions`, `options`) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, name);
            pStatement.setInt(2, rank);
            pStatement.setString(3, new Gson().toJson(permissions));
            pStatement.setString(4, new Gson().toJson(options));
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> editGroup(int id, String name, int rank, String[] permissions, HashMap<String, Object> options)
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("UPDATE sg_groups SET name = ?, rank = ?, permissions = ?, options = ? WHERE id = ?");
            pStatement.setString(1, name);
            pStatement.setInt(2, rank);
            pStatement.setString(3, new Gson().toJson(permissions));
            pStatement.setString(4, new Gson().toJson(options));
            pStatement.setInt(5, id);
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> removeGroup(int id)
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_groups WHERE id = ?");
            pStatement.setInt(1, id);

            ResultSet result = pStatement.executeQuery();

            if(!result.next())
                return new Pair<>(false, "Groupe non trouvé dans la base de donn�es !");

            pStatement = sql.prepareStatement("DELETE FROM sg_groups WHERE id = ?");
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }
}
