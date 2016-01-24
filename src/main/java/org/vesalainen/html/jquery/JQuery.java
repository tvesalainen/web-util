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
package org.vesalainen.html.jquery;

import org.vesalainen.html.AbstractFramework;
import org.vesalainen.html.Framework;
import org.vesalainen.html.Page;
import org.vesalainen.html.Tag;

/**
 *
 * @author tkv
 */
public class JQuery extends AbstractFramework
{
    private static String Format = "/jquery-%s.min.js";
    
    public JQuery()
    {
        super("1.12.0");
    }

    public JQuery(String version)
    {
        super(version);
    }

    @Override
    public void useIn(Page page)
    {
        Tag head = page.getHead();
        head.addTag("script")
                .addAttr("src", path(version));
    }

    @Override
    protected String path(String version)
    {
        return String.format(Format, version);
    }
    
}
