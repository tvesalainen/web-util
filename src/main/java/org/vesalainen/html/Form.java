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

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Form extends Element
{
    private static final long serialVersionUID = 1L;
    public Form(Content parent, Object action)
    {
        this(parent, null, "post", action);
    }
    public Form(Content parent, String id, String method, Object action)
    {
        super(parent, "form");
        setAttr("method", method);
        if (id != null)
        {
            setAttr("id", id);
        }
        if (action != null)
        {
            setAttr("action", action);
        }
    }
    
}
