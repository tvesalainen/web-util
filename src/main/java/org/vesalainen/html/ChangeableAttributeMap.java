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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;

/**
 *
 * @author tkv
 * @param <E>
 */
public class ChangeableAttributeMap<E extends Enum<E>> implements Changeable
{
    private EnumMap<E,BooleanAttribute> map;

    public ChangeableAttributeMap(Class<E> cls)
    {
        map = new EnumMap(cls);
    }

    public void add(E en, BooleanAttribute ba)
    {
        map.put(en, ba);
    }
    
    @Override
    public void change(Object value)
    {
        if (Enum.class.equals(value.getClass()))
        {
            Enum<E> en = (Enum<E>) value;
            for (Entry<E, BooleanAttribute> e : map.entrySet())
            {
                E key = e.getKey();
                BooleanAttribute v = e.getValue();
                if (key.equals(en))
                {
                    v.change(true);
                }
                else
                {
                    v.change(false);
                }
            }
        }
        else
        {
            if (EnumSet.class.equals(value.getClass()))
            {
                EnumSet<E> es = (EnumSet<E>) value;
                for (Entry<E, BooleanAttribute> e : map.entrySet())
                {
                    E key = e.getKey();
                    BooleanAttribute v = e.getValue();
                    if (es.contains(key))
                    {
                        v.change(true);
                    }
                    else
                    {
                        v.change(false);
                    }
                }
            }
            else
            {
                throw new IllegalArgumentException(value+" not enum or EnumSet");
            }
        }
    }
}
