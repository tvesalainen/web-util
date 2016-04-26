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

import java.util.Set;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Document;

/**
 *
 * @author tkv
 * @param <M>
 */
public class BeanDocument<M> extends Document
{
    ThreadLocal<M> threadLocalData;
    protected M context;
    protected Class<M> dataType;
    //protected final Map<String,BeanField> fieldMap = new HashMap<>();
    protected Set<String> allFields;

    public BeanDocument(ThreadLocal<M> threadLocalData)
    {
        this(threadLocalData, null);
    }

    public BeanDocument(ThreadLocal<M> threadLocalData, String title)
    {
        super(title);
        this.threadLocalData = threadLocalData;
        this.context = threadLocalData.get();
        this.dataType = (Class<M>) context.getClass();
        allFields = BeanHelper.getProperties(context.getClass());
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

    public Set<String> getAllFields()
    {
        return allFields;
    }

    public ThreadLocal<M> getThreadLocalData()
    {
        return threadLocalData;
    }

    public Class<M> getDataType()
    {
        return dataType;
    }

}
