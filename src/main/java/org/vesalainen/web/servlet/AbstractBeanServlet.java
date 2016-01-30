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

import java.awt.Color;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.AttributedContent;
import org.vesalainen.html.BooleanAttribute;
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
    
    private Map<String,String> typeMap = new HashMap<>();

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
                if (EnumSet.class.equals(type))
                {
                    setEnumSetValue(data, field, arr);
                }
                else
                {
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
    
    public Content createInput(D data, String field) throws IOException
    {
        String inputType = "text";
        Class type = BeanHelper.getType(data, field);
        Object value = BeanHelper.getFieldValue(data, field);
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(data, field, InputType.class);
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
                    if (isBoolean(type) || EnumSet.class.equals(type))
                    {
                        inputType = "checkbox";
                    }
                    else
                    {
                        if (Color.class.equals(type))
                        {
                            inputType = "color";
                        }
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
                if (isBoolean(type))
                {
                    return singleCheckboxContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
                }
                else
                {
                    return multiCheckboxContainer(field, inputType, type, value, labelText, placeholder, inputTypeAnnotation);
                }
            case "select":
                return selectContainer(field, inputType, type, value, labelText, placeholder, inputTypeAnnotation);
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

    protected ContainerContent textContainer(String field, String inputType, Object value, String labelText,String placeholder,  InputType inputTypeAnnotation)
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

    protected ContainerContent textAreaContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
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

    private Tag buttonContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        Tag button = new Tag("input")
                .addAttr("type", inputType)
                .addAttr("value", labelText);
        addAttrs(button, inputTypeAnnotation);
        return button;
    }

    protected Element radioContainer(String field, String inputType, Class<?> type, Object value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        Object[] constants = type.getEnumConstants();
        if (constants == null)
        {
            throw new IllegalArgumentException(field+" not enum");
        }
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
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
                radioInput.addAttr(new BooleanAttribute("checked", true));
            }
            fieldSet.addTag(radioInput);
        }
        return fieldSet;
    }

    protected ContainerContent singleCheckboxContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
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
                input.addAttr(new BooleanAttribute("checked", checked));
            }
        }
        return container;
    }

    private boolean isBoolean(Class type)
    {
        return boolean.class.equals(type) || Boolean.class.equals(type); 
    }

    protected Element multiCheckboxContainer(String field, String inputType, Class type, Object value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        if (!EnumSet.class.equals(type))
        {
            throw new UnsupportedOperationException(type+" not supported for multi selection");
        }
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from "+field);
        }
        Class<? extends Enum> enumType = inputTypeAnnotation.enumType();
        if (Enum.class.equals(enumType))
        {
            throw new IllegalArgumentException("enumType missing from "+field);
        }
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
                .addText(labelText);
        Enum[] constants = enumType.getEnumConstants();
        EnumSet es = (EnumSet) value;
        for (Enum c : constants)
        {
            String n = c.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .addAttr("for", n)
                    .addText(d);
            Input input = new Input(inputType, field)
                    .addAttr("id", n)
                    .addAttr("value", n);
            if (es.contains(c))
            {
                input.addAttr(new BooleanAttribute("checked", true));
            }
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    private void setEnumSetValue(D data, String field, String[] arr)
    {
        Class type = BeanHelper.getType(data, field);
        if (!EnumSet.class.equals(type))
        {
            throw new IllegalArgumentException(type+" is not EnumSet<E> in "+field);
        }
        Object value = BeanHelper.getFieldValue(data, field);
        if (value == null)
        {
            throw new IllegalArgumentException("EnumSet<E> is null in "+field);
        }
        EnumSet es = (EnumSet) value;
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(data, field, InputType.class);
        if  (inputTypeAnnotation == null || Enum.class.equals(inputTypeAnnotation.enumType()))
        {
            throw new IllegalArgumentException("@InputType or @InputType.enumType() missing in "+field);
        }
        Class<? extends Enum> enumType = inputTypeAnnotation.enumType();
        Enum[] constants = enumType.getEnumConstants();
        for (String a : arr)
        {
            for (Enum en : constants)
            {
                if (en.name().equals(a))
                {
                    es.add(en);
                }
            }
        }
    }

    protected Element selectContainer(String field, String inputType, Class type, Object value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        if (type.isEnum())
        {
            return singleSelectContainer(field, inputType, type, (Enum)value, labelText, placeholder, inputTypeAnnotation);
        }
        else
        {
            if (EnumSet.class.equals(type))
            {
                return multiSelectContainer(field, inputType, type, (EnumSet)value, labelText, placeholder, inputTypeAnnotation);
            }
            else
            {
                throw new IllegalArgumentException(field+" not Enum or EnumSet");
            }
        }
    }

    private Element singleSelectContainer(String field, String inputType, Class type, Enum value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        Object[] constants = type.getEnumConstants();
        if (constants == null)
        {
            throw new IllegalArgumentException(field+" not enum");
        }
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addAttr("for", field)
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .addAttr("name", field)
                .addAttr("id", field);
        for (Object c : constants)
        {
            String n = c.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .addAttr("value", n)
                    .addText(d);
            if (c.equals(value))
            {
                option.addAttr(new BooleanAttribute("selected", true));
            }
        }
        return fieldSet;
    }

    private Element multiSelectContainer(String field, String inputType, Class type, EnumSet value, String labelText, String placeholder, InputType inputTypeAnnotation) throws IOException
    {
        if (!EnumSet.class.equals(type))
        {
            throw new UnsupportedOperationException(type+" not supported for multi selection");
        }
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from "+field);
        }
        Class<? extends Enum> enumType = inputTypeAnnotation.enumType();
        if (Enum.class.equals(enumType))
        {
            throw new IllegalArgumentException("enumType missing from "+field);
        }
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .addAttr("name", field)
                .addAttr("id", field)
                .addAttr("multiple", true)
                .addAttr("data-native-menu", false);
        Enum[] constants = enumType.getEnumConstants();
        EnumSet es = (EnumSet) value;
        for (Enum c : constants)
        {
            String n = c.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .addAttr("value", n)
                    .addText(d);
            if (es.contains(c))
            {
                option.addAttr(new BooleanAttribute("selected", true));
            }
        }
        return fieldSet;
    }
}
