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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.BooleanAttribute;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Form;
import org.vesalainen.html.InputTag;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.html.Tag;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.util.Lists;
import org.vesalainen.web.Attr;
import org.vesalainen.web.I18n;
import org.vesalainen.web.InputType;
import org.vesalainen.web.MultipleSelector;
import org.vesalainen.web.SingleSelector;

/**
 *
 * @author tkv
 * @param <M>
 */
public class BeanForm<M> extends Form
{
    protected ThreadLocal<M> threadLocalData;

    public BeanForm(Content parent, ThreadLocal<M> threadLocalData, Object action)
    {
        this(parent, threadLocalData, "post", action);
    }

    public BeanForm(Content parent, ThreadLocal<M> threadLocalData, String method, Object action)
    {
        super(parent, method, action);
        this.threadLocalData = threadLocalData;
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
    
    public Content createInput(String field, Attribute... attributes)
    {
        return createInput(field, Lists.create(attributes));
    }
    public Content createInput(String field, List<Attribute> attrList)
    {
        M model = threadLocalData.get();
        return createInput(model, BeanHelper.getType(model, field), BeanHelper.getValue(model, field), BeanHelper.getParameterTypes(model, field), field, attrList);
    }
    public Content createInput(M model, Class type, Object value, Class[] parameterTypes, String field, List<Attribute> attrList)
    {
        String inputType = "text";
        InputType inputTypeAnnotation = BeanHelper.getAnnotation(model, field, InputType.class);
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
            inputType = getInputType(type);
        }
        String suffix = BeanHelper.suffix(field);
        Renderer labelText = I18n.getLabel(suffix);
        Renderer placeholder = I18n.getPlaceholder(BeanHelper.suffix(suffix));
        switch (inputType)
        {
            case "text":
            case "password":
            case "email":
            case "search":
            case "tel":
                return textContainer(field, ConvertUtility.convert(String.class, value), inputType, labelText, placeholder, attrList);
            case "url":
                return urlContainer(field, ConvertUtility.convert(String.class, value), inputType, labelText, placeholder, attrList);
            case "number":
            case "range":
                return numberContainer(field, ConvertUtility.convert(String.class, value), inputType, labelText, placeholder, attrList);
            case "textarea":
                return textAreaContainer(field, ConvertUtility.convert(String.class, value), inputType, labelText, placeholder, attrList);
            case "button":
            case "reset":
                return buttonContainer(field, inputType, labelText, placeholder, attrList);
            case "submit":
                return submitContainer(field, value, inputType, labelText, placeholder, attrList);
            case "radio":
                return radioContainer(field, ConvertUtility.convert(Enum.class, value), inputType, type, labelText, placeholder, attrList);
            case "checkbox":
                if (isBoolean(type))
                {
                    return singleCheckboxContainer(field, ConvertUtility.convert(Boolean.class, value), inputType, labelText, placeholder, attrList);
                }
                else
                {
                    return multiCheckboxContainer(field, ConvertUtility.convert(EnumSet.class, value), inputType, type, labelText, placeholder, parameterTypes[0], attrList);
                }
            case "select":
                if (MultipleSelector.class.isAssignableFrom(type))
                {
                    return multipleSelectorContainer(field, ConvertUtility.convert(MultipleSelector.class, value), inputType, value, labelText, placeholder, attrList);
                }
                else
                {
                    if (SingleSelector.class.isAssignableFrom(type))
                    {
                        return singleSelectorContainer(field, ConvertUtility.convert(SingleSelector.class, value), inputType, value, labelText, placeholder, attrList);
                    }
                    else
                    {
                        if (type.isEnum())
                        {
                            return singleSelectContainer(field, (Enum) value, type, labelText, attrList);
                        }
                        else
                        {
                            if (EnumSet.class.equals(type))
                            {
                                return multiSelectContainer(field, (EnumSet) value, labelText, parameterTypes[0], attrList);
                            }
                            else
                            {
                                throw new IllegalArgumentException(field + " not Enum or EnumSet");
                            }
                        }
                    }
                }
            case "color":
                return colorContainer(field, ConvertUtility.convert(String.class, value), inputType, type, labelText, placeholder, attrList);
            case "date":
            case "datetime":
            case "datetime-local":
            case "month":
            case "time":
            case "week":
                return dateContainer(field, ConvertUtility.convert(String.class, value), inputType, labelText, placeholder, attrList);
            case "datalist":
            case "keygen":
            case "output":
            default:
                throw new IllegalArgumentException(inputType + " unknown input type");
        }
    }

    public InputTag bareTextInput(String field, String value, String inputType, Collection<Attribute> attrs)
    {
        InputTag input = new InputTag(inputType, field).setAttr("id", field);
        input.setAttr(attrs);
        input.setAttr("value", value);
        return input;
    }

    public ContainerContent textContainer(String field, String value, String inputType, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(label);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr("value", value);
        return container;
    }

    public ContainerContent textAreaContainer(String field, String value, String inputType, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent textAreaContainer = new ContainerContent();
        Element textAreaLabel = new Element("label").setAttr("for", field).addText(label);
        textAreaContainer.addElement(textAreaLabel);
        Element input = new Element(inputType).setAttr("id", field).setAttr("name", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        input.addText(value);
        textAreaContainer.addElement(input);
        return textAreaContainer;
    }

    public Tag buttonContainer(String field, String inputType, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        Tag input = new Tag("input").setAttr("type", inputType).setAttr("name", field).setAttr("value", label).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        return input;
    }

    public Element radioContainer(String field, Enum value, String inputType, Class<Enum> type, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend").addText(label);
        for (Enum e : type.getEnumConstants())
        {
            String n = e.toString();
            String id = field+'-'+n;
            Renderer d = I18n.getLabel(n);
            fieldSet.addElement("label").setAttr("for", id).addText(d);
            InputTag input = new InputTag(inputType, field).setAttr("id", id).setAttr("value", n);
            input.setAttr(attrs);
            input.setAttr(new BooleanAttribute("checked", e.equals(value)));
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    public ContainerContent singleCheckboxContainer(String field, Boolean value, String inputType, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(label);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr(new BooleanAttribute("checked", value));
        return container;
    }

    private boolean isBoolean(Class type)
    {
        return boolean.class.equals(type) || Boolean.class.equals(type);
    }

    public Element multiCheckboxContainer(String field, EnumSet enumSet, String inputType, Class type, Renderer label, Renderer placeholder, Class<Enum> innerType, Collection<Attribute> attrs)
    {
        if (!EnumSet.class.equals(type))
        {
            throw new UnsupportedOperationException(type + " not supported for multi selection");
        }
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("legend").addText(label);
        for (Enum e : innerType.getEnumConstants())
        {
            String n = e.toString();
            String id = field+'-'+n;
            Renderer d = I18n.getLabel(n);
            fieldSet.addElement("label").setAttr("for", id).addText(d);
            InputTag input = new InputTag(inputType, field).setAttr("id", id).setAttr("value", n);
            input.setAttr(attrs);
            input.setAttr(new BooleanAttribute("checked", enumSet.contains(e)));
            fieldSet.addTag(input);
        }
        return fieldSet;
    }

    public Element selectContainer(String field, Object value, String inputType, Class type, Renderer label, Renderer placeholder, Class[] innerType, Collection<Attribute> attrs)
    {
        if (type.isEnum())
        {
            return singleSelectContainer(field, (Enum) value, type, label, attrs);
        }
        else
        {
            if (EnumSet.class.equals(type))
            {
                return multiSelectContainer(field, (EnumSet) value, label, innerType[0], attrs);
            }
            else
            {
                throw new IllegalArgumentException(field + " not Enum or EnumSet");
            }
        }
    }

    public Element singleSelectContainer(String field, Enum value, Class<Enum> type, Renderer labelText, Collection<Attribute> attrs)
    {
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").setAttr("for", field).addText(labelText);
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field);
        for (Enum e : type.getEnumConstants())
        {
            String n = e.toString();
            Renderer d = I18n.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", e.equals(value)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Element multiSelectContainer(String field, EnumSet value, Renderer labelText, Class<Enum> innerType, Collection<Attribute> attrs)
    {
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(labelText);
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute<>("multiple", true));
        for (Enum e : innerType.getEnumConstants())
        {
            String n = e.toString();
            Renderer d = I18n.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", e.equals(value)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public ContainerContent colorContainer(String field, String value, String inputType, Class type, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr("value", value);
        return container;
    }

    public ContainerContent dateContainer(String field, String value, String inputType, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element label = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(label);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr("value", value);
        return container;
    }

    public ContainerContent numberContainer(String field, String value, String inputType, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr("value", value);
        return container;
    }

    public ContainerContent urlContainer(String field, String value, String inputType, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        ContainerContent container = new ContainerContent();
        Element textLabel = new Element("label").setAttr("for", field).addText(labelText);
        container.addElement(textLabel);
        InputTag input = new InputTag(inputType, field).setAttr("id", field).setAttr("placeholder", placeholder);
        input.setAttr(attrs);
        container.addTag(input);
        input.setAttr("value", value);
        return container;
    }

    public Element multipleSelectorContainer(String field, MultipleSelector selector, String inputType, Object value, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        List options = selector.getOptions();
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(I18n.getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        select.setAttr(new BooleanAttribute("multiple", true));
        for (Object opt : options)
        {
            String n = opt.toString();
            Renderer d = I18n.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", selector.contains(opt)));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Element singleSelectorContainer(String field, SingleSelector selector, String inputType, Object value, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        List options = selector.getOptions();
        Element fieldSet = new Element("fieldset");
        fieldSet.addElement("label").addText(I18n.getLabel(field));
        Element select = fieldSet.addElement("select").setAttr("name", field).setAttr("id", field).setAttr("data-native-menu", false);
        for (Object opt : options)
        {
            String n = opt.toString();
            Renderer d = I18n.getLabel(n);
            Element option = select.addElement("option").setAttr("value", n).addText(d);
            option.setAttr(new BooleanAttribute("selected", opt.equals(selector.getValue())));
            option.setAttr(attrs);
        }
        return fieldSet;
    }

    public Tag submitContainer(String field, Object value, String inputType, Renderer label, Renderer placeholder, Collection<Attribute> attrs)
    {
        Tag input = new Tag("input").setAttr("type", inputType).setAttr("name", field).setAttr("value", label);
        input.setAttr(attrs);
        return input;
    }

    public static final String getInputType(Class type)
    {
        if (CharSequence.class.isAssignableFrom(type))
        {
            return "text";
        }
        if (NumberInput.isInteger(type))
        {
            return "number";
        }
        if (type.isEnum())
        {
            return "radio";
        }
        if (boolean.class.equals(type) || Boolean.class.equals(type) || EnumSet.class.equals(type))
        {
            return "checkbox";
        }
        if (Color.class.equals(type))
        {
            return "color";
        }
        if (LocalDate.class.equals(type))
        {
            return "date";
        }
        if (LocalTime.class.equals(type))
        {
            return "time";
        }
        if (LocalDateTime.class.equals(type))
        {
            return "datetime-local";
        }
        if (URL.class.equals(type))
        {
            return "url";
        }
        if (MultipleSelector.class.isAssignableFrom(type))
        {
            return "select";
        }
        if (SingleSelector.class.isAssignableFrom(type))
        {
            return "select";
        }
        throw new UnsupportedOperationException(type+" not supported");
    }

}
