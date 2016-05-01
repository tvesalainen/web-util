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
package org.vesalainen.web.servlet.bean;

import org.vesalainen.util.Bijection;
import org.vesalainen.util.HashBijection;

/**
 *
 * @author tkv
 * @param <M>
 */
public class Context<M>
{
    private M model;
    private Bijection<String,String> map = new HashBijection<>();
    private int number;

    public Context(M model)
    {
        this.model = model;
    }

    public String inputName(String name)
    {
        String id = map.getSecond(name);
        if (id == null)
        {
            id = "id"+number;
            number++;
            map.put(name, id);
        }
        System.err.println(name+" -> "+id);
        return id;
    }
    public String modelName(String inputName)
    {
        String first = map.getFirst(inputName);
        System.err.println(inputName+" -> "+first);
        return first;
    }
    public M getModel()
    {
        return model;
    }
    
}
