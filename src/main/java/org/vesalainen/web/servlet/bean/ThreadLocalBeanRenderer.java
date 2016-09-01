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
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Renderer;

/**
 * ThreadLocalBeanRenderer is a building part of document. Each part has
 * reference to model by ThreadLocal. It is assumed that when building the 
 * document tree, each part uses same ThreadLocal instance. When de-serializing
 * all parts have same ThreadLocal instance.
 * @author tkv
 * @param <M>
 * @param <R>
 */
public abstract class ThreadLocalBeanRenderer<M, R extends Renderer> extends BeanRenderer<R> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Map<ObjectInputStream,ThreadLocal> ThreadLocalMap = new WeakHashMap<>();
    protected transient ThreadLocal<Context<M>> threadLocalModel;

    public ThreadLocalBeanRenderer(ThreadLocal<Context<M>> threadLocalModel)
    {
        super();
        Objects.requireNonNull(threadLocalModel);
        this.threadLocalModel = threadLocalModel;
    }

    public ThreadLocal<Context<M>> getThreadLocalModel()
    {
        return threadLocalModel;
    }

    /**
     * Return Bean pattern
     *
     * @return
     */
    public String getPattern()
    {
        Context<M> ctx = threadLocalModel.get();
        M model = ctx.getModel();
        String pattern = BeanHelper.getPattern(model, this);
        if (pattern != null)
        {
            return pattern;
        }
        throw new IllegalArgumentException("no pattern for " + model + " -> " + this);
    }

    /**
     * Return bean pattern normalized to web
     *
     * @return
     */
    public String getWebPattern()
    {
        return getPattern().replace('.', '-');
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        threadLocalModel = ThreadLocalMap.get(in);
        if (threadLocalModel == null)
        {
            threadLocalModel = new ThreadLocal();
            ThreadLocalMap.put(in, threadLocalModel);
        }
    }

}
