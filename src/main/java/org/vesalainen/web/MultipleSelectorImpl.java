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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.vesalainen.util.Lists;

/**
 *
 * @author tkv
 * @param <T>
 */
public class MultipleSelectorImpl<T> extends HashSet<T> implements MultipleSelector<T>
{
    private final List<T> options;

    public MultipleSelectorImpl(T... options)
    {
        this(new HashSet<T>(), Lists.create(options));
    }

    public MultipleSelectorImpl(List<T> options)
    {
        this(new HashSet<T>(), options);
    }

    public MultipleSelectorImpl(Set<T> values, List<T> options)
    {
        addAll(values);
        this.options = options;
    }

    @Override
    public List<T> getOptions()
    {
        return options;
    }
    
}
