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

import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.ui.Scaler;

/**
 * SVGCoordinates draws coordinate system with x and y scales and grids
 * @author tkv
 */
public class SVGCoordinates extends Element
{
    private double minX;
    private double minY;
    private double width;
    private double height;

    public SVGCoordinates(Content parent, double minX, double minY, double width, double height)
    {
        super(parent, "g");
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        Scaler horScaler = new Scaler(minX, minX+width);
        Scaler verScaler = new Scaler(minY, minY+height);
        double strokeWidth = horScaler.step() / 14.0;
        SVGPath verGrid = new SVGPath();
        addContent(verGrid);
        verGrid.setAttr("stroke", "black");
        verGrid.setAttr("stroke-width", strokeWidth);
        
        verScaler.stream().forEach((v)->
        {
            verGrid.moveTo(minX, v);
            verGrid.horizontalLineToRel(width);
        });
    }

}
