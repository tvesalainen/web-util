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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.function.IntBinaryOperator;
import org.vesalainen.html.Element;
import org.vesalainen.math.DoubleTransform;
import org.vesalainen.ui.Drawer;
import org.vesalainen.ui.Plotter;
import org.vesalainen.ui.TextAlignment;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
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

        @Override
        public Rectangle2D bounds(String text)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void font(String name, int style, int size)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void text(double x, double y, TextAlignment alignment, String text)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFont(Font font)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setColor(Color color)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Color getColor()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setPaint(Paint paint)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setPattern(IntBinaryOperator pattern)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setStroke(BasicStroke stroke)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setTransform(DoubleTransform transform, double scale)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void draw(Shape shape)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void drawMark(Shape mark)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void fill(Shape shape)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void beginPath()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void moveTo(double... cp)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void drawLine(double... cp)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void drawQuad(double... cp)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void drawCubic(double... cp)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void closePath(double... cp)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> boolean supports(T target)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public <T> void write(T target)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
