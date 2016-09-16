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
package org.vesalainen.svg;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkv
 */
public class SVGPathTest
{
    
    public SVGPathTest()
    {
    }

    @Test
    public void testM()
    {
        String exp = "<path d=\"M100 100 200 200\"></path>";
        SVGPath p = new SVGPath(Locale.US);
        p.moveTo(100, 100);
        p.lineTo(200, 200);
        assertEquals(exp, p.toString());
    }
    
    @Test
    public void testMRel()
    {
        String exp = "<path d=\"m100 100 200 200\"></path>";
        SVGPath p = new SVGPath(Locale.US);
        p.moveToRel(100, 100);
        p.lineToRel(200, 200);
        assertEquals(exp, p.toString());
    }
    
}
