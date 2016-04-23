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
package org.vesalainen.html.jquery;

import org.vesalainen.js.ScriptContainer;
import org.vesalainen.web.servlet.bean.BeanDocument;

/**
 *
 * @author tkv
 * @param <C>
 */
public class JQueryDocument<C> extends BeanDocument<C>
{
    private DocumentReadyEvent readyEvent;
    
    public JQueryDocument(ThreadLocal<C> threadLocalData)
    {
        super(threadLocalData);
    }

    public JQueryDocument(ThreadLocal<C> threadLocalData, String title)
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
