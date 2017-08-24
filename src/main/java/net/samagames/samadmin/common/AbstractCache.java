package net.samagames.samadmin.common;

import net.samagames.samadmin.SamAdmin;

import java.util.HashMap;

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
public abstract class AbstractCache<T>
{
    protected final SamAdmin instance;
    protected final HashMap<Integer, T> cache;

    public AbstractCache(SamAdmin instance)
    {
        this.instance = instance;
        this.cache = new HashMap<>();

        this.reload();
    }

    public abstract void processReload();

    public void reload()
    {
        this.cache.clear();
        this.processReload();
    }

    public T getCachedObjectByID(int id)
    {
        if (this.cache.containsKey(id))
            return this.cache.get(id);
        else
            return null;
    }

    public HashMap<Integer, T> getCachedObjects()
    {
        return this.cache;
    }
}
