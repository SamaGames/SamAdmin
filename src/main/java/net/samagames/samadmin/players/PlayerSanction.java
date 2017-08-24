package net.samagames.samadmin.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class PlayerSanction
{
    public static UUID SAMARITAN = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public enum Type { MUTE, KICK, BAN, TEXT, @Deprecated AVERTISSEMENT }

    public final int id;
    public final Type type;
    public final UUID playerUUID;
    public final UUID punisherUUID;
    public final String reason;
    public final Timestamp creation;
    public final Timestamp expiration;

    public PlayerSanction(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.type = Type.valueOf(resultSet.getString("type").toUpperCase());
        this.playerUUID = UUID.fromString(resultSet.getString("playerUUID"));

        String punisherUUIDRaw = resultSet.getString("punisherUUID");

        if (punisherUUIDRaw != null)
            this.punisherUUID = UUID.fromString(punisherUUIDRaw);
        else
            this.punisherUUID = null;

        this.reason = resultSet.getString("reason");
        this.creation = resultSet.getTimestamp("createdAt");
        this.expiration = resultSet.getTimestamp("expiration");
    }

    public int getID()
    {
        return this.id;
    }

    public Type getType()
    {
        return this.type;
    }

    public UUID getPlayerUUID()
    {
        return this.playerUUID;
    }

    public UUID getPunisherUUID()
    {
        return this.punisherUUID;
    }

    public String getReason()
    {
        return this.reason;
    }

    public Timestamp getCreation()
    {
        return this.creation;
    }

    public Timestamp getExpiration()
    {
        return this.expiration;
    }

    public String getFormattedTime()
    {
        try
        {
            if (this.getPunisherUUID() != null)
                if (this.getPunisherUUID().equals(SAMARITAN))
                    return "Définitif";

            if (this.expiration == null)
            {
                if (this.type == Type.KICK)
                    return "/";
                else
                    return "Définitif";
            }

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date d1 = df.parse(this.expiration.toString());
            Date d2 = df.parse(this.creation.toString());

            long difference = d1.getTime() - d2.getTime();

            long diffMinutes = difference / (60 * 1000) % 60;
            long diffHours = difference / (60 * 60 * 1000) % 24;
            long diffDays = difference / (24 * 60 * 60 * 1000);

            StringBuilder builder = new StringBuilder();

            if (diffDays > 0)
                builder.append(diffDays).append(" jours ");

            if (diffHours > 0)
                builder.append(diffHours).append(" heures ");

            if (diffMinutes > 0)
                builder.append(diffMinutes).append(" minutes ");

            if (builder.length() == 0)
            {
                if (this.type == Type.MUTE || this.type == Type.BAN)
                    builder.append("Inconnnu");
                else
                    builder.append("/");
            }

            return builder.toString();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return "Erreur";
    }
}
