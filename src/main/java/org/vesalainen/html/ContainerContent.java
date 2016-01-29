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
public class ContainerContent implements Container
{
    protected List<Content> content;

    public ContainerContent()
    {
    }
    /**
     * Add text
     * @param text
     * @return this
     */
    @Override
    public ContainerContent addText(String text)
    {
        addContent(new Text(text));
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
        Tag tag = new Tag(tagName);
        addTag(tag);
        return tag;
    }
    /**
     * Add Tag
     * @param tag
     * @return this
     */
    @Override
    public ContainerContent addTag(Tag tag)
    {
        addContent(tag);
        return this;
    }
    /**
     * Add Element
     * @param element
     * @return new Element
     */
    @Override
    public Element addElement(String element)
    {
        Element el = new Element(element);
        addElement(el);
        return el;
    }
    /**
     * Add Element
     * @param element
     * @return this
     */
    @Override
    public ContainerContent addElement(Element element)
    {
        addContent(element);
        return this;
    }

    @Override
    public ContainerContent addContent(Content c)
    {
        if (content == null)
        {
            content = new ArrayList<>();
        }
        content.add(c);
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
        if (content != null)
        {
            for (Content c : content)
            {
                c.append(out);
            }
        }
    }
    
}
