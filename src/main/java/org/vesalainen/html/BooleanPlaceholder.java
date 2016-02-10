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
 *
 * @author tkv
 * @param <T>
 */
public class BooleanPlaceholder<T> extends Placeholder<T>
{
    private final Object comp;

    public BooleanPlaceholder(Object comp)
    {
        this.comp = comp;
    }

    public BooleanPlaceholder(Object comp, T value)
    {
        super(value);
        this.comp = comp;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        if (value != null && value.equals(comp))
        {
            super.append(out);
        }
    }

    @Override
    public String toString()
    {
        if (value != null && value.equals(comp))
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }

}
