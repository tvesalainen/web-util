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
package org.vesalainen.html.jquery;

import org.vesalainen.js.ScriptContainer;
import org.vesalainen.web.servlet.bean.BeanDocument;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <M>
 */
public abstract class JQueryDocument<M> extends BeanDocument<M>
{
    private DocumentReadyEvent readyEvent;
    
    public JQueryDocument(ThreadLocal<Context<M>> threadLocalData)
    {
        super(threadLocalData);
    }

    public JQueryDocument(ThreadLocal<Context<M>> threadLocalData, String title)
    {
        super(threadLocalData, title);
    }

    @Override
    public ScriptContainer getScriptContainer()
    {
        if (readyEvent == null)
        {
            ScriptContainer sc = super.getScriptContainer();
            readyEvent = new DocumentReadyEvent();
            sc.addScript(readyEvent);
        }
        return readyEvent;
    }
    
}
