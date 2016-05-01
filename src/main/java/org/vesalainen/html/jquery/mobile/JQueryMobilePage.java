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
package org.vesalainen.html.jquery.mobile;

import java.nio.charset.Charset;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.Tag;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.js.ScriptContainer;

/**
 *
 * @author tkv
 * @param <M>
 */
public class JQueryMobilePage<M> extends Element implements Page
{
    protected Object id;
    protected Element header;
    protected Element main;
    protected Element footer;
    protected ScriptContainer script;
    private final JQueryMobileDocument<M> document;
    protected boolean domCache = false;

    public JQueryMobilePage(Content parent, Object id, final JQueryMobileDocument<M> document)
    {
        super(parent, "div");
        this.document = document;
        this.id = id;
        setAttr("data-role", "page");
        setAttr("id", id);
        setDataAttr("dom-cache", domCache);
        main = new Element(this, "div").setDataAttr("role", "main").addClasses("ui-content");
        super.addContent(main);
    }

    public Object getId()
    {
        return id;
    }

    public Element getHeader()
    {
        if (header == null)
        {
            header = new Element(this, "div").setAttr("data-role", "header");
            insertContent(header);
        }
        return header;
    }

    public Element getMain()
    {
        return main;
    }

    @Override
    public Element addText(Renderer renderer)
    {
        return main.addText(renderer);
    }

    @Override
    public <T> Element addText(T text)
    {
        return main.addText(text);
    }

    @Override
    public Tag addTag(String tagName)
    {
        return main.addTag(tagName);
    }

    @Override
    public Element addTag(Tag tag)
    {
        return main.addTag(tag);
    }

    @Override
    public Element addElement(Element element)
    {
        return main.addElement(element);
    }

    @Override
    public Element addContent(Content c)
    {
        return main.addContent(c);
    }

    public Element getFooter()
    {
        if (footer == null)
        {
            footer = new Element(this, "div").setAttr("data-role", "footer");
            addContent(footer);
        }
        return footer;
    }

    @Override
    public ScriptContainer getScriptContainer()
    {
        if (script == null)
        {
            Element se = document.getHead().addElement("script");
            script = new AbstractScriptContainer("$(document).on(\"pagecreate\",\"#"+id+"\",function(){", "});");
            se.addRenderer(script);
        }
        return script;
    }

    @Override
    public Element getContent()
    {
        return main;
    }

    @Override
    public JQueryMobileForm addForm(Object action)
    {
        return addForm("post", action);
    }

    @Override
    public JQueryMobileForm addForm(String method, Object action)
    {
        JQueryMobileForm form = new JQueryMobileForm(this, document.getThreadLocalData(), method, action);
        main.addElement(form);
        return form;
    }

    @Override
    public Charset getCharset()
    {
        return document.getCharset();
    }

}
