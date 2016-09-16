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

import org.vesalainen.html.Element;
import org.vesalainen.util.Lists;

/**
 *
 * @author tkv
 */
public class CircleScale extends Element
{

    public CircleScale()
    {
        super("g");
        
        String a1 = Lists.print(" ", 0);
        String a2 = Lists.print(" ", 357, 3);
        String a3 = Lists.print(" ", 355, 0, 5);
        
        String x1 = Lists.print(" ", Math.sin(Math.toRadians(0)));
        String x2 = Lists.print(" ", Math.sin(Math.toRadians(357)), Math.sin(Math.toRadians(3)));
        String x3 = Lists.print(" ", Math.sin(Math.toRadians(355)), Math.sin(Math.toRadians(0)), Math.sin(Math.toRadians(5)));
        
        String y1 = Lists.print(" ", -Math.cos(Math.toRadians(0)));
        String y2 = Lists.print(" ", -Math.cos(Math.toRadians(357)), -Math.cos(Math.toRadians(3)));
        String y3 = Lists.print(" ", -Math.cos(Math.toRadians(355)), -Math.cos(Math.toRadians(0)), -Math.cos(Math.toRadians(5)));
        
        for (int a = 0; a < 360; a += 30)
        {
            Element text = addElement("text").setAttr("text-anchor", "middle").setAttr("transform", "rotate(" + a + ")");
            String l = String.valueOf(a);
            text.addText(l);
            String angles = null;
            String x = null;
            String y = null;
            switch (l.length())
            {
                case 1:
                    y = y1;
                    x = x1;
                    angles = a1;
                    break;
                case 2:
                    y = y2;
                    x = x2;
                    angles = a2;
                    break;
                case 3:
                    y = y3;
                    x = x3;
                    angles = a3;
                    break;
            }
            text.setAttr("x", x);
            text.setAttr("y", y);
            text.setAttr("rotate", angles);
        }
    }
    
}
