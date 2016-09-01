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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.vesalainen.util.Lists;

/**
 *
 * @author tkv
 */
public class ClassAttribute extends SimpleAttribute<Set<String>>
{
    private static final long serialVersionUID = 1L;

    public ClassAttribute(String... classes)
    {
        super("class", new HashSet<>());
        Collections.addAll(value, classes);
    }
    
    public void addClasses(String... cls)
    {
        Collections.addAll(value, cls);
    }
    
    public void removeClasses(String... cls)
    {
        Lists.remove(value, cls);
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        out.append("class=");
        Lists.print(out, "\"", " ", null, null, "\"", value);
    }
    
}
