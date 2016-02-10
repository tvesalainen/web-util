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

/**
 *
 * @author tkv
 * @param <K> Key type
 * @param <P> Param type
 */
public interface DynContent<P extends DynParam<K>,K>
{
    void append(P param, Appendable out) throws IOException;
    
    Placeholder<Object> wrap(K key);
    
    Placeholder<Object> wrap(K key, Object comp);
    
    void attach(K key, Placeholder wrap);
    
    void provision(P param);
}
