package net.samagames.samadmin.servlets;

import net.samagames.samadmin.SamAdmin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public abstract class AbstractServlet extends HttpServlet
{
    protected SamAdmin instance;

    public abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    public abstract void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    public abstract void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        this.instance = SamAdmin.getInstance();
        req.setAttribute("samadmin", this.instance);

        this.actionGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        this.instance = SamAdmin.getInstance();
        req.setAttribute("samadmin", this.instance);

        this.actionPost(req, resp);
    }

    public void redirectTo(HttpServletResponse response, String url) throws IOException
    {
        response.sendRedirect((this.instance.getConfiguration().PRODUCTION ? "/" : "/SamAdmin/") + url);
    }

    @Override
    public String getServletInfo()
    {
        return this.getClass().getCanonicalName();
    }
}
