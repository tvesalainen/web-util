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
public final class InputTag extends Tag
{

    public InputTag(String type, String name)
    {
        super("input");
        setAttr("type", type);
        setAttr("name", name);
    }

    @Override
    public <T> InputTag setAttr(Attribute<T> attr)
    {
        super.setAttr(attr);
        return this;
    }

    @Override
    public <T> InputTag setAttr(String name, T value)
    {
        super.setAttr(name, value);
        return this;
    }
    
}
