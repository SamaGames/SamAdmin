package net.samagames.samadmin.servlets.admin.charts;

import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.Chart;
import net.samagames.samadmin.players.PlayerSanction;
import net.samagames.samadmin.utils.UUIDTranslator;
import org.apache.commons.lang3.text.WordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
public class ModeratorActivityChart extends Chart
{
    private ArrayList<PlayerSanction> sanctions;

    public ModeratorActivityChart(SamAdmin instance, String moderator)
    {
        super("Activité du modérateur : " + moderator);

        this.sanctions = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        HashMap<PlayerSanction.Type, HashMap<Integer, Integer>> sanctions = this.getSanctions(instance, calendar, (moderator.equals("Samaritan") ? PlayerSanction.SAMARITAN : UUIDTranslator.getUUIDOf(moderator)));

        for (PlayerSanction.Type type : sanctions.keySet())
        {
            int total = 0;

            for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                total += sanctions.get(type).get(day);

            ChartData entry = this.addEntry("spline", WordUtils.capitalize(type.name().toLowerCase()) + " (" + total + ")", false, true);

            for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                entry.addValue(String.valueOf(day) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.FRANCE), sanctions.get(type).get(day));
        }
    }

    public HashMap<PlayerSanction.Type, HashMap<Integer, Integer>> getSanctions(SamAdmin instance, Calendar calendar, UUID moderator)
    {
        HashMap<PlayerSanction.Type, HashMap<Integer, Integer>> sanctions = new HashMap<>();
        this.sanctions.clear();

        try (Connection sql = instance.getMySQLAgent().openMinecraftServerConnection())
        {
            int month = calendar.get(Calendar.MONTH) + 1;

            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_player_sanctions WHERE createdAt LIKE '" + calendar.get(Calendar.YEAR) + "-" + (month < 10 ? "0" : "") + month + "%' AND punisherUUID = ?");
            pStatement.setString(1, moderator.toString());

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                PlayerSanction sanction = new PlayerSanction(resultSet);

                if (sanction.getPunisherUUID() == null || sanction.getCreation() == null)
                    continue;

                if (!sanctions.containsKey(sanction.getType()))
                {
                    HashMap<Integer, Integer> moderatorSanctions = new HashMap<>();

                    for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++)
                        moderatorSanctions.put(day, 0);

                    sanctions.put(sanction.getType(), moderatorSanctions);
                }

                int day = sanction.getCreation().getDate();
                int newCount = sanctions.get(sanction.getType()).get(day) + 1;
                sanctions.get(sanction.getType()).put(day, newCount);

                this.sanctions.add(sanction);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        Collections.reverse(this.sanctions);

        return sanctions;
    }

    public ArrayList<PlayerSanction> getSanctions()
    {
        return this.sanctions;
    }
}
