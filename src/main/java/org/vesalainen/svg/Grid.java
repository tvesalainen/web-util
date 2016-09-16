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

/**
 *
 * @author tkv
 */
public class Grid extends Element
{
    public Grid(int start, int end, int min, int max, int step, boolean vertical)
    {
        super("g");
        
        char fixed = vertical ? 'y' : 'x';
        char var = vertical ? 'x' : 'y';
        for (int a = start; a<= end; a+= step)
        {
            if (step == 1 && (a % 5) == 0)
            {
                continue;
            }
            if (step == 5 && (a % 10) == 0)
            {
                continue;
            }
            Element line = addElement("line");  // TODO change to path
            line.setAttr(fixed+"1", min);
            line.setAttr(var+"1", a);
            line.setAttr(fixed+"2", max);
            line.setAttr(var+"2", a);
        }
    }
    
}
