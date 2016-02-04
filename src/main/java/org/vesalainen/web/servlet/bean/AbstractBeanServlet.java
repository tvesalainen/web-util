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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanField;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.web.servlet.AbstractDocumentServlet;

/**
 *
 * @author tkv
 * @param <D> Document type
 * @param <C> Context type
 */
public abstract class AbstractBeanServlet<D extends BeanDocument,C> extends AbstractDocumentServlet<D>
{
    protected final ThreadLocal<C> threadLocalData;
    protected C empty;
    protected Class<C> dataType;

    public AbstractBeanServlet()
    {
        threadLocalData = new ThreadLocal<>();
    }

    @Override
    public void init() throws ServletException
    {
        empty = createData();
        dataType = (Class<C>) empty.getClass();
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        C data = createData();
        threadLocalData.set(data);
        for (Entry<String,String[]> e : req.getParameterMap().entrySet())
        {
            String field = e.getKey();
                BeanField bf = document.getBeanField(field);
            if (bf == null)
            {
                throw new IllegalArgumentException(field+" not found");
            }
            bf.set(e.getValue());
        }
        response(resp, document);
    }

    protected abstract C createData();
}
