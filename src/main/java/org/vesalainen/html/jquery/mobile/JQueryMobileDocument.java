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
package org.vesalainen.html.jquery.mobile;

import java.util.HashMap;
import java.util.Map;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Frameworks;
import org.vesalainen.html.jquery.JQueryDocument;
import org.vesalainen.js.ScriptContainer;
import org.vesalainen.web.servlet.bean.BeanForm;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <M>
 */
public class JQueryMobileDocument<M> extends JQueryDocument<M>
{
    private final Map<String,JQueryMobilePage> map = new HashMap<>();
    private boolean ajax = false;
    protected JQueryMobilePage defaultPage;
    
    public JQueryMobileDocument(ThreadLocal<Context<M>> threadLocalData)
    {
        this(threadLocalData, null);
    }

    public JQueryMobileDocument(ThreadLocal<Context<M>> threadLocalData, String title)
    {
        super(threadLocalData, title);
        use(Frameworks.JQueryMobile);
    }

    @Override
    public void init()
    {
        super.init();
    }

    public boolean isAjax()
    {
        return ajax;
    }

    public void setAjax(boolean ajax)
    {
        this.ajax = ajax;
    }
    
    public JQueryMobilePage getPage(String id)
    {
        JQueryMobilePage page = map.get(id);
        if (page == null)
        {
            page = new JQueryMobilePage(body, id, this);
            body.addContent(page);
            map.put(id, page);
        }
        return page;
    }

    @Override
    public ScriptContainer getScriptContainer()
    {
        return getDefaultPage().getScriptContainer();
    }

    @Override
    public BeanForm addForm(String id, String method, Object action)
    {
        JQueryMobilePage defPage = getDefaultPage();
        JQueryMobileForm form = new JQueryMobileForm(defPage, threadLocalData, id, method, action);
        defPage.addContent(form);
        return form;
    }

    @Override
    public JQueryMobileForm createForm(Content parent, String id, String method, Object action)
    {
        return new JQueryMobileForm(parent, threadLocalData, id, method, action);
    }

    public JQueryMobilePage getDefaultPage()
    {
        if (defaultPage == null)
        {
            defaultPage = getPage("defPage");
        }
        return defaultPage;
    }
    @Override
    public Element getBody()
    {
        return getDefaultPage().getMain();
    }

}
