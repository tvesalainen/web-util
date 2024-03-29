/*
 * Copyright (C) 2021 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.html.jstree;

import org.vesalainen.html.AbstractFramework;
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Framework;
import org.vesalainen.html.jquery.JQuery;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class JsTree extends AbstractFramework
{
    private static final String Dir = "/jstree-%1$s-dist/";
    private static final String Min = Dir+"jstree.min.js";
    private static final String CSS = Dir+"themes/default/style.min.css";

    public JsTree()
    {
        this("3.3.11", new JQuery());
    }

    public JsTree(String version, Framework... dependencies)
    {
        super(version, dependencies);
    }

    @Override
    protected String path(String version)
    {
        return String.format(Min, version);
    }

    @Override
    public void useIn(Document page)
    {
        Element head = page.getHead();
        head.addTag("link")
                .setAttr("rel", "stylesheet")
                .setAttr("href", String.format(CSS, version));
        head.addElement("script")
                .setAttr("src", String.format(Min, version));
    }
    
}
