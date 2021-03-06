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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Test;
import org.vesalainen.html.Element;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class SVGCoordinatesTest
{
    
    public SVGCoordinatesTest()
    {
    }

    @Test
    public void test1() throws IOException
    {
        SVGDocument doc = new SVGDocument();
        Element svg = doc.addElement("svg")
                .setAttr("viewBox", "-1, -1, 20, 2");
        SVGCoordinates coord = new SVGCoordinates(-1, -1, 20, 2);
        svg.addContent(coord);
        File file = File.createTempFile("coordinates-test", ".svg");
        try (FileWriter fos = new FileWriter(file))
        {
            doc.append(fos);
        }
    }
    
}
