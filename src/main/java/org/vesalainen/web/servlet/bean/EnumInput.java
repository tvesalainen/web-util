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
public class EnumInput<T> extends ThreadLocalBeanField<T,Enum>
{
    private Enum[] constants;
    
    public EnumInput(ThreadLocal<T> local, Class<? extends T> cls, String fieldname)
    {
        super(local, cls, fieldname);
        if (!type.isEnum())
        {
            throw new IllegalArgumentException(fieldname+" is not enum");
        }
        constants = (Enum[]) type.getEnumConstants();
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        super.set(arr[0]);
    }

    public Enum[] getConstants()
    {
        return constants;
    }
    
    public Value getValue(Enum en)
    {
        if (!en.getDeclaringClass().equals(type))
        {
            throw new IllegalArgumentException(en+" not same enum as "+type);
        }
        return new Value(en);
    }
    
    public class Value
    {
        private final Enum value;

        public Value(Enum value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            if (value.equals(get()))
            {
                return "true";
            }
            else
            {
                return "false";
            }
        }
        
    }
}
