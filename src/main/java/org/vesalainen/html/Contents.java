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
 */
public class Contents
{
    public static String toString(Object value)
    {
        if (value != null)
        {
            return value.toString();
        }
        else
        {
            return "";
        }
    }
    public static void append(Appendable out, Object value) throws IOException
    {
        if (value != null)
        {
            if (value instanceof Content)
            {
                Content content = (Content) value;
                content.append(out);
            }
            else
            {
                out.append(value.toString());
            }
        }
    }
    public static void visit(Object value, Consumer<? super Renderer> action)
    {
        if (value != null)
        {
            if (value instanceof Content)
            {
                Content content = (Content) value;
                content.visit(action);
            }
        }
    }
}
