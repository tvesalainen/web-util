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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import org.vesalainen.html.Element;

/**
 *
 * @author tkv
 */
public class SVGDocument extends Element
{

    public SVGDocument()
    {
        super(null, "svg");
        setAttr("xmlns", "http://www.w3.org/2000/svg");
        setAttr("xml:space", "preserve");
        setAttr("xmlns:xlink", "http://www.w3.org/1999/xlink");
    }
    
    public void write(OutputStream os) throws IOException
    {
        OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        appendDoctype(writer);
        append(writer);
        writer.flush();
    }
    
    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            appendDoctype(sb);
            append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private void appendDoctype(Appendable out) throws IOException
    {
        out.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        out.append("<!DOCTYPE svg  PUBLIC '-//W3C//DTD SVG 1.1//EN'  'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>\n");
    }
    
}
