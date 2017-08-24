package net.samagames.samadmin.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Player
{
    private final int id;
    private final int groupID;
    private final UUID playerUUID;
    private final String playerName;
    private final Timestamp lastLogin;
    private final Timestamp firstLogin;
    private final PlayerSanction[] sanctions;
    private final PlayerShop[] shops;
    private final PlayerStatistic[] statistics;
    private final int coins;
    private final int stars;

    public Player(ResultSet resultSet, int groupID, PlayerSanction[] sanctions, PlayerShop[] shops, PlayerStatistic[] statistics) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.groupID = groupID;
        this.playerUUID = UUID.fromString(resultSet.getString("uuid"));
        this.playerName = resultSet.getString("name");
        this.lastLogin = resultSet.getTimestamp("lastLogin");
        this.firstLogin = resultSet.getTimestamp("firstLogin");
        this.sanctions = sanctions;
        this.shops = shops;
        this.statistics = statistics;
        this.coins = resultSet.getInt("coins");
        this.stars = resultSet.getInt("stars");
    }

    public int getID()
    {
        return this.id;
    }

    public int getGroupID()
    {
        return this.groupID;
    }

    public UUID getPlayerUUID()
    {
        return this.playerUUID;
    }

    public String getPlayerName()
    {
        return this.playerName;
    }

    public Timestamp getLastLogin()
    {
        return this.lastLogin;
    }

    public Timestamp getFirstLogin()
    {
        return this.firstLogin;
    }

    public PlayerSanction[] getSanctions()
    {
        return this.sanctions;
    }

    public PlayerShop[] getShops()
    {
        return this.shops;
    }

    public PlayerStatistic[] getStatistics()
    {
        return this.statistics;
    }

    public int getCoins()
    {
        return this.coins;
    }

    public int getStars()
    {
        return this.stars;
    }
}
