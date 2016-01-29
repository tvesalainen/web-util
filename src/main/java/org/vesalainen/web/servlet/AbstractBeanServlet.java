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
import java.util.Locale;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.AttributedContent;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Input;
import org.vesalainen.html.Tag;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.web.Attr;
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
            Class type = BeanHelper.getType(data, field);
            if (isBoolean(type))
            {
                BeanHelper.setFieldValue(data, field, true);
            }
            else
            {
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
        }
        Document document = getDocument(data);
        response(resp, document);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Document document = getDocument(createData());
        response(resp, document);
    }

    protected abstract D createData() throws IOException;
    
    public Content createInput(Object ob, String field) throws IOException
    {
        String inputType = "text";
        Class type = BeanHelper.getType(ob, field);
        Object value = BeanHelper.getFieldValue(ob, field);
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(ob, field, InputType.class);
        if (inputTypeAnnotation != null)
        {
            inputType = inputTypeAnnotation.value();
        }
        else
        {
            if (isInteger(type))
            {
                inputType = "number";
            }
            else
            {
                if (type.isEnum())
                {
                    inputType = "radio";
                }
                else
                {
                    if (isBoolean(type))
                    {
                        inputType = "checkbox";
                    }
                }
            }
        }
        String labelText = getLabel(field);
        String placeholder = getPlaceholder(field);
        switch (inputType)
        {
            case "text":
            case "password":
            case "number":
            case "email":
            case "search":
                return textContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
            case "textarea":
                return textAreaContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
            case "button":
            case "reset":
            case "submit":
                return buttonContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
            case "radio":
                return radioContainer(field, inputType, type, value, labelText, placeholder, inputTypeAnnotation);
            case "checkbox":
                return checkboxContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
            case "color":
            case "date":
            case "datetime":
            case "datetime-local":
            case "month":
            case "range":
            case "tel":
            case "time":
            case "url":
            case "week":
            case "datalist":
            case "keygen":
            case "output":
            default:
                throw new IOException(inputType+" unknown input type");

        }
    }

    private void addAttrs(AttributedContent tag, InputType inputType)
    {
        if (inputType != null)
        {
            for (Attr at : inputType.attrs())
            {
                tag.addAttr(at.name(), at.value());
            }
        }
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

    private boolean isEnum(Class type)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Content textContainer(String field, String inputType, Object value, String labelText,String placeholder,  InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .addAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        Input input = new Input(inputType, field)
                .addAttr("id", field)
                .addAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        if (value != null)
        {
            input.addAttr("value", value);
        }
        return container;
    }

    private Content textAreaContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent textAreaContainer = new ContainerContent();
        Element textAreaLabel = new Element("label")
                .addAttr("for", field)
                .addText(labelText);
        textAreaContainer.addElement(textAreaLabel);
        Element textAreaInput = new Element(inputType)
                .addAttr("id", field)
                .addAttr("name", field)
                .addAttr("placeholder", placeholder);
        addAttrs(textAreaInput, inputTypeAnnotation);
        if (value != null)
        {
            textAreaInput.addText(value.toString());
        }
        textAreaContainer.addElement(textAreaInput);
        return textAreaContainer;
    }

    private Content buttonContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        Tag button = new Tag("input")
                .addAttr("type", inputType)
                .addAttr("value", labelText);
        addAttrs(button, inputTypeAnnotation);
        return button;
    }

    private Content radioContainer(String field, String inputType, Class<?> type, Object value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        Object[] constants = type.getEnumConstants();
        if (constants == null)
        {
            throw new IllegalArgumentException(field+" not enum");
        }
        Element fieldSet = new Element("fieldset")
                .addElement("legend")
                .addText(labelText);
        for (Object c : constants)
        {
            String n = c.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .addAttr("for", n)
                    .addText(d);
            Input radioInput = new Input(inputType, field)
                    .addAttr("id", n)
                    .addAttr("value", n);
            if (c.equals(value))
            {
                radioInput.addAttr("checked", true);
            }
            fieldSet.addTag(radioInput);
        }
        return fieldSet;
    }

    private Content checkboxContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .addAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        Input input = new Input(inputType, field)
                .addAttr("id", field)
                .addAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        if (value != null)
        {
            boolean checked = ConvertUtility.convert(boolean.class, value);
            if (checked)
            {
                input.addAttr("checked", checked);
            }
        }
        return container;
    }

    private boolean isBoolean(Class type)
    {
        return boolean.class.equals(type) || Boolean.class.equals(type); 
    }
}
