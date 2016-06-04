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

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import org.vesalainen.html.Element;
import org.vesalainen.ui.Drawer;
import org.vesalainen.ui.Plotter;

/**
 *
 * @author tkv
 */
public class SVGPlotter extends Plotter
{

    public SVGPlotter(int width, int height)
    {
        super(width, height);
    }

    public SVGPlotter(int width, int height, Color background)
    {
        super(width, height, background);
    }

    @Override
    public void plot(File file, String ext) throws IOException
    {
        if ("svg".equalsIgnoreCase(ext))
        {
            SVGDocument svg = new SVGDocument();
            svg.setAttr("viewBox", String.format(Locale.US, "%f %f %f %f", 0.0, 0.0, width, height));
            SVGDrawer drawer = new SVGDrawer(svg);
            drawables.stream().forEach((d) ->
            {
                d.draw(drawer);
            });
            svg.write(new FileOutputStream(file));
        }
        else
        {
            super.plot(file, ext);
        }
    }
    
    private static class SVGDrawer implements Drawer
    {
        private SVGDocument svg;
        private Color color;

        public SVGDrawer(SVGDocument svg)
        {
            this.svg = svg;
        }
        
        @Override
        public void color(Color color)
        {
            this.color = color;
        }

        @Override
        public void circle(double x, double y, double r)
        {
            Element circle = svg.addElement("circle").setAttr("cx", x).setAttr("cy", y).setAttr("r", r);
            if (color != null)
            {
                circle.setAttr("style", String.format("stroke:rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()));
            }
        }

        @Override
        public void ellipse(double x, double y, double rx, double ry)
        {
            Element ellipse = svg.addElement("ellipse").setAttr("cx", x).setAttr("cy", y).setAttr("rx", rx).setAttr("ry", ry);
            if (color != null)
            {
                ellipse.setAttr("style", String.format("stroke:rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()));
            }
        }

        @Override
        public void line(double x1, double y1, double x2, double y2)
        {
            Element line = svg.addElement("line").setAttr("x1", x1).setAttr("y1", y1).setAttr("x2", x2).setAttr("y2", y2);
            if (color != null)
            {
                line.setAttr("style", String.format("stroke:rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()));
            }
        }

        @Override
        public void polyline(double[] x, double[] y)
        {
            if (x.length != y.length)
            {
                throw new IllegalArgumentException("dimensions differ");
            }
            Element polyline = svg.addElement("polyline");
            StringBuilder sb = new StringBuilder();
            int len = x.length;
            for (int ii=0;ii<len;ii++)
            {
                sb.append(x[ii]);
                sb.append(',');
                sb.append(y[ii]);
                sb.append(' ');
            }
            polyline.setAttr("points", sb.toString());
            if (color != null)
            {
                polyline.setAttr("style", String.format("fill:none;stroke:rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()));
            }
        }
        
    }
}
