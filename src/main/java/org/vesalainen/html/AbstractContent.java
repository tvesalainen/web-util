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

import java.io.Serializable;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class AbstractContent implements Content, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Content parent;

    public AbstractContent()
    {
    }

    protected AbstractContent(Content parent)
    {
        this.parent = parent;
    }

    @Override
    public Content getParent()
    {
        return parent;
    }

    @Override
    public void setParent(Content parent)
    {
        this.parent = parent;
    }
    
}
