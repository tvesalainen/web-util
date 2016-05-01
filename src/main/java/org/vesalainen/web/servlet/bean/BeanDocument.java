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

import org.vesalainen.html.Document;

/**
 *
 * @author tkv
 * @param <M>
 */
public class BeanDocument<M> extends Document
{
    protected ThreadLocal<Context<M>> threadLocalData;

    public BeanDocument(ThreadLocal<Context<M>> threadLocalData)
    {
        this(threadLocalData, null);
    }

    public BeanDocument(ThreadLocal<Context<M>> threadLocalData, String title)
    {
        super(title);
        this.threadLocalData = threadLocalData;
    }

    @Override
    public void init()
    {
        super.init();
        getScriptContainer().addScript(new FormControl());
    }

    @Override
    public BeanForm addForm(Object action)
    {
        BeanForm form = new BeanForm(body, threadLocalData, "POST", action);
        body.addElement(form);
        return form;
    }

    @Override
    public BeanForm addForm(String method, Object action)
    {
        BeanForm form = new BeanForm(body, threadLocalData, method, action);
        body.addElement(form);
        return form;
    }

    public ThreadLocal<Context<M>> getThreadLocalData()
    {
        return threadLocalData;
    }

}
