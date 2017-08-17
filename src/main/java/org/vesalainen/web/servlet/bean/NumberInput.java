/*
 * Copyright (C) 2016 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.web.servlet.bean;

import org.vesalainen.bean.ThreadLocalBeanField;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T>
 */
public class NumberInput<T> extends ThreadLocalBeanField<T,Object>
{

    public NumberInput(ThreadLocal<T> local, Class<? extends T> cls, String fieldname)
    {
        super(local, cls, fieldname);
        if (!isInteger(type))
        {
            throw new IllegalArgumentException(type+" not integer");
        }
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        String n = arr[0];
        if (n.isEmpty())
        {
            super.set(0);
        }
        else
        {
            super.set(n);
        }
    }
    
    public static boolean isInteger(Class type)
    {
        return 
                int.class.equals(type) ||
                long.class.equals(type) ||
                short.class.equals(type) ||
                Short.class.equals(type) ||
                Integer.class.equals(type) ||
                Long.class.equals(type);
    }

    @Override
    public String toString()
    {
        Object value = get();
        if (value != null)
        {
            return value.toString();
        }
        else
        {
            return "";
        }
    }
    
}
