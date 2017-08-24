package net.samagames.samadmin.servlets.admin;

import net.samagames.samadmin.servlets.AbstractServlet;
import net.samagames.samadmin.servlets.admin.charts.ModeratorsActivityChart;
import net.samagames.samadmin.servlets.admin.charts.NewPlayersPerDayChart;
import net.samagames.samadmin.servlets.admin.charts.UniquePlayersPerDayChart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
@WebServlet(
        name = "AdminStatisticsServlet",
        value = {
                "/admin/statistics"
        }
)
public class AdminStatisticsServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "login");
            return;
        }

        if (!this.instance.getUserManager().isAdmin(request.getSession()))
        {
            this.redirectTo(response, "dashboard");
            return;
        }

        request.setAttribute("title", "Statistiques");
        request.setAttribute("base-url", "../");

        request.setAttribute("uniques-players", this.instance.getStatisticManager().countUniquesPlayers());

        request.setAttribute("total-coins", this.instance.getStatisticManager().countTotalCoins());
        request.setAttribute("total-stars", this.instance.getStatisticManager().countTotalStars());

        request.setAttribute("new-players-per-day-chart", new NewPlayersPerDayChart(this.instance));
        request.setAttribute("unique-players-per-day-chart", new UniquePlayersPerDayChart(this.instance));
        request.setAttribute("moderators-activity-chart", new ModeratorsActivityChart(this.instance));

        request.getRequestDispatcher("../www/admin/statistics.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
}
