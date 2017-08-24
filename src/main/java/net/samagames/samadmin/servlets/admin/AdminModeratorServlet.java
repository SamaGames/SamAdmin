package net.samagames.samadmin.servlets.admin;

import net.samagames.samadmin.groups.Group;
import net.samagames.samadmin.servlets.AbstractServlet;
import net.samagames.samadmin.servlets.admin.charts.ModeratorActivityChart;
import net.samagames.samadmin.users.UserPermissionLevel;

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
        name = "AdminModeratorServlet",
        value = {
                "/admin/moderator"
        }
)
public class AdminModeratorServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "login");
            return;
        }

        if (!UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.MASTERMODERATOR))
        {
            this.redirectTo(response, "dashboard");
            return;
        }

        String name = request.getParameter("name");

        request.setAttribute("title", "Gestion du modérateur " + name);
        request.setAttribute("base-url", "../");
        request.setAttribute("moderator-name", name);
        request.setAttribute("moderator-activity-chart", new ModeratorActivityChart(this.instance, name));
        request.getRequestDispatcher("../www/admin/moderator.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("name") == null)
        {
            this.redirectTo(response, "search");
            return;
        }

        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
}
