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
 * @param <T>
 */
public class DataAttribute<T> extends SimpleAttribute<T>
{
    private final String camelCased;
    
    public DataAttribute(String name, T value)
    {
        super("data-"+name, value);
        camelCased = name.replace('-', '.');
    }

    public String getCamelCased()
    {
        return camelCased;
    }
    
}