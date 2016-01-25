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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkv
 */
public class TagTest
{
    
    public TagTest()
    {
    }

    @Test
    public void test()
    {
        Element html = new Element("html");
        assertEquals("<html></html>", html.toString());
        html.addAttr("lang", "fi");
        assertEquals("<html lang=\"fi\"></html>", html.toString());
        html.addAttr("test", true);
        assertEquals("<html lang=\"fi\" test=\"true\"></html>", html.toString());
        Element head = html.addElement("head");
        assertEquals("<html lang=\"fi\" test=\"true\"><head></head></html>", html.toString());
        Element div = head.addElement("div").addAttr("id", 13);
        assertEquals("<html lang=\"fi\" test=\"true\"><head><div id=\"13\"></div></head></html>", html.toString());
        div.addText("Hello <There>");
        assertEquals("<html lang=\"fi\" test=\"true\"><head><div id=\"13\">Hello &lt;There&gt;</div></head></html>", html.toString());
    }
    
}
