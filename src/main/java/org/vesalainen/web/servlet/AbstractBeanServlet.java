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
package org.vesalainen.web.servlet;

import java.io.IOException;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Input;
import org.vesalainen.web.HTML5Datetime;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 * @param <D> Data type
 */
public abstract class AbstractBeanServlet<D> extends AbstractDocumentServlet<D>
{
    public static final HTML5Datetime parser = HTML5Datetime.getInstance();
    public static final String DateFormat = "yyyy-MM-dd'T'HH:mm";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        D data = createData();
        for (Entry<String,String[]> e : req.getParameterMap().entrySet())
        {
            String field = e.getKey();
            String[] arr = e.getValue();
            if (arr == null || arr.length == 0)
            {
                BeanHelper.setFieldValue(data, field, true);
            }
            else
            {
                if (arr.length == 1)
                {
                    BeanHelper.setFieldValue(data, field, arr[0]);
                }
                else
                {
                    BeanHelper.setFieldValue(data, field, arr);
                }
            }
        }
        Document document = getDocument(data);
        response(resp, document);
    }

    protected abstract D createData() throws IOException;
    
    public Input createInput(Object ob, String field) throws IOException
    {
        Object value = BeanHelper.getFieldValue(ob, field);
        InputType inputType = BeanHelper.getAnnotation(ob, field, InputType.class);
        if (inputType != null)
        {
            String it = inputType.value();
            Input input = new Input(it, field);
            switch (it)
            {
                case "text":
                case "password":
                if (value != null)
                {
                    input.addAttr("value", value);
                }
                break;
                case "radio":
                case "checkbox":
                case "color":
                case "date":
                case "datetime":
                case "datetime-local":
                case "email":
                case "month":
                case "number":
                case "range":
                case "search":
                case "tel":
                case "time":
                case "url":
                case "week":
                default:
                    throw new IOException(it+" unknown input type");
                    
            }
            return input;
        }
        Class type = BeanHelper.getType(ob, field);
        if (isInteger(type))
        {
            Input input = new Input("number", field);
            input.addAttr("value", value);
            return input;
        }
        return null;
    }

    private boolean isInteger(Class type)
    {
        return 
                int.class.equals(type) ||
                long.class.equals(type) ||
                short.class.equals(type) ||
                Short.class.equals(type) ||
                Integer.class.equals(type) ||
                Long.class.equals(type);
    }
}
