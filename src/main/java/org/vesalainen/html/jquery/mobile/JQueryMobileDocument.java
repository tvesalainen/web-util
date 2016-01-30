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
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Frameworks;

/**
 *
 * @author tkv
 */
public class JQueryMobileDocument extends Document
{
    private final Map<String,Page> map = new HashMap<>();
    
    public JQueryMobileDocument()
    {
        this(null);
    }

    public JQueryMobileDocument(String title)
    {
        super(title);
        use(Frameworks.JQueryMobile);
    }
    
    public Page getPage(String id)
    {
        Page page = map.get(id);
        if (page == null)
        {
            page = new Page(id);
            body.addElement(page);
            map.put(id, page);
        }
        return page;
    }
    
    public static class Page extends Element
    {
        private Element header;
        private Element main;
        private Element footer;

        public Page(String id)
        {
            super("div");
            addAttr("data-role", "page");
            addAttr("id", id);
            main = new Element("div")
                    .addAttr("data-role", "main")
                    .addClasses("ui-content");
            addContent(main);
        }

        public Element getHeader()
        {
            if (header == null)
            {
                header = new Element("div")
                        .addAttr("data-role", "header");
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
                footer = new Element("div")
                        .addAttr("data-role", "footer");
                addContent(footer);
            }
            return footer;
        }
        
    }
    
}
