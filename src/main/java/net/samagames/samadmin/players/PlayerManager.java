package net.samagames.samadmin.players;

import javafx.util.Pair;
import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.users.User;
import net.samagames.samadmin.utils.UUIDTranslator;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
public class PlayerManager
{
    private final SamAdmin instance;

    public PlayerManager(SamAdmin instance)
    {
        this.instance = instance;
    }

    public Pair<Boolean, String> addCommentary(User user, UUID uuid, String text)
    {
        return this.addSanction(user, uuid, "text", null, text);
    }

    public Pair<Boolean, String> addSanction(User user, UUID uuid, String type, String time, String text)
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("INSERT INTO sg_player_sanctions (`type`, `reason`, `playerUUID`, `punisherUUID`, `expiration`, `isDeleted`, `createdAt`, `updatedAt`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pStatement.setString(1, type);
            pStatement.setString(2, text);
            pStatement.setString(3, uuid.toString());
            pStatement.setString(4, user.getUUID().toString());

            long expirationTimestamp = -1;

            if (time != null)
            {
                Calendar nowDate = Calendar.getInstance();
                int timeNumber = Integer.valueOf(time.split(":")[0]);
                String unit = time.split(":")[1];

                if (timeNumber < 1)
                    return new Pair<>(false, "Durée de sanction négative !");

                if (unit.equals("m"))
                    nowDate.add(Calendar.MINUTE, timeNumber);
                else if (unit.equals("h"))
                    nowDate.add(Calendar.HOUR, timeNumber);
                else if (unit.equals("d"))
                    nowDate.add(Calendar.DAY_OF_MONTH, timeNumber);

                expirationTimestamp = nowDate.getTimeInMillis();
            }

            if (expirationTimestamp == -1)
                pStatement.setString(5, null);
            else
                pStatement.setLong(5, expirationTimestamp);

            pStatement.setBoolean(6, false);

            Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());

            pStatement.setTimestamp(7, nowTimestamp);
            pStatement.setTimestamp(8, nowTimestamp);

            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> setPlayerGroup(UUID playerUUID, int groupID)
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_groups WHERE playerUUID = ?");
            pStatement.setString(1, playerUUID.toString());

            if(!pStatement.executeQuery().next())
            {
                pStatement = sql.prepareStatement("INSERT INTO sg_player_groups (`groupId`, `playerUUID`, `options`) VALUES (?, ?, ?)");
                pStatement.setInt(1, groupID);
                pStatement.setString(2, playerUUID.toString());
                pStatement.setString(3, "{}");
                pStatement.executeUpdate();
            }
            else
            {
                pStatement = sql.prepareStatement("UPDATE sg_player_groups SET groupId = ? WHERE playerUUID = ?");
                pStatement.setInt(1, groupID);
                pStatement.setString(2, playerUUID.toString());
                pStatement.executeUpdate();
            }

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Player getPlayerByName(String name)
    {
        UUID uuid = UUIDTranslator.getUUIDOf(name);

        if (uuid == null)
            return null;

        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_players WHERE uuid = ?");
            pStatement.setString(1, uuid.toString());

            ResultSet playerResult = pStatement.executeQuery();
            playerResult.next();

            pStatement = sql.prepareStatement("SELECT * FROM sg_player_groups WHERE playerUUID = ?");
            pStatement.setString(1, uuid.toString());

            ResultSet playerGroupsResult = pStatement.executeQuery();

            int groupId = 1;

            if (playerGroupsResult.next())
                groupId = playerGroupsResult.getInt("groupId");

            PlayerSanction[] sanctions = this.getSanctionsOf(sql, uuid);
            PlayerShop[] shops = this.getShopsOf(sql, uuid);
            PlayerStatistic[] statistics = this.getStatisticsOf(sql, uuid);

            Player player = new Player(playerResult, groupId, sanctions, shops, statistics);

            sql.close();

            return player;
        }
        catch (SQLException e)
        {
            e.getMessage();
        }

        return null;
    }

    public Pair<Boolean, String> isPlayerExist(String name)
    {
        UUID uuid = UUIDTranslator.getUUIDOf(name);

        if (uuid == null)
            return new Pair<>(false, "Ce joueur n'existe pas !");

        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_players WHERE uuid = ?");
            pStatement.setString(1, uuid.toString());

            ResultSet playerResult = pStatement.executeQuery();

            if(playerResult.next())
            {
                sql.close();
                return new Pair<>(true, null);
            }
            else
            {
                sql.close();
                return new Pair<>(false, "Ce joueur n'existe pas dans la base de données !");
            }
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    private PlayerSanction[] getSanctionsOf(Connection sql, UUID uuid) throws SQLException
    {
        PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_sanctions WHERE playerUUID = ?");
        pStatement.setString(1, uuid.toString());

        ResultSet resultSet = pStatement.executeQuery();

        int rows = 0;

        if (resultSet.last())
        {
            rows = resultSet.getRow();
            resultSet.beforeFirst();
        }

        PlayerSanction[] sanctions = new PlayerSanction[rows];

        int i = 0;

        while (resultSet.next())
        {
            sanctions[i] = new PlayerSanction(resultSet);
            i++;
        }

        Collections.reverse(Arrays.asList(sanctions));

        return sanctions;
    }

    private PlayerShop[] getShopsOf(Connection sql, UUID uuid) throws SQLException
    {
        PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_shops WHERE playerUUID = ?");
        pStatement.setString(1, uuid.toString());

        ResultSet resultSet = pStatement.executeQuery();

        int rows = 0;

        if (resultSet.last())
        {
            rows = resultSet.getRow();
            resultSet.beforeFirst();
        }

        PlayerShop[] shops = new PlayerShop[rows];

        int i = 0;

        while (resultSet.next())
        {
            shops[i] = new PlayerShop(resultSet);
            i++;
        }

        return shops;
    }

    private PlayerStatistic[] getStatisticsOf(Connection sql, UUID uuid) throws SQLException
    {
        PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_statistics WHERE playerUUID = ?");
        pStatement.setString(1, uuid.toString());

        ResultSet resultSet = pStatement.executeQuery();

        int rows = 0;

        if (resultSet.last())
        {
            rows = resultSet.getRow();
            resultSet.beforeFirst();
        }

        PlayerStatistic[] statistics = new PlayerStatistic[rows];

        int i = 0;

        while (resultSet.next())
        {
            statistics[i] = new PlayerStatistic(resultSet);
            i++;
        }

        return statistics;
    }
}
