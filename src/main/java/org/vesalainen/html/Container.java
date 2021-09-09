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

import java.util.Collection;
import java.util.function.Supplier;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public interface Container extends Content
{
    default ContainerContent add(Renderer renderer)
    {
        if (renderer instanceof Content)
        {
            return addContent((Content)renderer);
        }
        return addRenderer(renderer);
    }
    default ContainerContent insert(Renderer renderer)
    {
        if (renderer instanceof Content)
        {
            return insertContent((Content)renderer);
        }
        return insertRenderer(renderer);
    }
    ContainerContent addContent(Content c);
    default void addAll(Collection<Renderer> c)
    {
        c.forEach((i)->add(i));
    }
    ContainerContent addRenderer(Renderer c);
    ContainerContent insertContent(Content c);
    ContainerContent insertRenderer(Renderer c);
    Element addElement(String element);
    Tag addTag(String tagName);
    <T> ContainerContent addText(T text);
    <T> ContainerContent addText(Supplier<String> text);
}
