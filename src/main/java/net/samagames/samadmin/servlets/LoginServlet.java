package net.samagames.samadmin.servlets;

import net.samagames.samadmin.users.User;
import net.samagames.samadmin.utils.CryptUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        name = "LoginServlet",
        value = {
                "/login"
        }
)
public class LoginServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "dashboard");
            return;
        }

        request.setAttribute("title", "Connexion");
        request.getRequestDispatcher("www/login.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("logout") != null)
        {
            request.getSession().invalidate();
        }

        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("username") != null && request.getParameter("password") != null)
        {
            String username = request.getParameter("username");
            String password = CryptUtils.toSHA256(this.instance.getConfiguration().SALT + request.getParameter("password"));

            try(Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
            {
                PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_users WHERE username = ? AND password = ?");
                pStatement.setString(1, username);
                pStatement.setString(2, password);

                ResultSet result = pStatement.executeQuery();

                if(result.next() && result.getString("username") != null && result.getString("username").equals(username))
                {
                    request.getSession().setAttribute("user-object", new User(result));
                }
                else
                {
                    request.setAttribute("failed", true);
                }

                sql.close();
            }
            catch (SQLException ignored) {}
        }

        this.processRequest(request, response);
    }
}
