package net.samagames.samadmin.servlets.admin.charts;

import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.Chart;
import net.samagames.samadmin.players.PlayerSanction;
import net.samagames.samadmin.utils.UUIDTranslator;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.IntStream;

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
public class ModeratorsActivityChart extends Chart
{
    private final SamAdmin instance;
    private final Calendar calendar;

    public ModeratorsActivityChart(SamAdmin instance)
    {
        super("Activité de l'équipe de modération (Mois)");

        this.instance = instance;
        this.calendar = Calendar.getInstance();

        HashMap<UUID, HashMap<Integer, Integer>> sanctions = this.getSanctions();

        for (UUID moderator : sanctions.keySet())
        {
            int total = 0;

            for (int day = 1; day <= this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                total += sanctions.get(moderator).get(day);

            ChartData entry = this.addEntry("spline", (moderator.equals(PlayerSanction.SAMARITAN) ? "Samaritan" : UUIDTranslator.getNameOf(moderator)) + " (" + total + ")", (moderator.equals(PlayerSanction.SAMARITAN)), true);

            for (int day = 1; day <= this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                entry.addValue(String.valueOf(day) + " " + this.calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.FRANCE), sanctions.get(moderator).get(day));
        }
    }

    public HashMap<UUID, HashMap<Integer, Integer>> getSanctions()
    {
        HashMap<UUID, HashMap<Integer, Integer>> sanctions = new HashMap<>();

        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_sanctions WHERE createdAt LIKE '" + this.calendar.get(Calendar.YEAR) + "-" + (this.calendar.get(Calendar.MONTH) + 1) + "%'");

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                PlayerSanction sanction = new PlayerSanction(resultSet);

                if (sanction.getType() != PlayerSanction.Type.BAN && sanction.getType() != PlayerSanction.Type.MUTE)
                    continue;

                if (sanction.getPunisherUUID() == null || sanction.getCreation() == null)
                    continue;

                if (!sanctions.containsKey(sanction.getPunisherUUID()))
                {
                    HashMap<Integer, Integer> moderatorSanctions = new HashMap<>();

                    for (int day = 1; day <= this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                        moderatorSanctions.put(day, 0);

                    sanctions.put(sanction.getPunisherUUID(), moderatorSanctions);
                }

                int day = sanction.getCreation().getDate();
                int newCount = sanctions.get(sanction.getPunisherUUID()).get(day) + 1;
                sanctions.get(sanction.getPunisherUUID()).put(day, newCount);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return sanctions;
    }
}
