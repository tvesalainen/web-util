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
package org.vesalainen.js;

import java.io.IOException;
import org.vesalainen.util.Lists;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Function extends AbstractScriptContainer
{
    /**
     * Creates javascript function
     * @param name Function name or null if anonymous
     * @param args 
     */
    public Function(String name, String... args)
    {
        super(create(name, args), "}");
    }
    private static String create(String name, String... args)
    {
        StringBuilder sb = new StringBuilder();
        if (name != null)
        {
            sb.append("function ");
            sb.append(name);
        }
        else
        {
            sb.append("function");
        }
        try
        {
            Lists.print(sb, "(", ",", null, null, "){", args);
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
        return sb.toString();
    }
    
}
