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
import java.util.Map.Entry;
import java.util.function.BiFunction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.http.Query;
import org.vesalainen.json.JsonHelper;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.web.I18n;
import org.vesalainen.web.SingleSelector;
import org.vesalainen.web.servlet.AbstractDocumentServlet;

/**
 *
 * @author tkv
 * @param <V> View (Document) type
 * @param <M> Model type
 */
public abstract class AbstractBeanServlet<V extends BeanDocument,M> extends AbstractDocumentServlet<V>
{
    protected final ThreadLocal<M> threadLocalData;
    protected BiFunction<Class<?>,String,Object> objectFactory = BeanHelper::defaultFactory;

    public AbstractBeanServlet()
    {
        threadLocalData = new ThreadLocal<>();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        I18n.setLocale(req.getLocale());
        M data = createData();
        threadLocalData.set(data);
        String submitField = null;
        for (Entry<String,String[]> e : req.getParameterMap().entrySet())
        {
            String field = e.getKey();
            String[] arr = e.getValue();
            if (BeanHelper.hasProperty(data, field))
            {
                Object value = BeanHelper.getValue(data, field);
                if (value instanceof SingleSelector)
                {
                    SingleSelector ss = (SingleSelector) value;
                    Class[] pt = BeanHelper.getParameterTypes(data, field);
                    ss.setValue(ConvertUtility.convert(pt[0], arr[0]));
                }
                else
                {
                    if (arr.length == 1 && arr[0].isEmpty())
                    {
                        BeanHelper.setValue(data, field, null);
                    }
                    else
                    {
                        BeanHelper.setValue(data, field, arr);
                    }
                }
            }
            else
            {
                if (!BeanHelper.applyList(data, field, (Class<Object> c, String h)->{return createObject(data, field, c, h);}))
                {
                    submitField = field;
                }
            }
        }
        Query query = null;
        String queryString = req.getQueryString();
        if (queryString != null)
        {
            query = new Query(queryString);
        }
        onSubmit(data, submitField, query);
        response(resp, document);
    }
    protected abstract void onSubmit(M data, String field, Query query);

    protected abstract M createData();
    
    protected <T> T createObject(M data, String field, Class<T> cls, String hint)
    {
        return BeanHelper.defaultFactory(cls, hint);
    }
}
