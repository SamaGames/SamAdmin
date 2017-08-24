package net.samagames.samadmin;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

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
public class Configuration
{
    public String SALT = "4356TYUHJGNBV5RTJGHN547E567575bvnxdfgbvc4356TY54g";

    public String SQL_APP_HOST;
    public String SQL_APP_USERNAME;
    public String SQL_APP_PASSWORD;
    public String SQL_APP_DATABASE;

    public String SQL_SERVER_HOST;
    public String SQL_SERVER_USERNAME;
    public String SQL_SERVER_PASSWORD;
    public String SQL_SERVER_DATABASE;

    public String REDIS_HOST;
    public String REDIS_PORT;
    public String REDIS_PASSWORD;

    public boolean PRODUCTION = false;

    public Configuration(ServletContextEvent sce)
    {
        ServletContext context = sce.getServletContext();

        try
        {
            String url = context.getRealPath(File.separator);
            File file = new File(url, "config.json");

            if (!file.exists())
            {
                this.createConfiguration(file);

                System.out.println("================================================");
                System.out.println("=[            Configuration Created           ]=");
                System.out.println("=[                  Stopping                  ]=");
                System.out.println("================================================");

                System.exit(0);
            }

            JsonObject configuration = new JsonParser().parse(new FileReader(file)).getAsJsonObject();

            PRODUCTION = configuration.get("production").getAsBoolean();

            JsonObject sqlAppConfiguration = configuration.get("sql_app").getAsJsonObject();

            SQL_APP_HOST = sqlAppConfiguration.get("host").getAsString();
            SQL_APP_USERNAME = sqlAppConfiguration.get("username").getAsString();
            SQL_APP_PASSWORD = sqlAppConfiguration.get("password").getAsString();
            SQL_APP_DATABASE = sqlAppConfiguration.get("database").getAsString();

            JsonObject sqlServerConfiguration = configuration.get("sql_server").getAsJsonObject();

            SQL_SERVER_HOST = sqlServerConfiguration.get("host").getAsString();
            SQL_SERVER_USERNAME = sqlServerConfiguration.get("username").getAsString();
            SQL_SERVER_PASSWORD = sqlServerConfiguration.get("password").getAsString();
            SQL_SERVER_DATABASE = sqlServerConfiguration.get("database").getAsString();

            JsonObject redisConfiguration = configuration.get("redis").getAsJsonObject();

            REDIS_HOST = redisConfiguration.get("host").getAsString();
            REDIS_PORT = redisConfiguration.get("port").getAsString();
            REDIS_PASSWORD = redisConfiguration.get("password").getAsString();
        }
        catch (MalformedURLException | FileNotFoundException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    private void createConfiguration(File file) throws MalformedURLException, URISyntaxException
    {
        JsonObject jsonRoot = new JsonObject();

        jsonRoot.addProperty("production", false);

        JsonObject sqlAppConfiguration = new JsonObject();

        sqlAppConfiguration.addProperty("host", "");
        sqlAppConfiguration.addProperty("username", "");
        sqlAppConfiguration.addProperty("password", "");
        sqlAppConfiguration.addProperty("database", "");

        JsonObject sqlServerConfiguration = new JsonObject();

        sqlServerConfiguration.addProperty("host", "");
        sqlServerConfiguration.addProperty("username", "");
        sqlServerConfiguration.addProperty("password", "");
        sqlServerConfiguration.addProperty("database", "");

        JsonObject redisConfiguration = new JsonObject();

        redisConfiguration.addProperty("host", "");
        redisConfiguration.addProperty("port", "");
        redisConfiguration.addProperty("password", "");

        jsonRoot.add("sql_app", sqlAppConfiguration);
        jsonRoot.add("sql_server", sqlServerConfiguration);
        jsonRoot.add("redis", redisConfiguration);

        try (Writer writer = new FileWriter(file))
        {
            new GsonBuilder().setPrettyPrinting().create().toJson(jsonRoot, writer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
