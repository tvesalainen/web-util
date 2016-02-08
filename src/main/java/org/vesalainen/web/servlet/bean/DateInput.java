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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.vesalainen.bean.ThreadLocalBeanField;

/**
 *
 * @author tkv
 */
public class DateInput<T> extends ThreadLocalBeanField<T, Object>
{

    private String format;

    public DateInput(ThreadLocal<T> local, Class<? extends T> cls, String fieldname, String type)
    {
        super(local, cls, fieldname);
        switch (type)
        {
            case "date":
                format = "yyyy-MM-dd";
                break;
            case "datetime":
                format = "yyyy-MM-dd'T'HH:mm";
                break;
            case "datetime-local":
                format = "yyyy-MM-dd'T'HH:mm";
                break;
            case "month":
                format = "yyyy-MM";
                break;
            case "time":
                format = "HH:mm";
                break;
            case "week":
                format = "yyyy-'W'ww";
                break;
            default:
                throw new IllegalArgumentException(type+" illegal");
        }
    }

    @Override
    public Object get()
    {
        Date date = (Date) super.get();
        if (date != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
        else
        {
            return "";
        }
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        String sd = arr[0];
        if (!sd.isEmpty())
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date date = sdf.parse(sd);
                super.set(date);
            }
            catch (ParseException ex)
            {
                throw new IllegalArgumentException(ex);
            }
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
