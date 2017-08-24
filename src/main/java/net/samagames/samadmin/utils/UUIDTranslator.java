package net.samagames.samadmin.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
public class UUIDTranslator
{
    public static String getNameOf(UUID uuid)
    {
        try
        {
            URL url = new URL("http://mcuuid.com/api/" + uuid.toString());

            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            in.close();

            JsonObject response = new JsonParser().parse(builder.toString()).getAsJsonObject();

            if (response.get("name").isJsonNull())
                return null;

            return response.get("name").getAsString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static UUID getUUIDOf(String name)
    {
        try
        {
            URL url = new URL("http://mcuuid.com/api/" + name);

            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            in.close();

            JsonObject response = new JsonParser().parse(builder.toString()).getAsJsonObject();

            if (response.get("uuid").isJsonNull())
                return null;

            return UUID.fromString(response.get("uuid_formatted").getAsString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
