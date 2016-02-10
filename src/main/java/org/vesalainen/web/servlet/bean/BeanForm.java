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
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Form;
import org.vesalainen.html.InputTag;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.Tag;
import org.vesalainen.js.ScriptContainer;
import org.vesalainen.util.Lists;
import org.vesalainen.web.Attr;
import org.vesalainen.web.I18n;
import org.vesalainen.web.InputType;
import org.vesalainen.web.MultipleSelector;
import org.vesalainen.web.SingleSelector;

/**
 *
 * @author tkv
 * @param <C>
 */
public class BeanForm<C> extends Form implements I18n
{
    protected BeanDocument<C> document;
    protected boolean hasHideScript;

    public BeanForm(BeanDocument document, Object action)
    {
        this(document, "post", action);
    }

    public BeanForm(BeanDocument document, String method, Object action)
    {
        super(method, action);
        this.document = document;
    }

    public void addInputs(String... fields)
    {
        for (String field : fields)
        {
            addContent(createInput(field));
        }
    }

    public void addInput(String field, Attribute... attributes)
    {
        addContent(createInput(field, attributes));
    }
    
    public void addRestAsHiddenInputs()
    {
        Set<String> set = new HashSet<>();
        set.addAll(document.allFields);
        set.removeAll(document.fieldMap.keySet());
        addHideScript();
        addContent(hiddenContainer(set));
    }

    public Content createInput(String field, Attribute... attributes)
    {
        List<Attribute> attrList = Lists.create(attributes);
        String inputType = "text";
        Class type = BeanHelper.getType(document.context, field);
        Object value = BeanHelper.getFieldValue(document.context, field);
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(document.context, field, InputType.class);
        if (inputTypeAnnotation != null)
        {
            inputType = inputTypeAnnotation.value();
            for (Attr at : inputTypeAnnotation.attrs())
            {
                attrList.add(new SimpleAttribute(at.name(), at.value()));
            }
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
                                    if (MultipleSelector.class.isAssignableFrom(type))
                                    {
                                        inputType = "select";
                                    }
                                    else
                                    {
                                        if (SingleSelector.class.isAssignableFrom(type))
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
        String labelText = document.getLabel(field);
        String placeholder = document.getPlaceholder(field);
        switch (inputType)
        {
            case "text":
            case "password":
            case "email":
            case "search":
            case "tel":
                return textContainer(field, inputType, labelText, placeholder, attrList);
            case "url":
                return urlContainer(field, inputType, labelText, placeholder, attrList);
            case "number":
            case "range":
                return numberContainer(field, inputType, labelText, placeholder, attrList);
            case "textarea":
                return textAreaContainer(field, inputType, labelText, placeholder, attrList);
            case "button":
            case "reset":
                return buttonContainer(field, inputType, labelText, placeholder, attrList);
            case "submit":
                return submitContainer(field, inputType, labelText, placeholder, attrList);
            case "radio":
                return radioContainer(field, inputType, type, labelText, placeholder, attrList);
            case "checkbox":
                if (isBoolean(type))
                {
                    return singleCheckboxContainer(field, inputType, labelText, placeholder, attrList);
                }
                else
                {
                    return multiCheckboxContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation, attrList);
                }
            case "select":
                if (MultipleSelector.class.isAssignableFrom(type))
                {
                    return multipleSelectorContainer(field, inputType, value, labelText, placeholder, attrList);
                }
                else
                {
                    if (SingleSelector.class.isAssignableFrom(type))
                    {
                        return singleSelectorContainer(field, inputType, value, labelText, placeholder, attrList);
                    }
                    else
                    {
                        return selectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation, attrList);
                    }
                }
            case "color":
                return colorContainer(field, inputType, type, labelText, placeholder, attrList);
            case "date":
            case "datetime":
            case "datetime-local":
            case "month":
            case "time":
            case "week":
                return dateContainer(field, inputType, labelText, placeholder, attrList);
            case "datalist":
            case "keygen":
            case "output":
            default:
                throw new IllegalArgumentException(inputType + " unknown input type");
        }
    }

    public ContainerContent textContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        TextInput w = new TextInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    public ContainerContent textAreaContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent textAreaContainer = new ContainerContent();
        Element textAreaLabel = new Element("label").setAttr("for", field).addText(labelText);
        textAreaContainer.addElement(textAreaLabel);
        Element input = new Element(inputType).setAttr("id", field).setAttr("name", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        TextInput w = new TextInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, w);
        input.addText(w);
        textAreaContainer.addElement(input);
        return textAreaContainer;
    }

    public Tag buttonContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        Tag input = new Tag("input").setAttr("type", inputType).setAttr("name", field).setAttr("value", labelText).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        return input;
    }

    public Element radioContainer(String field, String inputType, Class<?> type, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        EnumInput enumInput = new EnumInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, enumInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend").addText(labelText);
        for (Enum e : enumInput.getConstants())
        {
            String n = e.toString();
            String d = document.getLabel(n);
            fieldSet.addElement("label").setAttr("for", n).addText(d);
            InputTag input = new InputTag(inputType, field).setAttr("id", n).setAttr("value", n);
            input.setAttr(attrs);
            input.setAttr(new BooleanAttribute("checked", enumInput.getValue(e)));
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    public ContainerContent singleCheckboxContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        BooleanInput booleanInput = new BooleanInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, booleanInput);
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr(new BooleanAttribute("checked", booleanInput));
        return container;
    }

    private boolean isBoolean(Class type)
    {
        return boolean.class.equals(type) || Boolean.class.equals(type);
    }

    public Element multiCheckboxContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation, Collection<Attribute> attrs)
    {
        if (!EnumSet.class.equals(type))
        {
            throw new UnsupportedOperationException(type + " not supported for multi selection");
        }
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from " + field);
        }
        Class<?> itemType = inputTypeAnnotation.itemType();
        if (Object.class.equals(itemType))
        {
            throw new IllegalArgumentException("enumType missing from " + field);
        }
        EnumSetInput enumSetInput = new EnumSetInput(document.threadLocalData, document.dataType, inputTypeAnnotation.itemType(), field);
        document.fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend").addText(labelText);
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            fieldSet.addElement("label").setAttr("for", n).addText(d);
            InputTag input = new InputTag(inputType, field).setAttr("id", n).setAttr("value", n);
            input.setAttr(attrs);
            input.setAttr(new BooleanAttribute("checked", enumSetInput.getValue(e)));
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    public Element selectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation, Collection<Attribute> attrs)
    {
        if (type.isEnum())
        {
            return singleSelectContainer(field, labelText, attrs);
        }
        else
        {
            if (EnumSet.class.equals(type))
            {
                return multiSelectContainer(field, labelText, inputTypeAnnotation, attrs);
            }
            else
            {
                throw new IllegalArgumentException(field + " not Enum or EnumSet");
            }
        }
    }

    public Element singleSelectContainer(String field, String labelText, Collection<Attribute> attrs)
    {
        EnumInput input = new EnumInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").setAttr("for", field).addText(labelText);
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field);
        for (Enum e : input.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(e)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Element multiSelectContainer(String field, String labelText, InputType inputTypeAnnotation, Collection<Attribute> attrs)
    {
        if (inputTypeAnnotation == null)
        {
            throw new IllegalArgumentException("@InputType missing from " + field);
        }
        Class<?> itemType = inputTypeAnnotation.itemType();
        if (Object.class.equals(itemType))
        {
            throw new IllegalArgumentException("enumType missing from " + field);
        }
        EnumSetInput enumSetInput = new EnumSetInput(document.threadLocalData, document.dataType, inputTypeAnnotation.itemType(), field);
        document.fieldMap.put(field, enumSetInput);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(labelText);
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute<>("multiple", true));
        for (Enum e : enumSetInput.getConstants())
        {
            String n = e.toString();
            String d = getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", enumSetInput.getValue(e)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public ContainerContent colorContainer(String field, String inputType, Class type, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        ColorInput colorInput = new ColorInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, colorInput);
        input.setAttr("value", colorInput);
        return container;
    }

    public ContainerContent dateContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        DateInput dateInput = new DateInput(document.threadLocalData, document.dataType, field, inputType);
        document.fieldMap.put(field, dateInput);
        input.setAttr("value", dateInput);
        return container;
    }

    public ContainerContent numberContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        NumberInput w = new NumberInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    public ContainerContent urlContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        URLInput w = new URLInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, w);
        input.setAttr("value", w);
        return container;
    }

    public Element multipleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        MultipleSelector selector = (MultipleSelector) value;
        List options = selector.getOptions();
        MultipleSelectorInput<C, Object> input = new MultipleSelectorInput<>(document.threadLocalData, document.dataType, field, selector);
        document.fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute("multiple", true));
        for (Object opt : options)
        {
            String n = opt.toString();
            String d = document.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(opt)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Element singleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        SingleSelector selector = (SingleSelector) value;
        List options = selector.getOptions();
        SingleSelectorInput<C, Object> input = new SingleSelectorInput<>(document.threadLocalData, document.dataType, field, selector);
        document.fieldMap.put(field, input);
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(document.getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        for (Object opt : options)
        {
            String n = opt.toString();
            String d = getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", input.getValue(opt)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Tag submitContainer(String field, String inputType, String labelText, String placeholder, Collection<Attribute> attrs)
    {
        Tag input = new Tag("input").setAttr("type", inputType).setAttr("name", field).setAttr("value", labelText);
        input.setAttr(attrs);
        SubmitInput submitInput = new SubmitInput(document.threadLocalData, document.dataType, field);
        document.fieldMap.put(field, submitInput);
        return input;
    }

    protected InputTag hiddenContainer(Set<String> fields)
    {
        InputTag inputTag = new InputTag("text", "JSON");
        inputTag.addClasses("hidden");
        JSONInput input = new JSONInput(document.threadLocalData, fields);
        document.fieldMap.put("JSON", input);
        inputTag.setAttr("value", input);
        return inputTag;
    }

    protected void addHideScript()
    {
        if (!hasHideScript)
        {
            hasHideScript = true;
            ScriptContainer sc = document.getScriptContainer();
            sc.addCode("$('.hidden').hide();");
        }
    }

    @Override
    public String getLabel(Object key)
    {
        return document.getLabel(key);
    }

    @Override
    public String getLabel(Locale locale, Object key)
    {
        return document.getLabel(locale, key);
    }

    @Override
    public String getPlaceholder(Object key)
    {
        return document.getPlaceholder(key);
    }

    @Override
    public String getPlaceholder(Locale locale, Object key)
    {
        return document.getPlaceholder(locale, key);
    }

    public boolean isHasHideScript()
    {
        return hasHideScript;
    }

    public void setHasHideScript(boolean hasHideScript)
    {
        this.hasHideScript = hasHideScript;
    }
    
}
