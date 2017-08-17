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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Checker
{
    public static final void checkIds(Renderer renderer)
    {
        Set<Object> set = new HashSet<>();
        renderer.stream().forEach((r)->
        {
            if (r instanceof AttributedContent)
            {
                AttributedContent ac = (AttributedContent) r;
                Attribute<?> attr = ac.getAttr("id");
                if (attr != null)
                {
                    Object id = attr.getValue();
                    if (set.contains(id))
                    {
                        throw new IllegalArgumentException(id+" is not unique id");
                    }
                    set.add(id);
                }
            }
        });
    }
}
