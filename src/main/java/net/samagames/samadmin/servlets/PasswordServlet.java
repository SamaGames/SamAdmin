package net.samagames.samadmin.servlets;

import javafx.util.Pair;

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
        name = "PasswordServlet",
        value = {
                "/password"
        }
)
public class PasswordServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "login");
            return;
        }

        request.setAttribute("title", "Changer son mot de passe");
        request.getRequestDispatcher("www/password.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("password") != null)
        {
            Pair<Boolean, String> result = this.instance.getUserManager().editPassword(this.instance.getUserManager().getActual(request.getSession()).getID(), request.getParameter("password"));

            if (!result.getKey())
            {
                request.setAttribute("error", result.getValue());
            }
            else
            {
                this.redirectTo(response, "login?logout");
                return;
            }
        }

        this.processRequest(request, response);
    }
}
