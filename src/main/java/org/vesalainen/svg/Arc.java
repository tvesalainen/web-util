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
package org.vesalainen.svg;

import org.vesalainen.html.Element;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Arc extends Element
{

    public Arc(double r1, double cx, double cy, int step, double length)
    {
        this(r1, cx, cy, 0, 360, step, length);
    }
    public Arc(double r1, double cx, double cy, int start, int end, int step, double length)
    {
        super("path");
        StringBuilder sb = new StringBuilder();
        double r2 = r1 - length;
        for (int a = start; a < end; a += step)
        {
            if (step == 1 && (a % 5) == 0)
            {
                continue;
            }
            if (step == 5 && (a % 10) == 0)
            {
                continue;
            }
            double rad = Math.toRadians(a);
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            double x1 = cx + sin * r1;
            double y1 = cy - cos * r1;
            double x2 = cx + sin * r2;
            double y2 = cy - cos * r2;
            if (a > 0)
            {
                sb.append(" ");
            }
            sb.append("M");
            sb.append(x1);
            sb.append(",");
            sb.append(y1);
            sb.append("L");
            sb.append(x2);
            sb.append(",");
            sb.append(y2);
        }
        setAttr("d", sb.toString());
    }
    
}
