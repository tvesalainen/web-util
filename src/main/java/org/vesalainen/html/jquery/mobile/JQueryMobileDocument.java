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

import java.util.HashMap;
import java.util.Map;
import org.vesalainen.html.Frameworks;
import org.vesalainen.web.servlet.bean.BeanDocument;

/**
 *
 * @author tkv
 * @param <C>
 */
public class JQueryMobileDocument<C> extends BeanDocument<C>
{
    private final Map<String,JQueryMobilePage> map = new HashMap<>();
    private boolean ajax = true;
    
    public JQueryMobileDocument(ThreadLocal<C> threadLocalData)
    {
        this(threadLocalData, null);
    }

    public JQueryMobileDocument(ThreadLocal<C> threadLocalData, String title)
    {
        super(threadLocalData, title);
        use(Frameworks.JQueryMobile);
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
            page = new JQueryMobilePage(id, this);
            body.addElement(page);
            map.put(id, page);
        }
        return page;
    }
    
    
}
