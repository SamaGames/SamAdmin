package net.samagames.samadmin.messages;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Message
{
    private final int id;
    private final String from;
    private final String message;
    private final int delay;
    private final TimeUnit timeUnit;

    public Message(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.from = resultSet.getString("fromname");
        this.message = resultSet.getString("message");
        this.delay = resultSet.getInt("delay");
        this.timeUnit = TimeUnit.valueOf(resultSet.getString("timeunit"));
    }

    public int getID()
    {
        return this.id;
    }

    public String getFrom()
    {
        return this.from;
    }

    public String getMessage()
    {
        return this.message;
    }

    public int getDelay()
    {
        return this.delay;
    }

    public TimeUnit getTimeUnit()
    {
        return this.timeUnit;
    }
}
