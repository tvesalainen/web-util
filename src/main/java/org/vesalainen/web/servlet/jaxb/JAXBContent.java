/*
 * Copyright (C) 2016 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.web.servlet.jaxb;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.AttributedContent;
import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.InputTag;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.web.I18n;
import org.vesalainen.web.servlet.bean.BeanDocument;
import org.vesalainen.web.servlet.bean.BeanForm;
import org.vesalainen.web.servlet.bean.Context;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <M>
 */
public class JAXBContent<M> extends ThreadLocalContent<M>
{
    private String action;
    private BeanDocument document;
    
    public JAXBContent(ThreadLocal<Context<M>> model, BeanDocument document, String action)
    {
        super(model);
        this.document = document;
        this.action = action;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        Context<M> ctx = threadLocalModel.get();
        M model = ctx.getModel();
        BeanForm form = document.createForm(null, "form", "post", action);
        form.addElement("input").setAttr("type", "submit").setAttr("name", "submit").setAttr("value", I18n.getLabel("submit"));
        LevelHandler levelHandler = new LevelHandler(form);
        BeanHelper.stream(model)
                //.filter((String s)->{return filter(model, s, XmlType.class, XmlValue.class, XmlAttribute.class, XmlElement.class, XmlElements.class);})
                //.sorted(new ComparatorImpl<>(data))
                .forEach((String pattern)->
        {
            List<Attribute> attributes = new ArrayList<>();
            levelHandler.pop(pattern);
            String inputName = ctx.inputName(pattern);
            AnnotatedElement annotatedElement = BeanHelper.getAnnotatedElement(model, pattern);
            Xml2Html.inject(annotatedElement.getAnnotations(), attributes);
            Class type = BeanHelper.getType(model, pattern);
            Object value = BeanHelper.getValue(model, pattern);
            Class[] parameterTypes = BeanHelper.getParameterTypes(model, pattern);
            String suffix = BeanHelper.suffix(pattern);
            XmlType xmlType = annotatedElement.getAnnotation(XmlType.class);
            if (xmlType != null)
            {
                Element collapsible = new Element("div").setDataAttr("role", "collapsible").setAttr("id", inputName);
                String name = xmlType.name();
                if (name != null)
                {
                    collapsible.addElement("h1").addText(I18n.getLabel(name)+" "+describe(value));
                }
                Element navbar = collapsible.addElement("div").setDataAttr("role", "navbar");
                Element ul = navbar.addElement("ul");
                Element li = ul.addElement("li");
                li.addElement("input").setAttr("type", "submit").setDataAttr("icon", "delete").setAttr("name", ctx.inputName(pattern+'#'));
                //li.addElement("a").setAttr("href", "#").setDataAttr("pattern", inputName).setDataAttr("icon", "delete").addClasses("delete");
                levelHandler.push(pattern, collapsible);
            }
            else
            {
                if (List.class.isAssignableFrom(type))
                {
                    Element navbar = new Element("div").setDataAttr("role", "navbar").setDataAttr("iconpos", "right");
                    Element ul = navbar.addElement("ul");
                    Element li = ul.addElement("li");
                    XmlElements xmlElements = annotatedElement.getAnnotation(XmlElements.class);
                    if (xmlElements != null)
                    {
                        Element collapsible = new Element("div").setDataAttr("role", "collapsible");
                        collapsible.addElement("h1").addText(I18n.getLabel("add"));
                        for (XmlElement xmlElement : xmlElements.value())
                        {
                            Class t = xmlElement.type();
                            String name = xmlElement.name();
                            if (t != null && name != null)
                            {
                                li.addElement("input").setAttr("type", "submit").setDataAttr("icon", "plus").setAttr("name", ctx.inputName(pattern+'+'+name)).setAttr("value", I18n.getLabel("add-"+name));
                                //li.addElement("a").setAttr("href", "#").setDataAttr("pattern", ctx.inputName(pattern+'+'+name)).setDataAttr("icon", "plus").addText(I18n.getLabel("add-"+name)).addClasses("add");
                            }
                        }
                        collapsible.addContent(navbar);
                        levelHandler.add(collapsible);
                    }
                    else
                    {
                        li.addElement("input").setAttr("type", "submit").setDataAttr("icon", "plus").setAttr("name", ctx.inputName(pattern+'+')).setAttr("value", I18n.getLabel("add-"+suffix));
                        //li.addElement("a").setAttr("href", "#").setDataAttr("pattern", ctx.inputName(pattern+'+')).setDataAttr("icon", "plus").addText(I18n.getLabel("add-"+suffix)).addClasses("add");
                        levelHandler.add(navbar);
                    }
                    Element ul2 = new Element("ul").setDataAttr("role", "listview");
                    levelHandler.push(pattern, ul2);
                }
                else
                {
                    XmlValue xmlValue = annotatedElement.getAnnotation(XmlValue.class);
                    if (xmlValue != null)
                    {
                        String string = ConvertUtility.convert(String.class, value);
                        Xml2Html.injectArea(string, attributes);
                        ContainerContent textAreaContainer = form.textAreaContainer(pattern, string, "textarea", I18n.getLabel(suffix), I18n.getPlaceholder(suffix), attributes);
                        levelHandler.add(textAreaContainer);
                    }
                    else
                    {
                        XmlAttribute xmlAttribute = annotatedElement.getAnnotation(XmlAttribute.class);
                        if (xmlAttribute != null)
                        {
                            Content input = form.createInput(model, type, value, parameterTypes, pattern, attributes);
                            levelHandler.add(input);
                            if (xmlAttribute.required())
                            {
                                String string = ConvertUtility.convert(String.class, value);
                                if (string == null || string.isEmpty())
                                {
                                    levelHandler.openCollapsibles();
                                }
                            }
                        }
                        else
                        {
                            if (value != null && levelHandler.isList())
                            {
                                String string = ConvertUtility.convert(String.class, value);
                                InputTag input = form.bareTextInput(pattern, string, "text", attributes);
                                levelHandler.add(input);
                            }
                        }
                    }
                }
            }
        });
        form.append(out);
    }
    
    public String describe(Object ob)
    {
        if (ob == null)
        {
            return "";
        }
        try
        {
            Method toString = ob.getClass().getDeclaredMethod("toString");
            return (String) toString.invoke(ob);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            try
            {
                Method getName = ob.getClass().getMethod("getName");
                return (String) getName.invoke(ob);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex1)
            {
                try
                {
                    Method name = ob.getClass().getMethod("name");
                    return (String) name.invoke(ob);
                }
                catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2)
                {
                    return "";
                }
            }
        }
    }
    private boolean filter(M data, String pattern, Class<? extends Annotation>... allowed)
    {
        Class type = BeanHelper.getType(data, pattern);
        if (List.class.isAssignableFrom(type))
        {
            return true;
        }
        AnnotatedElement ae = BeanHelper.getAnnotatedElement(data, pattern);
        for (Class<? extends Annotation> ac : allowed)
        {
            if (ae.isAnnotationPresent(ac))
            {
                return true;
            }
        }
        return false;
    }
    private static class ComparatorImpl<C> implements Comparator<String>
    {
        private C data;

        public ComparatorImpl(C data)
        {
            this.data = data;
        }
        
        @Override
        public int compare(String o1, String o2)
        {
            String p1 = prefix(o1);
            String p2 = prefix(o2);
            if (p1.equals(p2))
            {
                AnnotatedElement a1 = BeanHelper.getAnnotatedElement(data, o1);
                AnnotatedElement a2 = BeanHelper.getAnnotatedElement(data, o2);
                boolean b1 = a1.isAnnotationPresent(XmlAttribute.class);
                boolean b2 = a2.isAnnotationPresent(XmlAttribute.class);
                if (b1 != b2)
                {
                    return b2 ? 1 : -1;
                }
            }
            return o1.compareTo(o2);
        }
        
        private String prefix(String pattern)
        {
            int idx = pattern.lastIndexOf('.');
            if (idx != -1)
            {
                return pattern.substring(0, idx);
            }
            return "";
        }
    }
    private static class LevelHandler
    {
        Deque<String> stack = new ArrayDeque<>();
        Element current;

        public LevelHandler(Element current)
        {
            this.current = current;
        }

        public Element getCurrent()
        {
            return current;
        }
        boolean isList()
        {
            return "ul".equals(current.getName());
        }
        void add(Content content )
        {
            if (isList())
            {
                wrap(content);
            }
            else
            {
                current.addContent(content);
            }
        }
        void push(String pattern, Element element)
        {
            if (isList())
            {
                wrap(element);
                stack.push(pattern);
            }
            else
            {
                current.addContent(element);
            }
            current = element;
            stack.push(pattern);
        }
        void pop(String pattern)
        {
            while (!stack.isEmpty() && !pattern.startsWith(stack.peek()))
            {
                stack.pop();
                current = (Element) current.getParent();
            }
        }

        private void openCollapsibles()
        {
            Element e = current;
            while (e != null)
            {
                Attribute<?> attr = e.getAttr("data-role");
                if (attr != null && "collapsible".equals(attr.getValue()))
                {
                    e.setDataAttr("collapsed", false);
                }
                e = (Element) e.getParent();
            }
        }

        private void wrap(Content content)
        {
            Element li = current.addElement("li");
            if (content instanceof AttributedContent)
            {
                AttributedContent element = (AttributedContent) content;
                Attribute<?> id = element.getAttr("id");
                if (id != null)
                {
                    element.removeAttr("id");
                    li.setAttr("id", id.getValue());
                }
                if ("input".equals(element.getName()))
                {
                    li.addElement("a").setAttr("href", "#").addContent(content);
                    li.addElement("a").setAttr("href", "#").setDataAttr("pattern", id.getValue()).setDataAttr("icon", "delete").addClasses("delete");
                }
                else
                {
                    li.addContent(content);
                }
            }
        }
    }
}
