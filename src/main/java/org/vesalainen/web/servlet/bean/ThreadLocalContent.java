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
import org.vesalainen.html.Content;

/**
 * ThreadLocalContent is a content without parent. It got it's model from 
 * ThreadLocal.
 * @author tkv
 * @param <M> Context type
 */
public abstract class ThreadLocalContent<M> implements Content
{
    protected final ThreadLocal<M> local;

    public ThreadLocalContent(ThreadLocal<M> local)
    {
        this.local = local;
    }

    @Override
    public Content getParent()
    {
        return null;
    }
    /**
     * @param parent 
     */
    @Override
    public void setParent(Content parent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
}
