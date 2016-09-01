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
import java.util.function.Consumer;

/**
 *
 * @author tkv
 */
public class ContainerContent extends AbstractContent implements Container
{
    private static final long serialVersionUID = 1L;
    protected final List<Renderer> content = new ArrayList<>();

    public ContainerContent()
    {
    }

    public ContainerContent(Content parent)
    {
        super(parent);
    }

    /**
     * Add text
     * @param <T>
     * @param text
     * @return this
     */
    @Override
    public <T> ContainerContent addText(T text)
    {
        addContent(new Text(this, text));
        return this;
    }
    /**
     * Add new Tag
     * @param tagName
     * @return this
     */
    @Override
    public Tag addTag(String tagName)
    {
        Tag tag = new Tag(this, tagName);
        addContent(tag);
        return tag;
    }
    /**
     * Add Element
     * @param element
     * @return new Element
     */
    @Override
    public Element addElement(String element)
    {
        Element el = new Element(this, element);
        addContent(el);
        return el;
    }
    @Override
    public ContainerContent addContent(Content c)
    {
        c.setParent(this);
        return addRenderer(c);
    }

    @Override
    public ContainerContent addRenderer(Renderer renderer)
    {
        content.add(renderer);
        return this;
    }
    @Override
    public ContainerContent insertContent(Content c)
    {
        c.setParent(this);
        return insertRenderer(c);
    }
    @Override
    public ContainerContent insertRenderer(Renderer c)
    {
        content.add(0, c);
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
        for (Renderer c : content)
        {
            c.append(out);
        }
    }

    @Override
    public void visit(Consumer<? super Renderer> consumer)
    {
        consumer.accept(this);
        for (Renderer c : content)
        {
            c.visit(consumer);
        }
    }
    
    
}
