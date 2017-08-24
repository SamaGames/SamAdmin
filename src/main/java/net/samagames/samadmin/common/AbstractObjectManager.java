package net.samagames.samadmin.common;

import net.samagames.samadmin.SamAdmin;

import java.lang.reflect.InvocationTargetException;

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
public class AbstractObjectManager<CACHE extends AbstractCache>
{
    protected final SamAdmin instance;
    protected CACHE cache;

    public AbstractObjectManager(SamAdmin instance, Class<? extends AbstractCache> cacheClass)
    {
        this.instance = instance;

        try
        {
            this.cache = (CACHE) cacheClass.getConstructor(SamAdmin.class).newInstance(instance);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public CACHE getCache()
    {
        return this.cache;
    }
}
