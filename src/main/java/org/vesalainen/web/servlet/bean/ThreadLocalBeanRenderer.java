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

import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Renderer;

/**
 *
 * @author tkv
 * @param <M>
 * @param <R>
 */
public abstract class ThreadLocalBeanRenderer<M,R extends Renderer> extends BeanRenderer<R>
{
    protected final ThreadLocal<Context<M>> threadLocalModel;

    public ThreadLocalBeanRenderer(ThreadLocal<Context<M>> threadLocalModel)
    {
        super();
        this.threadLocalModel = threadLocalModel;
    }
    /**
     * Return Bean pattern
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
        throw new IllegalArgumentException("no pattern for "+model+" -> "+this);
    }
    /**
     * Return bean pattern normalized to web
     * @return 
     */
    public String getWebPattern()
    {
        return getPattern().replace('.', '-');
    }
}
