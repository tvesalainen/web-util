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

import java.util.List;
import java.util.Set;
import org.vesalainen.bean.ThreadLocalBeanField;
import org.vesalainen.util.Lists;
import org.vesalainen.web.MultipleSelector;

/**
 *
 * @author tkv
 * @param <D> Base type
 * @param <T> Item type
 */
public class MultipleSelectorInput<D,T> extends ThreadLocalBeanField<D,MultipleSelector<T>>
{
    private final List<T> options;
    
    public MultipleSelectorInput(ThreadLocal<D> local, Class<? extends D> cls, String fieldname, List<T> options)
    {
        super(local, cls, fieldname);
        this.options = options;
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        MultipleSelector selector = get();
        Set set = selector.getValues();
        set.clear();
        for (String name : arr)
        {
            for (T opt : options)
            {
                if (opt.toString().equals(name))
                {
                    set.add(opt);
                }
            }
        }
    }

    public Value getValue(T opt)
    {
        return new Value(opt);
    }
    
    public class Value
    {
        private final T value;

        public Value(T value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            MultipleSelector selector = get();
            Set set = selector.getValues();
            if (set.contains(value))
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
