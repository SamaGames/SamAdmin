package net.samagames.samadmin.groups;

import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.AbstractCache;

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
public class GroupCache extends AbstractCache<Group>
{
    public GroupCache(SamAdmin instance)
    {
        super(instance);
    }

    @Override
    public void processReload()
    {
        try (Connection sql = this.instance.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_groups");
            ResultSet result = pStatement.executeQuery();

            while(result.next())
            {
                Group group = new Group(result);
                this.cache.put(group.getID(), group);
            }

            sql.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
