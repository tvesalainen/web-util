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
package org.vesalainen.web.servlet.bean;

import java.awt.Color;
import org.vesalainen.bean.ThreadLocalBeanField;

/**
 *
 * @author tkv
 */
public class ColorInput<T> extends ThreadLocalBeanField<T,Object>
{

    public ColorInput(ThreadLocal<T> local, Class<? extends T> cls, String fieldname)
    {
        super(local, cls, fieldname);
    }

    @Override
    public Object get()
    {
        Object value = super.get();
        if (value instanceof Color)
        {
            Color color = (Color) value;
            return String.format("#%06x", color.getRGB() & 0xffffff);
        }
        if (value instanceof Number)
        {
            Number number = (Number) value;
            return String.format("#%06x", number.intValue());
        }
        return "";
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        String color = arr[0];
        if (color.startsWith("#"))
        {
            int ci = Integer.parseInt(color.substring(1), 16);
            super.set(ci);
        }
        else
        {
            throw new IllegalArgumentException(color+" not a color");
        }
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
