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
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;
import static org.vesalainen.html.Encoder.encode;

/**
 *
 * @author tkv
 * @param <T>
 */
public class FunctionalAttribute<T> implements Attribute<Supplier<T>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String name;
    protected Supplier<T> value;

    public FunctionalAttribute(String name, Supplier<T> value)
    {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name + "=\"" + value.get() + "\"";
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append(name);
        out.append("=\"");
        if (value != null)
        {
            encode(out, value.get().toString());
        }
        out.append("\"");
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Supplier<T> getValue()
    {
        return value;
    }

    public void setValue(Supplier<T> value)
    {
        this.value = value;
    }

}
