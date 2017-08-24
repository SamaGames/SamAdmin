package net.samagames.samadmin.utils;

import net.samagames.samadmin.SamAdmin;

import java.sql.Connection;
import java.sql.DriverManager;
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
public class MySQLAgent
{
    private final SamAdmin instance;

    public MySQLAgent(SamAdmin instance)
    {
        this.instance = instance;
    }

    public Connection openApplicationConnection()
    {
        return this.openConnection(this.instance.getConfiguration().SQL_APP_HOST, (this.instance.getConfiguration().SQL_APP_HOST.contains(":") ? this.instance.getConfiguration().SQL_APP_HOST.split(":")[1] : "3306"), this.instance.getConfiguration().SQL_APP_DATABASE, this.instance.getConfiguration().SQL_APP_USERNAME, this.instance.getConfiguration().SQL_APP_PASSWORD);
    }

    public Connection openMinecraftServerConnection()
    {
        return this.openConnection(this.instance.getConfiguration().SQL_SERVER_HOST, (this.instance.getConfiguration().SQL_SERVER_HOST.contains(":") ? this.instance.getConfiguration().SQL_SERVER_HOST.split(":")[1] : "3306"), this.instance.getConfiguration().SQL_SERVER_DATABASE, this.instance.getConfiguration().SQL_SERVER_USERNAME, this.instance.getConfiguration().SQL_SERVER_PASSWORD);
    }

    public Connection openConnection(String host, String port, String db, String user, String password)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?characterEncoding=UTF-8";

        try
        {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}