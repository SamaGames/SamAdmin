package net.samagames.samadmin.servlets.admin.charts;

import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.Chart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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
public class UniquePlayersPerDayChart extends Chart
{
    public UniquePlayersPerDayChart(SamAdmin instance)
    {
        super("Joueurs uniques au jour (Mois)");

        Calendar calendar = Calendar.getInstance();
        HashMap<Integer, Integer> newPlayersPerDay = this.getNewPlayersPerDay(instance, calendar);

        int total = 0;

        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
            total += newPlayersPerDay.get(day);

        ChartData entry = this.addEntry("spline", "Joueurs uniques (" + total + ")", false, true);

        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
            entry.addValue(String.valueOf(day) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.FRANCE), newPlayersPerDay.get(day));
    }

    public HashMap<Integer, Integer> getNewPlayersPerDay(SamAdmin instance, Calendar calendar)
    {
        HashMap<Integer, Integer> sanctions = new HashMap<>();

        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
            sanctions.put(day, 0);

        try (Connection sql = instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_players WHERE lastLogin LIKE '" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "%'");

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                int day = resultSet.getTimestamp("lastLogin").getDate();
                int newCount = sanctions.get(day) + 1;
                sanctions.put(day, newCount);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return sanctions;
    }
}
