package net.samagames.samadmin.servlets;

import net.samagames.samadmin.SamAdmin;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

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
@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        SamAdmin.init(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        SamAdmin.getInstance().shutdown();
    }
}
