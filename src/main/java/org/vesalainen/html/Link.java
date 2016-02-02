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
 */
public class Link implements Attribute<String>
{
    private final String name;
    private final String path;
    private final Content query;

    public Link(String name, String path, Content query)
    {
        this.name = name;
        this.path = path;
        this.query = query;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getValue()
    {
        return path+'?'+query;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append(name);
        out.append("=\"");
        out.append(path);
        out.append('?');
        query.append(out);
        out.append("\"");
    }
    
}
