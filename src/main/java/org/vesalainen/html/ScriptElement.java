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
import org.vesalainen.js.Script;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class ScriptElement extends AbstractContent
{
    private static final long serialVersionUID = 1L;
    private Element element;

    public ScriptElement(Content parent)
    {
        super(parent);
        element = new Element(parent, "script");
    }
    
    public ScriptElement(Content parent, Script script)
    {
        super(parent);
        element = new Element(parent, "script");
        element.addRenderer(script);
    }
    
    public void addScript(Script script)
    {
        element.addRenderer(script);
    }
    @Override
    public void append(Appendable out) throws IOException
    {
        element.append(out);
    }

    @Override
    public void visit(Consumer<? super Renderer> consumer)
    {
        element.visit(consumer);
    }
    
}
