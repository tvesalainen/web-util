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

import java.awt.Color;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.bean.BeanField;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.AttributedContent;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Input;
import org.vesalainen.html.Tag;
import org.vesalainen.web.Attr;
import org.vesalainen.web.HTML5Datetime;
import org.vesalainen.web.InputType;
import org.vesalainen.web.servlet.AbstractDocumentServlet;

/**
 *
 * @author tkv
 * @param <D> Data type
 */
public abstract class AbstractBeanServlet<D> extends AbstractDocumentServlet<D>
{
    public static final HTML5Datetime parser = HTML5Datetime.getInstance();
    public static final String DateFormat = "yyyy-MM-dd'T'HH:mm";
    private final ThreadLocal<D> threadLocal;
    private final Map<String,BeanField> fieldMap;
    private final D empty = createData();
    private final Class<D> dataType = (Class<D>) empty.getClass();

    public AbstractBeanServlet()
    {
        threadLocal = new ThreadLocal<>();
        fieldMap = new HashMap<>();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        D data = createData();
        threadLocal.set(data);
        for (Entry<String,String[]> e : req.getParameterMap().entrySet())
        {
            String field = e.getKey();
            BeanField bf = fieldMap.get(field);
            if (bf == null)
            {
                throw new IllegalArgumentException(field+" not found");
            }
            bf.set(e.getValue());
        }
        response(resp, document);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        threadLocal.set(empty);
        response(resp, document);
    }

    protected abstract D createData();
    
    public Content createInput(D data, String field)
    {
        String inputType = "text";
        Class type = BeanHelper.getType(data, field);
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
                return textContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "textarea":
                return textAreaContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "button":
            case "reset":
            case "submit":
                return buttonContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "radio":
                return radioContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
            case "checkbox":
                if (isBoolean(type))
                {
                    return singleCheckboxContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
                }
                else
                {
                    return multiCheckboxContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
                }
            case "select":
                return selectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
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
                throw new IllegalArgumentException(inputType+" unknown input type");

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

    protected ContainerContent textContainer(String field, String inputType, String labelText,String placeholder,  InputType inputTypeAnnotation)
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
        TextInput w = new TextInput(threadLocal, dataType, field);
        fieldMap.put(field, w);
        input.addAttr("value", w);
        return container;
    }

    protected ContainerContent textAreaContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
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
        TextInput w = new TextInput(threadLocal, dataType, field);
        fieldMap.put(field, w);
        textAreaInput.addText(w);
        textAreaContainer.addElement(textAreaInput);
        return textAreaContainer;
    }

    private Tag buttonContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        Tag button = new Tag("input")
                .addAttr("type", inputType)
                .addAttr("value", labelText);
        addAttrs(button, inputTypeAnnotation);
        return button;
    }

    protected Element radioContainer(String field, String inputType, Class<?> type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        EnumInput enumInput = new EnumInput(threadLocal, dataType, field);
        fieldMap.put(field, enumInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
                .addText(labelText);
        for (Enum e : enumInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .addAttr("for", n)
                    .addText(d);
            Input radioInput = new Input(inputType, field)
                    .addAttr("id", n)
                    .addAttr("value", n);
            radioInput.addAttr(new BooleanAttribute("checked", enumInput.getValue(e)));
            fieldSet.addTag(radioInput);
        }
        return fieldSet;
    }

    protected ContainerContent singleCheckboxContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        BooleanInput booleanInput = new BooleanInput(threadLocal, dataType, field);
        fieldMap.put(field, booleanInput);
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
        input.addAttr(new BooleanAttribute("checked", booleanInput));
        return container;
    }

    private boolean isBoolean(Class type)
    {
        return boolean.class.equals(type) || Boolean.class.equals(type); 
    }

    protected Element multiCheckboxContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
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
        EnumSetInput enumSetInput = new EnumSetInput(threadLocal, dataType, inputTypeAnnotation.enumType(), field);
        fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
                .addText(labelText);
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .addAttr("for", n)
                    .addText(d);
            Input input = new Input(inputType, field)
                    .addAttr("id", n)
                    .addAttr("value", n);
            input.addAttr(new BooleanAttribute("checked", enumSetInput.getValue(e)));
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    protected Element selectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        if (type.isEnum())
        {
            return singleSelectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
        }
        else
        {
            if (EnumSet.class.equals(type))
            {
                return multiSelectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
            }
            else
            {
                throw new IllegalArgumentException(field+" not Enum or EnumSet");
            }
        }
    }

    private Element singleSelectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        EnumInput input = new EnumInput(threadLocal, dataType, field);
        fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addAttr("for", field)
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .addAttr("name", field)
                .addAttr("id", field);
        for (Enum e : input.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .addAttr("value", n)
                    .addText(d);
            option.addAttr(new BooleanAttribute("selected", input.getValue(e)));
        }
        return fieldSet;
    }

    private Element multiSelectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from "+field);
        }
        Class<? extends Enum> enumType = inputTypeAnnotation.enumType();
        if (Enum.class.equals(enumType))
        {
            throw new IllegalArgumentException("enumType missing from "+field);
        }
        EnumSetInput enumSetInput = new EnumSetInput(threadLocal, dataType, inputTypeAnnotation.enumType(), field);
        fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .addAttr("name", field)
                .addAttr("id", field)
                .addAttr("multiple", true)
                .addAttr("data-native-menu", false);
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .addAttr("value", n)
                    .addText(d);
            option.addAttr(new BooleanAttribute("selected", enumSetInput.getValue(e)));
        }
        return fieldSet;
    }
}