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
package org.vesalainen.html.bootstrap;

import org.vesalainen.html.AbstractFramework;
import org.vesalainen.html.Framework;
import org.vesalainen.html.Page;
import org.vesalainen.html.Tag;
import org.vesalainen.html.jquery.JQuery;

/**
 *
 * @author tkv
 */
public class Bootstrap extends AbstractFramework
{
    private static final String Dir = "/bootstrap-%s-dist/";
    private static final String Min = Dir+"js/bootstrap.min.js";
    private static final String CSS = Dir+"css/bootstrap.min.css";

    public Bootstrap()
    {
        super("3.3.6", new JQuery());
    }

    public Bootstrap(String version, Framework... dependencies)
    {
        super(version, new JQuery());
    }
    
    @Override
    public void useIn(Page page)
    {
        Tag head = page.getHead();
        head.addTag("meta")
                .addAttr("name", "viewport")
                .addAttr("content", "width=device-width, initial-scale=1");
        head.addTag("link")
                .addAttr("rel", "stylesheet")
                .addAttr("href", String.format(CSS, version));
        head.addTag("script")
                .addAttr("src", String.format(Min, version));
    }

    @Override
    protected String path(String version)
    {
        return String.format(Min, version);
    }
    
}
