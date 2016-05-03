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
package org.vesalainen.html;

import javax.xml.bind.annotation.XmlType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.web.servlet.bean.BeanRenderer;

/**
 *
 * @author tkv
 */
public class RendererSupportTest
{
    
    public RendererSupportTest()
    {
    }

    @Test
    public void test()
    {
        StringBuilder sb = new StringBuilder();
        RendererSupport.render(new C2(), sb);
        assertEquals("<div></div><span title=\"test\"></span>", sb.toString());
    }

    public class C1 extends BeanRenderer
    {
        public String title = "test";
        
        @Override
        protected Renderer create()
        {
            return new Element("span").setAttr("title", "${title}");
        }
        
    }
    @XmlType(propOrder={"el", "c1"})
    public class C2
    {
        protected Element el = new Element("div");
        protected C1 c1 = new C1();

        public Element getEl()
        {
            return el;
        }

        public C1 getC1()
        {
            return c1;
        }

        public void setEl(Element el)
        {
            this.el = el;
        }

        public void setC1(C1 c1)
        {
            this.c1 = c1;
        }
        
    }
}
