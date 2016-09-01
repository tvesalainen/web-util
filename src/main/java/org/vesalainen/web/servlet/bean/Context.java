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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.vesalainen.html.Renderer;
import org.vesalainen.util.Bijection;
import org.vesalainen.util.HashBijection;

/**
 * Model might contain ThreadLocalBeanRenderer parts. These parts must have
 * same ThreadLocal as this class. After re-serialization this is also true.
 * @author tkv
 * @param <M>
 */
public class Context<M> extends ThreadLocalBeanRenderer implements Serializable
{
    private static final long serialVersionUID = 1L;
    private M model;
    private Bijection<String,String> map = new HashBijection<>();
    private int number;

    public Context(ThreadLocal<Context<M>> threadLocalModel, M model)
    {
        super(threadLocalModel);
        this.model = model;
        threadLocalModel.set(this);
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
        if (first == null)
        {
            first = inputName.replace('-', '.');
        }
        return first;
    }
    public M getModel()
    {
        return model;
    }

    @Override
    protected Renderer create()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        threadLocalModel.set(this);
    }

}
