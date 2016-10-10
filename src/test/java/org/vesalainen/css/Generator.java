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
package org.vesalainen.css;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import org.junit.Test;

/**
 *
 * @author tkv
 */
public class Generator
{
    private static final int MIN = 400;
    private static final int MAX = 2000;
    private static final int STEP = 10;
    @Test
    public void genHeigth() throws IOException
    {
        try (FileWriter fw = new FileWriter("src/main/resources/org/vesalainen/web/jar/rwd-height.css"))
        {
            for (int h=MIN;h<MAX;h+=STEP)
            {
                fw.append(String.format(Locale.US, "@media screen and (min-height: %dpx) and (max-height: %dpx) {\n", h, h+STEP-1));
                for (int p=1;p<101;p++)
                {
                    fw.append(String.format(Locale.US, ".height-%d {height: %dpx;}\n", p, h*p/100));
                }
                fw.append("}\n");
            }
        }
    }
}
