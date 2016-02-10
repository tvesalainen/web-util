/*
 * Copyright (C) 2016 tkv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vesalainen.html;

import java.util.List;
import java.util.Map.Entry;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;

/**
 *
 * @author tkv
 * @param <P>
 * @param <K>
 */
public abstract class AbstractDynContent<P extends DynParam<K>,K> implements DynContent<P,K>
{
    protected MapList<K,Placeholder> map;

    public AbstractDynContent()
    {
        this(new HashMapList<K,Placeholder>());
    }

    protected AbstractDynContent(MapList<K, Placeholder> map)
    {
        this.map = map;
    }
    
    @Override
    public Placeholder<Object> wrap(K key)
    {
        Placeholder wrap = new Placeholder<>();
        map.add(key, wrap);
        return wrap;
    }

    @Override
    public Placeholder<Object> wrap(K key, Object comp)
    {
        BooleanPlaceholder wrap = new BooleanPlaceholder(comp);
        map.add(key, wrap);
        return wrap;
    }

    @Override
    public void attach(K key, Placeholder wrap)
    {
        map.add(key, wrap);
    }

    @Override
    public void provision(P param)
    {
        for (Entry<K,List<Placeholder>> e : map.entrySet())
        {
            K key = e.getKey();
            for (Placeholder w : e.getValue())
            {
                param.provision(key, w);
            }
        }
    }
    
}
