package net.samagames.samadmin.servlets;

import javafx.util.Pair;
import net.samagames.samadmin.groups.Group;
import net.samagames.samadmin.players.Player;
import net.samagames.samadmin.players.PlayerSanction;
import net.samagames.samadmin.users.User;
import net.samagames.samadmin.users.UserPermissionLevel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        name = "PlayerServlet",
        value = {
                "/player"
        }
)
public class PlayerServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "login");
            return;
        }

        String name = request.getParameter("name");
        Player player = this.instance.getPlayerManager().getPlayerByName(name);

        if (player == null)
        {
            this.redirectTo(response, "search");
            return;
        }

        request.setAttribute("title", "Profil de " + name);
        request.setAttribute("player-name", name);
        request.setAttribute("player", player);

        Group group = this.instance.getGroupManager().getCache().getCachedObjectByID(player.getGroupID());

        request.setAttribute("player-group", (group != null ? group.getName() : "Inconnu"));
        request.setAttribute("groups", this.instance.getGroupManager().getCache().getCachedObjects().values());

        request.getRequestDispatcher("www/player.jsp").forward(request, response);
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
        if (request.getParameter("action") != null && request.getParameter("player-name") != null && request.getParameter("player") != null)
        {
            String action = request.getParameter("action");

            if (action.equals("commentary") && request.getParameter("commentary") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.MODERATOR))
            {
                User user = this.instance.getUserManager().getActual(request.getSession());
                UUID player = UUID.fromString(request.getParameter("player"));
                String commentary = request.getParameter("commentary");

                byte[] commentaryBytes = commentary.getBytes(StandardCharsets.ISO_8859_1);
                commentary = new String(commentaryBytes, StandardCharsets.UTF_8);

                StringBuilder text = new StringBuilder(commentary);

                int loc = new String(text).indexOf('\n');

                while (loc > 0)
                {
                    text.replace(loc, (loc + 1), "<br/>");
                    loc = new String(text).indexOf('\n');
                }

                Pair<Boolean, String> result = this.instance.getPlayerManager().addCommentary(user, player, text.toString());

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("sanction") && request.getParameter("type") != null && request.getParameter("time") != null && request.getParameter("unit") != null && request.getParameter("reason") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.MODERATOR))
            {
                User user = this.instance.getUserManager().getActual(request.getSession());
                UUID player = UUID.fromString(request.getParameter("player"));
                String type = request.getParameter("type");
                String reason = request.getParameter("reason");

                byte[] reasonBytes = reason.getBytes(StandardCharsets.ISO_8859_1);
                reason = new String(reasonBytes, StandardCharsets.UTF_8);

                StringBuilder text = new StringBuilder(reason);

                int loc = new String(text).indexOf('\n');

                while (loc > 0)
                {
                    text.replace(loc, (loc + 1), "<br/>");
                    loc = new String(text).indexOf('\n');
                }

                String time = request.getParameter("time") + ":" + request.getParameter("unit");

                Pair<Boolean, String> result = this.instance.getPlayerManager().addSanction(user, player, type, time, reason);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("group") && request.getParameter("group") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.MODERATOR))
            {
                UUID player = UUID.fromString(request.getParameter("player"));
                int groupID = Integer.valueOf(request.getParameter("group"));

                Pair<Boolean, String> result = this.instance.getPlayerManager().setPlayerGroup(player, groupID);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }

            this.redirectTo(response, "player?name=" + request.getParameter("player-name"));
            return;
        }

        this.processRequest(request, response);
    }
}
