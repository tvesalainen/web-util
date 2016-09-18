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

    public SVGCoordinates(double minX, double minY, double width, double height)
    {
        super("g");
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
        Scaler horScaler = new Scaler(minX, minX+width);
        Scaler verScaler = new Scaler(minY, minY+height);
        
        double strokeWidth = height/1000;
        // vertical
        double verLevel = verScaler.level();
        SVGPath verGrid1 = new SVGPath();
        addContent(verGrid1);
        verGrid1.setAttr("stroke", "gray");
        verGrid1.setAttr("stroke-width", strokeWidth);
        
        verScaler.stream(1).forEach((v)->
        {
            verGrid1.moveTo(minX, v);
            verGrid1.horizontalLineToRel(width);
        });
        SVGPath verGrid0 = new SVGPath();
        addContent(verGrid0);
        verGrid0.setAttr("stroke", "gray");
        verGrid0.setAttr("stroke-width", strokeWidth*2);

        Element verScale = addElement("text");
        verScale.setAttr("text-anchor", "start");
        verScale.setAttr("dominant-baseline", "central");
        double fontSize = Math.min(height/20, verScaler.step(verLevel));
        verScale.setAttr("font-size", fontSize);
        
        verScaler.stream(verLevel).forEach((v)->
        {
            verGrid0.moveTo(minX, v);
            verGrid0.horizontalLineToRel(width);
        });
        String verFormat = verScaler.getFormat(verLevel);
        long verCount = (long) verScaler.count(verLevel);
        verScaler.stream(verLevel).skip(1).limit(verCount-2).forEach((v)->
        {
            Element tspan = verScale.addElement("tspan");
            tspan.addText(String.format(verFormat, -v));
            tspan.setAttr("y", v);
            tspan.setAttr("x", minX);
        });
        // horizontal
        double horLevel = horScaler.level();
        double floor = minY+height;
        SVGPath horGrid1 = new SVGPath();
        addContent(horGrid1);
        horGrid1.setAttr("stroke", "gray");
        horGrid1.setAttr("stroke-width", strokeWidth);
        
        horScaler.stream(1).skip(1).forEach((v)->
        {
            horGrid1.moveTo(v, minY);
            horGrid1.verticalLineToRel(height);
        });
        SVGPath horGrid0 = new SVGPath();
        addContent(horGrid0);
        horGrid0.setAttr("stroke", "gray");
        horGrid0.setAttr("stroke-width", strokeWidth*2);

        Element horScale = addElement("text");
        horScale.setAttr("text-anchor", "middle");
        horScale.setAttr("font-size", fontSize);
        
        horScaler.stream(horLevel).skip(1).forEach((v)->
        {
            horGrid0.moveTo(v, minY);
            horGrid0.verticalLineToRel(height);
        });
        String horFormat = horScaler.getFormat(horLevel);
        long horCount = (long) horScaler.count(horLevel);
        horScaler.stream(horLevel).skip(1).limit(horCount-2).forEach((v)->
        {
            Element tspan = horScale.addElement("tspan");
            tspan.addText(String.format(horFormat, v));
            tspan.setAttr("y", floor);
            tspan.setAttr("x", v);
        });
        
    }

}
