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

import java.util.EnumSet;
import org.vesalainen.bean.ThreadLocalBeanField;

/**
 *
 * @author tkv
 * @param <T>
 */
public class EnumSetInput<T> extends ThreadLocalBeanField<T,EnumSet>
{
    private Class<? extends Enum> enumType;
    private Enum[] constants;
    
    public EnumSetInput(ThreadLocal<T> local, Class<? extends T> cls, Class<? extends Enum> enumType, String fieldname)
    {
        super(local, cls, fieldname);
        this.enumType = enumType;
        if (!type.equals(EnumSet.class))
        {
            throw new IllegalArgumentException(fieldname+" is not EnumSet");
        }
        constants = enumType.getEnumConstants();
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        EnumSet es = get();
        es.clear();
        for (String name : arr)
        {
            for (Enum en : constants)
            {
                if (en.name().equals(name))
                {
                    es.add(en);
                }
            }
        }
    }

    public Enum[] getConstants()
    {
        return constants;
    }
    
    public Value getValue(Enum en)
    {
        if (!en.getDeclaringClass().equals(enumType))
        {
            throw new IllegalArgumentException(en+" not same enum as "+enumType);
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
            EnumSet es = get();
            if (es.contains(value))
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
