package net.samagames.samadmin.servlets.admin;

import javafx.util.Pair;
import net.samagames.samadmin.servlets.AbstractServlet;
import net.samagames.samadmin.users.UserPermissionLevel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        name = "AdminMessagesServlet",
        value = {
                "/admin/messages"
        }
)
public class AdminMessagesServlet extends AbstractServlet
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

        request.setAttribute("title", "Gestion des messages programmés");
        request.setAttribute("base-url", "../");
        request.setAttribute("messages", this.instance.getMessageManager().getMessages());
        request.getRequestDispatcher("../www/admin/messages.jsp").forward(request, response);
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

            if (action.equals("create") && request.getParameter("from") != null && request.getParameter("message-text") != null && request.getParameter("delay") != null && request.getParameter("time-unit") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                String from = request.getParameter("from");
                String message = new String(request.getParameter("message-text").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                int delay = Integer.valueOf(request.getParameter("delay"));
                TimeUnit timeUnit = TimeUnit.valueOf(request.getParameter("time-unit"));

                Pair<Boolean, String> result = this.instance.getMessageManager().addMessage(from, message, delay, timeUnit);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("edit") && request.getParameter("message") != null && request.getParameter("from") != null && request.getParameter("message-text") != null && request.getParameter("delay") != null && request.getParameter("time-unit") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                int id = Integer.valueOf(request.getParameter("message"));
                String from = request.getParameter("from");
                String message = new String(request.getParameter("message-text").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                int delay = Integer.valueOf(request.getParameter("delay"));
                TimeUnit timeUnit = TimeUnit.valueOf(request.getParameter("time-unit"));

                Pair<Boolean, String> result = this.instance.getMessageManager().editMessage(id, from, message, delay, timeUnit);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("delete") && request.getParameter("message") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                Pair<Boolean, String> result = this.instance.getMessageManager().removeMessage(Integer.valueOf(request.getParameter("message")));

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
        }

        this.processRequest(request, response);
    }
}
