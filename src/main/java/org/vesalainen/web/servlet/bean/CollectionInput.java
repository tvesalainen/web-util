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

import java.io.IOException;
import java.util.Collection;
import org.vesalainen.bean.ThreadLocalBeanField;
import org.vesalainen.html.InputTag;
import org.vesalainen.util.ConvertUtility;

/**
 *
 * @author tkv
 * @param <T>
 */
public class CollectionInput<T> extends ThreadLocalBeanField<T,Collection>
{
    private Class<?> itemType;
    private String field;
    
    public CollectionInput(ThreadLocal<T> local, Class<? extends T> cls, Class<?> itemType, String fieldname)
    {
        super(local, cls, fieldname);
        if (Object.class.equals(itemType))
        {
            throw new IllegalArgumentException("itemType missing");
        }
        this.itemType = itemType;
        this.field = fieldname;
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        Collection col = get();
        col.clear();
        if (!(arr.length == 1 && arr[0].isEmpty())) // empty
        {
            for (String str : arr)
            {
                col.add(ConvertUtility.convert(itemType, str));
            }
        }
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            InputTag input = new InputTag("text", field);
            Collection col = get();
            if (!col.isEmpty())
            {
                for (Object ob : col)
                {
                    input.setAttr("value", ob);
                    input.append(sb);
                }
            }
            else
            {
                input.append(sb);
            }
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    
}
