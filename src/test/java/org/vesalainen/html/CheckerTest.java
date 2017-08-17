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
package org.vesalainen.html;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class CheckerTest
{
    
    public CheckerTest()
    {
    }

    @Test
    public void test1()
    {
        Element el = new Element("div").setAttr("id", 1);
        el.addElement("span").setAttr("id", 1);
        try
        {
            Checker.checkIds(el);
            fail("should have thrown");
        }
        catch (IllegalArgumentException ex)
        {
            
        }
    }
    
    @Test
    public void test2()
    {
        Element el = new Element("div").setAttr("id", 1);
        el.addElement("span").setAttr("id", 2);
        try
        {
            Checker.checkIds(el);
        }
        catch (IllegalArgumentException ex)
        {
            fail("should not have thrown");
        }
    }
    
}
