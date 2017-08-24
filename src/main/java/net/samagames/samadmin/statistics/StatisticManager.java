package net.samagames.samadmin.statistics;

import net.samagames.samadmin.SamAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class StatisticManager
{
    private final SamAdmin instance;

    public StatisticManager(SamAdmin instance)
    {
        this.instance = instance;
    }

    public long countUniquesPlayers()
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT COUNT(*) AS total FROM sg_players");

            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();

            return resultSet.getLong("total");
        }
        catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }
    }

    public long countTotalCoins()
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT SUM(coins) AS total FROM sg_players");

            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();

            return resultSet.getLong("total");
        }
        catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }
    }

    public long countTotalStars()
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT SUM(stars) AS total FROM sg_players");

            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();

            return resultSet.getLong("total");
        }
        catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }
    }
}
