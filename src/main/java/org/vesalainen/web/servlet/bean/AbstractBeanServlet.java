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
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import org.vesalainen.html.InputTag;
import org.vesalainen.html.RawContent;
import org.vesalainen.html.Tag;
import org.vesalainen.web.Attr;
import org.vesalainen.web.InputType;
import org.vesalainen.web.MultipleSelector;
import org.vesalainen.web.SingleSelector;
import org.vesalainen.web.servlet.AbstractDocumentServlet;

/**
 *
 * @author tkv
 * @param <D> Data type
 */
public abstract class AbstractBeanServlet<D> extends AbstractDocumentServlet<D>
{
    protected final ThreadLocal<D> threadLocalData;
    protected final Map<String,BeanField> fieldMap;
    protected D empty;
    protected Class<D> dataType;
    protected Set<String> allFields;

    public AbstractBeanServlet()
    {
        threadLocalData = new ThreadLocal<>();
        fieldMap = new HashMap<>();
    }

    @Override
    public void init() throws ServletException
    {
        empty = createData();
        dataType = (Class<D>) empty.getClass();
        allFields = BeanHelper.getFields(dataType);
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        D data = createData();
        threadLocalData.set(data);
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

    protected abstract D createData();
    public void addInputs(Element form, String... fields)
    {
        for (String field : fields)
        {
            form.addContent(createInput(field));
        }
    }
    
    public void addRestAsHiddenInputs(Element form)
    {
        for (String field : allFields)
        {
            if (!fieldMap.containsKey(field))
            {
                Content input = createHiddenInput(field);
                form.addContent(input);
            }
        }
    }
    
    public void addHiddenInputs(Element form, String... fields)
    {
        for (String field : fields)
        {
            Content input = createHiddenInput(field);
            form.addContent(input);
        }
    }
    
    public Content createHiddenInput(String field)
    {
        Class type = BeanHelper.getType(empty, field);
        if (Collection.class.isAssignableFrom(type))
        {
            InputType inputTypeAnnotation = BeanHelper.getAnnotation(empty, field, InputType.class);
            if (inputTypeAnnotation == null)
            {
                throw new IllegalArgumentException("@InputType not found");
            }
            return hiddenCollectionContainer(field, inputTypeAnnotation.itemType());
        }
        else
        {
            return hiddenContainer(field);
        }
    }
    public Content createInput(String field)
    {
        String inputType = "text";
        Class type = BeanHelper.getType(empty, field);
        Object value = BeanHelper.getFieldValue(empty, field);
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(empty, field, InputType.class);
        if (inputTypeAnnotation != null)
        {
            inputType = inputTypeAnnotation.value();
        }
        else
        {
            if (NumberInput.isInteger(type))
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
                        else
                        {
                            if (Date.class.equals(type))
                            {
                                inputType = "datetime-local";
                            }
                            else
                            {
                                if (URL.class.equals(type))
                                {
                                    inputType = "url";
                                }
                                else
                                {
                                    if (MultipleSelector.class.equals(type))
                                    {
                                        inputType = "select";
                                    }
                                    else
                                    {
                                        if (SingleSelector.class.equals(type))
                                        {
                                            inputType = "select";
                                        }
                                    }
                                }
                            }
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
            case "email":
            case "search":
            case "tel":
                return textContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "url":
                return urlContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "number":
            case "range":
                return numberContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "textarea":
                return textAreaContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "button":
            case "reset":
                return buttonContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
            case "submit":
                return submitContainer(field, inputType, labelText, placeholder, inputTypeAnnotation);
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
                if (MultipleSelector.class.equals(type))
                {
                    return multipleSelectorContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
                }
                else
                {
                    if (SingleSelector.class.equals(type))
                    {
                        return singleSelectorContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation);
                    }
                    else
                    {
                        return selectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
                    }
                }
            case "color":
                return colorContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
            case "date":
            case "datetime":
            case "datetime-local":
            case "month":
            case "time":
            case "week":
                return dateContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation);
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
                tag.setAttr(at.name(), at.value());
            }
        }
    }
    protected ContainerContent textContainer(String field, String inputType, String labelText,String placeholder,  InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        TextInput w = new TextInput(threadLocalData, dataType, field);
        fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    protected ContainerContent textAreaContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent textAreaContainer = new ContainerContent();
        Element textAreaLabel = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        textAreaContainer.addElement(textAreaLabel);
        Element textAreaInput = new Element(inputType)
                .setAttr("id", field)
                .setAttr("name", field)
                .setAttr("placeholder", placeholder);
        addAttrs(textAreaInput, inputTypeAnnotation);
        TextInput w = new TextInput(threadLocalData, dataType, field);
        fieldMap.put(field, w);
        textAreaInput.addText(w);
        textAreaContainer.addElement(textAreaInput);
        return textAreaContainer;
    }

    private Tag buttonContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        Tag button = new Tag("input")
                .setAttr("type", inputType)
                .setAttr("value", labelText);
        addAttrs(button, inputTypeAnnotation);
        return button;
    }

    protected Element radioContainer(String field, String inputType, Class<?> type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        EnumInput enumInput = new EnumInput(threadLocalData, dataType, field);
        fieldMap.put(field, enumInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
                .addText(labelText);
        for (Enum e : enumInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .setAttr("for", n)
                    .addText(d);
            InputTag radioInput = new InputTag(inputType, field)
                    .setAttr("id", n)
                    .setAttr("value", n);
            radioInput.setAttr(new BooleanAttribute("checked", enumInput.getValue(e)));
            fieldSet.addTag(radioInput);
        }
        return fieldSet;
    }

    protected ContainerContent singleCheckboxContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        BooleanInput booleanInput = new BooleanInput(threadLocalData, dataType, field);
        fieldMap.put(field, booleanInput);
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        input.setAttr(new BooleanAttribute("checked", booleanInput));
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
        Class<?> itemType = inputTypeAnnotation.itemType();
        if (Object.class.equals(itemType))
        {
            throw new IllegalArgumentException("enumType missing from "+field);
        }
        EnumSetInput enumSetInput = new EnumSetInput(threadLocalData, dataType, inputTypeAnnotation.itemType(), field);
        fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend")
                .addText(labelText);
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            fieldSet.addElement("label")
                    .setAttr("for", n)
                    .addText(d);
            InputTag input = new InputTag(inputType, field)
                    .setAttr("id", n)
                    .setAttr("value", n);
            input.setAttr(new BooleanAttribute("checked", enumSetInput.getValue(e)));
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
        EnumInput input = new EnumInput(threadLocalData, dataType, field);
        fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .setAttr("for", field)
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .setAttr("name", field)
                .setAttr("id", field);
        for (Enum e : input.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .setAttr("value", n)
                    .addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(e)));
        }
        return fieldSet;
    }

    private Element multiSelectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from "+field);
        }
        Class<?> itemType = inputTypeAnnotation.itemType();
        if (Object.class.equals(itemType))
        {
            throw new IllegalArgumentException("enumType missing from "+field);
        }
        EnumSetInput enumSetInput = new EnumSetInput(threadLocalData, dataType, inputTypeAnnotation.itemType(), field);
        fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addText(labelText);
        Element select = fieldSet.addElement("select")
                .setAttr("name", field)
                .setAttr("id", field)
                .setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute<>("multiple", true));
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .setAttr("value", n)
                    .addText(d);
            option.setAttr(new BooleanAttribute("selected", enumSetInput.getValue(e)));
        }
        return fieldSet;
    }

    protected Content colorContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        ColorInput colorInput = new ColorInput(threadLocalData, dataType, field);
        fieldMap.put(field, colorInput);
        input.setAttr("value", colorInput);
        return container;
    }

    private Content dateContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        DateInput dateInput = new DateInput(threadLocalData, dataType, field, inputType);
        fieldMap.put(field, dateInput);
        input.setAttr("value", dateInput);
        return container;
    }

    protected Content numberContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        NumberInput w = new NumberInput(threadLocalData, dataType, field);
        fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    private Content urlContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label")
                .setAttr("for", field)
                .addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field)
                .setAttr("id", field)
                .setAttr("placeholder", placeholder);
        addAttrs(input, inputTypeAnnotation);
        container.addTag(input);
        URLInput w = new URLInput(threadLocalData, dataType, field);
        fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    protected Element multipleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        MultipleSelector selector = (MultipleSelector) value;
        List options = selector.getOptions();
        MultipleSelectorInput<D,Object> input = new MultipleSelectorInput<>(threadLocalData, dataType, field, options);
        fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addText(getLabel(field));
        Element select = fieldSet.addElement("select")
                .setAttr("name", field)
                .setAttr("id", field)
                .setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute("multiple", true));
        for (Object opt : options)
        {
            String n = opt.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .setAttr("value", n)
                    .addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(opt)));
        }
        return fieldSet;
    }

    protected Element singleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        SingleSelector selector = (SingleSelector) value;
        List options = selector.getOptions();
        SingleSelectorInput<D,Object> input = new SingleSelectorInput<>(threadLocalData, dataType, field, options);
        fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label")
                .addText(getLabel(field));
        Element select = fieldSet.addElement("select")
                .setAttr("name", field)
                .setAttr("id", field)
                .setAttr("data-native-menu", false);
        for (Object opt : options)
        {
            String n = opt.toString();
            String d = getLabel(n);
            Element option = select.addElement("option")
                    .setAttr("value", n)
                    .addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(opt)));
        }
        return fieldSet;
    }

    private Tag submitContainer(String field, String inputType, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        Tag button = new Tag("input")
                .setAttr("type", inputType)
                .setAttr("name", field)
                .setAttr("value", labelText);
        addAttrs(button, inputTypeAnnotation);
        TextInput input = new TextInput(threadLocalData, dataType, field);
        fieldMap.put(field, input);
        return button;
    }

    private InputTag hiddenContainer(String field)
    {
        InputTag input = new InputTag("text", field)
                .setAttr("id", field)
                .setAttr("style", "visibility: hidden");
        TextInput w = new TextInput(threadLocalData, dataType, field);
        fieldMap.put(field, w);
        input.setAttr("value", w);
        return input;
    }

    private Element hiddenCollectionContainer(String field, Class itemType)
    {
        Element div = new Element("div")
                .setAttr("style", "visibility: hidden");
        CollectionInput input = new CollectionInput(threadLocalData, dataType, itemType, field);
        fieldMap.put(field, input);
        RawContent<CollectionInput> raw = new RawContent<>(input);
        div.addContent(raw);
        return div;
    }
}
