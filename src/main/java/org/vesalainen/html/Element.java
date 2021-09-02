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
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Element extends ContainerContent implements AttributedContent
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected Map<String,Attribute<?>> attributes;
    protected ClassAttribute classes;

    public Element(String name)
    {
        this.name = name;
    }

    public Element(Content parent, String name)
    {
        super(parent);
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Element addContent(Content c)
    {
        super.addContent(c);
        return this;
    }

    @Override
    public Tag addTag(String tagName)
    {
        return super.addTag(tagName);
    }

    @Override
    public <T> Element addText(T text)
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
            setAttr(classes);
        }
        else
        {
            classes.addClasses(cls);
        }
        return this;
    }
    /**
     * Set SimpleAttribute
     * @param <T>
     * @param name
     * @param value
     * @return this
     */
    @Override
    public <T> Element setAttr(String name, T value)
    {
        return setAttr(new SimpleAttribute<>(name, value));
    }
    /**
     * Set functional attribute.
     * @param <T>
     * @param name
     * @param value
     * @return 
     */
    @Override
    public <T> Element setAttr(String name, Supplier<T> value)
    {
        return setAttr(new SupplierAttribute<>(name, value));
    }

    @Override
    public <T> Element setDataAttr(String name, T value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }

    @Override
    public <T> Element setDataAttr(String name, Supplier<T> value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }
    
    /**
     * Set Attribute
     * @param <T>
     * @param attr
     * @return this
     */
    @Override
    public <T> Element setAttr(Attribute<T> attr)
    {
        if (attributes == null)
        {
            attributes = new TreeMap<>();
        }
        attributes.put(attr.getName(), attr);
        return this;
    }

    @Override
    public AttributedContent setAttr(Collection<Attribute> all)
    {
        for (Attribute a : all)
        {
            setAttr(a);
        }
        return this;
    }

    @Override
    public AttributedContent removeAttr(String name)
    {
        if (attributes != null)
        {
            attributes.remove(name);
        }
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
        if (attributes != null)
        {
            return attributes.containsKey(name);
        }
        return false;
    }

    @Override
    public Attribute<?> getAttr(String name)
    {
        if (attributes != null)
        {
            return attributes.get(name);
        }
        return null;
    }

}
