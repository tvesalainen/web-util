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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.vesalainen.bean.BeanField;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Document;
import org.vesalainen.html.Form;

/**
 *
 * @author tkv
 * @param <C>
 */
public class BeanDocument<C> extends Document
{
    ThreadLocal<C> threadLocalData;
    protected C context;
    protected Class<C> dataType;
    protected final Map<String,BeanField> fieldMap = new HashMap<>();
    protected Set<String> allFields;

    public BeanDocument(ThreadLocal<C> threadLocalData)
    {
        this(threadLocalData, null);
    }

    public BeanDocument(ThreadLocal<C> threadLocalData, String title)
    {
        super(title);
        this.threadLocalData = threadLocalData;
        this.context = threadLocalData.get();
        this.dataType = (Class<C>) context.getClass();
        allFields = BeanHelper.getFields(context.getClass());
    }

    @Override
    public BeanForm addForm(Object action)
    {
        BeanForm form = new BeanForm(this, "POST", action);
        body.addElement(form);
        return form;
    }

    @Override
    public BeanForm addForm(String method, Object action)
    {
        BeanForm form = new BeanForm(this, method, action);
        body.addElement(form);
        return form;
    }

    public BeanField getBeanField(String field)
    {
        return fieldMap.get(field);
    }

    public Map<String, BeanField> getFieldMap()
    {
        return fieldMap;
    }

    public Set<String> getAllFields()
    {
        return allFields;
    }

    public ThreadLocal<C> getThreadLocalData()
    {
        return threadLocalData;
    }

    public Class<C> getDataType()
    {
        return dataType;
    }

}
