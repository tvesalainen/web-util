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
package org.vesalainen.js;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tkv
 */
public class AbstractScriptContainer implements ScriptContainer
{
    protected String prefix;
    protected String suffix;
    protected List<Script> content = new ArrayList<>();

    public AbstractScriptContainer(String prefix, String suffix)
    {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @Override
    public ScriptContainer addScript(Script script)
    {
        content.add(script);
        return this;
    }

    @Override
    public ScriptContainer addCode(String code)
    {
        addScript(new AbstractScript(code));
        return this;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append(prefix);
        for (Script script : content)
        {
            script.append(out);
        }
        out.append(suffix);
    }
    
}
