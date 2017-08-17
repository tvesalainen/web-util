/*
 * Copyright (C) 2016 Timo Vesalainen <timo.vesalainen@iki.fi>
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
import java.util.function.Consumer;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T>
 */
public class Placeholder<T> extends AbstractContent
{
    private static final long serialVersionUID = 1L;
    protected T value;

    public Placeholder(Content parent)
    {
        this(parent, null);
    }

    public Placeholder(Content parent, T value)
    {
        super(parent);
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return Contents.toString(value);
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Contents.append(out, value);
    }

    @Override
    public void visit(Consumer<? super Renderer> consumer)
    {
        Contents.visit(value, consumer);
    }
    
}
