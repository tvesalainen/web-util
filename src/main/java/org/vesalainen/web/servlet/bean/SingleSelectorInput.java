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
import org.vesalainen.bean.ThreadLocalBeanField;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.web.SingleSelector;

/**
 *
 * @author tkv
 * @param <D> Base type
 * @param <T> Item type
 */
public class SingleSelectorInput<D,T> extends ThreadLocalBeanField<D,SingleSelector<T>>
{
    private final List<T> options;
    private Class<T> optType;
    
    public SingleSelectorInput(ThreadLocal<D> local, Class<? extends D> cls, String fieldname, SingleSelector selector)
    {
        super(local, cls, fieldname);
        this.options = selector.getOptions();
        if (options.isEmpty())
        {
            throw new IllegalArgumentException(fieldname+" has empty options");
        }
        optType = (Class<T>) options.get(0).getClass();
    }

    @Override
    public void set(Object value)
    {
        if (value != null)
        {
            String[] arr = (String[]) value;
            SingleSelector selector = get();
            selector.setValue(ConvertUtility.convert(optType, arr[0]));
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
            SingleSelector selector = get();
            Object cur = selector.getValue();
            if (value.equals(cur))
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
