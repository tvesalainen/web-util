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

import java.util.Collection;

/**
 *
 * @author tkv
 */
public interface AttributedContent extends Content
{
    /**
     * Set Attribute
     * @param <T>
     * @param name
     * @param value
     * @return this
     */
    <T> AttributedContent setAttr(String name, T value);
    /**
     * Set HTML5 data- attribute
     * @param <T>
     * @param name Name without data- prefix
     * @param value
     * @return 
     */
    <T> AttributedContent setDataAttr(String name, T value);
    /**
     * Set Attribute
     * @param <T>
     * @param attr
     * @return this
     */
    <T> AttributedContent setAttr(Attribute<T> attr);
    
    AttributedContent setAttr(Collection<Attribute> all);
    
    AttributedContent removeAttr(String name);
    /**
     * Add entr(y/ies) to clas attribute
     * @param cls
     * @return this
     */
    AttributedContent addClasses(String... cls);
    
    boolean hasAttr(String name);
    
    Attribute<?> getAttr(String name);
}
