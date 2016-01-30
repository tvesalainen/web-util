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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tkv
 */
public class Element extends ContainerContent implements AttributedContent
{
    protected String name;
    protected Map<String,Attribute<?>> attributes;
    protected ClassAttribute classes;

    public Element(String name)
    {
        this.name = name;
    }

    @Override
    public Element addContent(Content c)
    {
        super.addContent(c);
        return this;
    }

    @Override
    public Element addElement(Element element)
    {
        super.addElement(element);
        return this;
    }

    @Override
    public Element addTag(Tag tag)
    {
        return (Element) super.addTag(tag);
    }

    @Override
    public Tag addTag(String tagName)
    {
        return super.addTag(tagName);
    }

    @Override
    public Element addText(String text)
    {
        return (Element) super.addText(text);
    }
    
    /**
     * Add entr(y/ies) to clas attribute
     * @param cls
     * @return this
     */
    @Override
    public Element addClasses(String... cls)
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
        return this;
    }
    /**
     * Add Attribute
     * @param <T>
     * @param name
     * @param value
     * @return this
     */
    @Override
    public <T> Element addAttr(String name, T value)
    {
        return addAttr(new Attribute<>(name, value));
    }
    /**
     * Add Attribute
     * @param <T>
     * @param attr
     * @return this
     */
    @Override
    public <T> Element addAttr(Attribute<T> attr)
    {
        if (attributes == null)
        {
            attributes = new HashMap<>();
        }
        attributes.put(attr.getName(), attr);
        return this;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append('<');
        out.append(name);
        if (attributes != null)
        {
            for (Attribute<?> attr : attributes.values())
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

    @Override
    public boolean hasAttr(String name)
    {
        return attributes.containsKey(name);
    }

    @Override
    public Attribute<?> getAttr(String name)
    {
        return attributes.get(name);
    }

}
