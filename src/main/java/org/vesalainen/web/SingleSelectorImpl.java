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
package org.vesalainen.web;

import java.util.List;
import org.vesalainen.html.Contents;
import org.vesalainen.util.Lists;

/**
 *
 * @author tkv
 * @param <T>
 */
public class SingleSelectorImpl<T> implements SingleSelector<T>
{
    private T value;
    private final List<T> options;

    public SingleSelectorImpl(T... options)
    {
        this(null, Lists.create(options));
    }

    public SingleSelectorImpl(List<T> options)
    {
        this(null, options);
    }

    public SingleSelectorImpl(T value, List<T> options)
    {
        this.options = options;
        this.value = value;
    }

    @Override
    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public T getValue()
    {
        return value;
    }

    @Override
    public List<T> getOptions()
    {
        return options;
    }

    @Override
    public String toString()
    {
        return Contents.toString(value);
    }
    
}
