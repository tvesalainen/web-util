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

import java.io.IOException;
import org.vesalainen.html.Renderer;
import org.vesalainen.js.Function;
import org.vesalainen.js.Script;
import org.vesalainen.js.ScriptContainer;

/**
 *
 * @author tkv
 */
public class SelectorFunction extends AbstractSelector implements ScriptContainer
{
    private Function function;
    
    public SelectorFunction(String selector, String action, String... args)
    {
        super(selector, action);
        this.function = new Function(null, args);
    }

    @Override
    protected void appendArgs(Appendable out) throws IOException
    {
        function.append(out);
    }

    @Override
    public ScriptContainer addScript(Renderer script)
    {
        function.addScript(script);
        return function;
    }

    @Override
    public ScriptContainer addCode(Object code)
    {
        function.addCode(code);
        return function;
    }
    
}
