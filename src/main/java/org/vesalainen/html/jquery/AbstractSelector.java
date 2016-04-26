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
import org.vesalainen.html.AbstractContent;
import org.vesalainen.js.Script;

/**
 *
 * @author tkv
 */
public abstract class AbstractSelector extends AbstractContent implements Script
{
    private final String selector;
    private final String action;

    public AbstractSelector(String selector, String action)
    {
        this.selector = selector;
        this.action = action;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append("$(");
        if (selector != null)
        {
            out.append('"');
            out.append(selector);
            out.append('"');
        }
        else
        {
            out.append("this");
        }
        out.append(").");
        out.append(action);
        out.append("(");
        appendArgs(out);
        out.append(");");
    }
    protected abstract void appendArgs(Appendable out) throws IOException;
    
}
