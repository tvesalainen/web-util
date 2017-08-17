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
import java.util.ArrayList;
import java.util.List;
import org.vesalainen.html.AbstractContent;
import org.vesalainen.html.Contents;
import org.vesalainen.html.Renderer;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class AbstractScriptContainer implements ScriptContainer
{
    protected Object prefix;
    protected Object suffix;
    protected List<Renderer> content = new ArrayList<>();

    public AbstractScriptContainer()
    {
    }

    public AbstractScriptContainer(Object prefix, String suffix)
    {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @Override
    public ScriptContainer addScript(Renderer script)
    {
        content.add(script);
        return this;
    }

    @Override
    public ScriptContainer addCode(Object code)
    {
        addScript(new AbstractScript(code));
        return this;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Contents.append(out, prefix);
        for (Renderer script : content)
        {
            script.append(out);
        }
        Contents.append(out, suffix);
    }
    
}
