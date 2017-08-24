package net.samagames.samadmin.servlets.admin;

import javafx.util.Pair;
import net.samagames.samadmin.servlets.AbstractServlet;
import net.samagames.samadmin.users.User;
import net.samagames.samadmin.users.UserPermissionLevel;
import net.samagames.samadmin.utils.CryptUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@WebServlet(
        name = "AdminUsersServlet",
        value = {
                "/admin/users"
        }
)
public class AdminUsersServlet extends AbstractServlet
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

        request.setAttribute("title", "Gestion des utilisateurs");
        request.setAttribute("base-url", "../");
        request.setAttribute("users", this.instance.getUserManager().getUserList());
        request.getRequestDispatcher("../www/admin/users.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("action") != null)
        {
            String action = request.getParameter("action");

            if (action.equals("create") && request.getParameter("username") != null && request.getParameter("password") != null && request.getParameter("uuid") != null && request.getParameter("permission") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                UUID uuid = UUID.fromString(request.getParameter("uuid"));
                UserPermissionLevel permission = UserPermissionLevel.valueOf(request.getParameter("permission"));

                Pair<Boolean, String> result = this.instance.getUserManager().addUser(username, password, uuid, permission);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("edit") && request.getParameter("user") != null && request.getParameter("username") != null && request.getParameter("uuid") != null && request.getParameter("permission") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                int userID = Integer.valueOf(request.getParameter("user"));
                String username = request.getParameter("username");
                UUID uuid = UUID.fromString(request.getParameter("uuid"));
                UserPermissionLevel permission = UserPermissionLevel.valueOf(request.getParameter("permission"));

                String password = null;

                if (request.getParameter("password") != null)
                    password = request.getParameter("password");

                Pair<Boolean, String> result = this.instance.getUserManager().editUser(userID, username, password, uuid, permission);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("delete") && request.getParameter("user") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                int userID = Integer.valueOf(request.getParameter("user"));

                Pair<Boolean, String> result = this.instance.getUserManager().removeUser(userID);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
        }

        this.processRequest(request, response);
    }
}
