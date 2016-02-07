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
import java.util.Locale;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.js.ScriptContainer;

/**
 *
 * @author tkv
 * @param <C>
 */
public class JQueryMobilePage<C> extends Element implements Page
{
    protected Object id;
    protected Element header;
    protected Element main;
    protected Element footer;
    protected ScriptContainer script;
    private final JQueryMobileDocument<C> document;

    public JQueryMobilePage(Object id, final JQueryMobileDocument<C> document)
    {
        super("div");
        this.document = document;
        this.id = id;
        setAttr("data-role", "page");
        setAttr("id", id);
        main = new Element("div").setAttr("data-role", "main").addClasses("ui-content");
        addContent(main);
    }

    public Object getId()
    {
        return id;
    }

    public Element getHeader()
    {
        if (header == null)
        {
            header = new Element("div").setAttr("data-role", "header");
            insertContent(header);
        }
        return header;
    }

    public Element getMain()
    {
        return main;
    }

    public Element getFooter()
    {
        if (footer == null)
        {
            footer = new Element("div").setAttr("data-role", "footer");
            addContent(footer);
        }
        return footer;
    }

    @Override
    public ScriptContainer getScriptContainer()
    {
        if (script == null)
        {
            ScriptContainer sc = document.getScriptContainer();
            script = new AbstractScriptContainer("$(document).on(\"pagecreate\",\"#"+id+"\",function(){", "});");
            sc.addScript(script);
        }
        return script;
    }

    @Override
    public Element getContent()
    {
        return main;
    }

    @Override
    public JQueryMobileForm addForm(String action)
    {
        return addForm("post", action);
    }

    @Override
    public JQueryMobileForm addForm(String method, String action)
    {
        JQueryMobileForm form = new JQueryMobileForm(document, this, method, action);
        main.addElement(form);
        return form;
    }

    @Override
    public Charset getCharset()
    {
        return document.getCharset();
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
    
}
