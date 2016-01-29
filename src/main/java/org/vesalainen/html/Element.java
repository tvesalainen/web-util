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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tkv
 */
public class Element extends Container
{
    protected String name;
    protected List<Attribute<?>> attributes;
    protected ClassAttribute classes;

    public Element(String name)
    {
        this.name = name;
    }

    public void addClasses(String... cls)
    {
        if (classes == null)
        {
            classes = new ClassAttribute(cls);
            addAttr(classes);
        }
        else
        {
            classes.addClasses(cls);
        }
    }
    
    
    public <T> Element addAttr(String name, T value)
    {
        return addAttr(new Attribute<>(name, value));
    }

    public <T> Element addAttr(Attribute<T> attr)
    {
        if (attributes == null)
        {
            attributes = new ArrayList<>();
        }
        attributes.add(attr);
        return this;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append('<');
        out.append(name);
        if (attributes != null)
        {
            for (Attribute<?> attr : attributes)
            {
                out.append(' ');
                attr.append(out);
            }
        }
        out.append('>');
        super.append(out);
        out.append("</");
        out.append(name);
        out.append('>');
    }
    
    
}
