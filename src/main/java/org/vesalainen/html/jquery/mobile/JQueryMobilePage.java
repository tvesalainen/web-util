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

import org.vesalainen.html.ContainerContent;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.Tag;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.js.ScriptContainer;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 * @param <M>
 */
public class JQueryMobilePage<M> extends ContainerContent implements Page
{
    protected Object id;
    protected ScriptContainer script;
    protected Element divPage;
    protected Element header;
    protected Element main;
    protected Element footer;
    protected boolean domCache = false;
    protected char theme = 'a';
    protected ThreadLocal<Context<M>> threadLocalData;

    public JQueryMobilePage(Content parent, Object id, final JQueryMobileDocument<M> document)
    {
        this(parent, id, document.getThreadLocalData());
    }
    public JQueryMobilePage(Content parent, Object id, ThreadLocal<Context<M>> threadLocalData)
    {
        super(parent);
        this.threadLocalData = threadLocalData;
        this.id = id;
        
        Element se = new Element(this, "script");
        script = new AbstractScriptContainer("$(document).on(\"pagecreate\",\"#"+id+"\",function(){", "});");
        se.addRenderer(script);
        content.add(se);
        
        divPage = new Element(this, "div")
                .setAttr("data-role", "page")
                .setAttr("id", id)
                .setDataAttr("dom-cache", this::isDomCache)
                .setDataAttr("theme", this::getTheme)
                ;
        content.add(divPage);
        
        header = divPage.addElement("div").setAttr("data-role", "header");
        
        main = divPage.addElement("div").setDataAttr("role", "main").addClasses("ui-content");
        
        footer = divPage.addElement("div").setAttr("data-role", "footer");
    }

    public Object getId()
    {
        return id;
    }

    public Element getDivPage()
    {
        return divPage;
    }

    public Element getHeader()
    {
        return header;
    }

    public Element getMain()
    {
        return main;
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
    public Element addContent(Content c)
    {
        return main.addContent(c);
    }

    public Element getFooter()
    {
        return footer;
    }

    @Override
    public ScriptContainer getScriptContainer()
    {
        return script;
    }

    @Override
    public Element getContent()
    {
        return main;
    }

    @Override
    public JQueryMobileForm addForm(String method, String id, Object action)
    {
        JQueryMobileForm form = createForm(parent, method, id, action);
        main.addContent(form);
        return form;
    }

    @Override
    public JQueryMobileForm createForm(Content parent, String method, String id, Object action)
    {
        return new JQueryMobileForm(this, threadLocalData, id, method, action);
    }

    @Override
    public void addToHeader(Renderer content)
    {
        getHeader().add(content);
    }

    @Override
    public void addToFooter(Renderer content)
    {
        getFooter().add(content);
    }

    public boolean isDomCache()
    {
        return domCache;
    }

    public void setDomCache(boolean domCache)
    {
        this.domCache = domCache;
    }

    public char getTheme()
    {
        return theme;
    }

    public void setTheme(char theme)
    {
        this.theme = theme;
    }

}
