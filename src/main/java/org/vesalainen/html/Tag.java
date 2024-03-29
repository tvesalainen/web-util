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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Tag extends AbstractContent implements AttributedContent
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected Map<String,Attribute<?>> attributes;
    protected ClassAttribute classes;

    public Tag(String name)
    {
        this.name = name;
    }

    public Tag(Content parent, String name)
    {
        super(parent);
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Add entr(y/ies) to clas attribute
     * @param cls
     * @return this
     */
    @Override
    public Tag addClasses(String... cls)
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
    @Override
    public <T> Tag setAttr(String name, T value)
    {
        return setAttr(new SimpleAttribute<>(name, value));
    }

    @Override
    public <T> AttributedContent setAttr(String name, Supplier<T> value)
    {
        return setAttr(new SupplierAttribute<>(name, value));
    }

    @Override
    public <T> Tag setDataAttr(String name, T value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }

    @Override
    public <T> AttributedContent setDataAttr(String name, Supplier<T> value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }
    
    @Override
    public <T> Tag setAttr(Attribute<T> attr)
    {
        if (attr != null)
        {
            if (attributes == null)
            {
                attributes = new HashMap<>();
            }
            attributes.put(attr.getName(), attr);
        }
        return this;
    }

    @Override
    public AttributedContent setAttr(Collection<Attribute> all)
    {
        if (all != null)
        {
            for (Attribute a : all)
            {
                setAttr(a);
            }
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
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
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
