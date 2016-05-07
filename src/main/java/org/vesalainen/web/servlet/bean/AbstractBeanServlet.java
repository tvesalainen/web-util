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
import javax.servlet.http.HttpSession;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Renderer;
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
    private static final String Model = "__mOdEl__";
    protected final ThreadLocal<Context<M>> threadLocalModel;
    protected BiFunction<Class<?>,String,Object> objectFactory = BeanHelper::defaultFactory;

    public AbstractBeanServlet()
    {
        threadLocalModel = new ThreadLocal<>();
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        i18nSupport = I18n.camelCaseI18n;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.err.println(req);
        I18n.set(i18nSupport, req.getLocale());
        HttpSession session = req.getSession(true);
        M model;
        Context<M> context = (Context<M>) session.getAttribute(Model);
        if (context == null)
        {
            model = createData();
            context = new Context(model);
            session.setAttribute(Model, context);
        }
        else
        {
            model = context.getModel();
        }
        threadLocalModel.set(context);
        onService(req, resp, context, model);
    }
    protected void onService(HttpServletRequest req, HttpServletResponse resp, Context<M> context, M model) throws ServletException, IOException
    {
        String submitField = null;
        String removeAction = null;
        Object newObject = null;
        for (Entry<String,String[]> e : req.getParameterMap().entrySet())
        {
            String key = e.getKey();
            String[] arr = e.getValue();
            if ("pattern".equals(key))
            {
                key = arr[0];
            }
            String field = context.modelName(key);
            if (field == null)
            {
                submitField = key;
            }
            else
            {
                if (field.endsWith("#"))
                {
                    removeAction = field;
                }
                else
                {
                    if (BeanHelper.isAdd(field) || BeanHelper.isAssign(field))
                    {
                        newObject = BeanHelper.applyList(model, field, (Class<Object> c, String h)->{return createObject(model, field, c, h);});
                    }
                    else
                    {
                        try
                        {
                            if (BeanHelper.hasProperty(model, field))
                            {
                                Object value = BeanHelper.getValue(model, field);
                                if (value instanceof SingleSelector)
                                {
                                    SingleSelector ss = (SingleSelector) value;
                                    Class[] pt = BeanHelper.getParameterTypes(model, field);
                                    ss.setValue(ConvertUtility.convert(pt[0], arr[0]));
                                }
                                else
                                {
                                    if (arr.length == 1 && arr[0].isEmpty())
                                    {
                                        BeanHelper.setValue(model, field, null);
                                    }
                                    else
                                    {
                                        BeanHelper.setValue(model, field, arr);
                                    }
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            throw new ServletException("problem with "+field, ex);
                        }
                    }
                }
            }
        }
        if (removeAction != null)
        {
            BeanHelper.applyList(model, removeAction);
        }
        onSubmit(model, submitField);
        if (newObject != null && req.getParameter("sendFragment") != null && (newObject instanceof Renderer))
        {
            response(resp, (Renderer)newObject);
        }
        else
        {
            response(resp, document);
        }
    }
    protected abstract void onSubmit(M data, String field);

    protected abstract M createData();
    
    protected <T> T createObject(M data, String field, Class<T> cls, String hint)
    {
        return BeanHelper.defaultFactory(cls, hint);
    }

}
