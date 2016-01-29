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

/**
 *
 * @author tkv
 */
public interface Container extends Content
{

    ContainerContent addContent(Content c);

    /**
     * Add Element
     * @param element
     * @return new Element
     */
    Element addElement(String element);

    /**
     * Add Element
     * @param element
     * @return this
     */
    ContainerContent addElement(Element element);

    /**
     * Add new Tag
     * @param tagName
     * @return this
     */
    Tag addTag(String tagName);

    /**
     * Add Tag
     * @param tag
     * @return this
     */
    ContainerContent addTag(Tag tag);

    /**
     * Add text
     * @param text
     * @return this
     */
    ContainerContent addText(String text);
    
}