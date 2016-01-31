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
package org.vesalainen.html;

import java.io.IOException;

/**
 * Implements HTML boolean attribute. Boolean attribute shows only name when
 * value != null and value.toString() == 'true'
 * @author tkv
 * @param <T>
 */
public class BooleanAttribute<T> extends Attribute<T>
{
    public BooleanAttribute(String name, T value)
    {
        super(name, value);
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        if (value != null && "true".equals(value.toString()))
        {
            out.append(name);
        }
    }

}
