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
import org.vesalainen.text.FormatUtil;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class SVGPath extends Element
{
    private static final long serialVersionUID = 1L;
    private StringBuilder sb = new StringBuilder();
    private char last;
    private boolean sep;

    public SVGPath()
    {
        super("path");
        setAttr("d", sb);
    }
    /**
     * Returns true if path is empty
     * @return 
     */
    public final boolean isEmpty()
    {
        return sb.length() == 0;
    }
    
    public final void moveTo(double... coord)
    {
        command('M');
        pairs(coord);
    }

    public final void moveToRel(double... coord)
    {
        command('m');
        pairs(coord);
    }
    
    public final void lineTo(double... coord)
    {
        command('L');
        pairs(coord);
    }

    public final void lineToRel(double... coord)
    {
        command('l');
        pairs(coord);
    }

    public final void horizontalLineTo(double x)
    {
        command('H');
        single(x);
    }
    public final void verticalLineTo(double y)
    {
        command('V');
        single(y);
    }
    public final void horizontalLineToRel(double x)
    {
        command('h');
        single(x);
    }
    public final void verticalLineToRel(double y)
    {
        command('v');
        single(y);
    }
    public final void curveTo(double x1, double y1, double x2, double y2, double x, double y)
    {
        command('C');
        single(x1);
        single(y1);
        single(x2);
        single(y2);
        single(x);
        single(y);
    }
    public final void curveToRel(double x1, double y1, double x2, double y2, double x, double y)
    {
        command('c');
        single(x1);
        single(y1);
        single(x2);
        single(y2);
        single(x);
        single(y);
    }
    public final void curveTo(double x2, double y2, double x, double y)
    {
        command('S');
        single(x2);
        single(y2);
        single(x);
        single(y);
    }
    public final void curveToRel(double x2, double y2, double x, double y)
    {
        command('s');
        single(x2);
        single(y2);
        single(x);
        single(y);
    }
    public final void quadraticCurveTo(double x1, double y1, double x, double y)
    {
        command('Q');
        single(x1);
        single(y1);
        single(x);
        single(y);
    }
    public final void quadraticCurveToRel(double x1, double y1, double x, double y)
    {
        command('q');
        single(x1);
        single(y1);
        single(x);
        single(y);
    }
    public final void quadraticCurveTo(double x, double y)
    {
        command('T');
        single(x);
        single(y);
    }
    public final void quadraticCurveToRel(double x, double y)
    {
        command('t');
        single(x);
        single(y);
    }
    public final void ellipticalArc(double rx, double ry, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag, double x, double y)
    {
        command('A');
        single(rx);
        single(ry);
        single(xAxisRotation);
        flag(largeArcFlag);
        flag(sweepFlag);
        single(x);
        single(y);
    }
    public final void ellipticalArcRel(double rx, double ry, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag, double x, double y)
    {
        command('a');
        single(rx);
        single(ry);
        single(xAxisRotation);
        flag(largeArcFlag);
        flag(sweepFlag);
        single(x);
        single(y);
    }
    public final void closePath()
    {
        command('Z');
    }
    public final void closePathRel()
    {
        command('z');
    }
    
    private void pairs(double... coord)
    {
        if (coord.length % 2 != 0)
        {
            throw new IllegalArgumentException("not coordinate pairs");
        }
        for (double v : coord)
        {
            single(v);
        }
        sep = false;
    }
    private void command(char c)
    {
        if (!(
                (c == last) ||
                (last == 'M' && c == 'L') ||
                (last == 'm' && c == 'l')
                ))
        {
            sb.append(c);
            sep = true;
        }
        last = c;
    }

    private void single(double v)
    {
        if (!sep)
        {
            sb.append(' ');
        }
        FormatUtil.format(sb, v);
        sep = false;
    }

    private void flag(boolean f)
    {
        if (!sep)
        {
            sb.append(' ');
        }
        if (f)
        {
            sb.append('1');
        }
        else
        {
            sb.append('0');
        }
        sep = false;
    }

}
