/*
 * Copyright (C) 2021 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.html;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class AbstractDynamicInputTest
{
    
    public AbstractDynamicInputTest() throws IOException
    {
        Inp inp = new Inp();
        inp.addClasses("c1");
        inp.setAttr("name", "value");
        StringBuilder sb = new StringBuilder();
        inp.append(sb, Integer.SIZE);
        assertEquals("<input type=\"number\" min=\"-2147483648\" max=\"2147483647\" pattern=\"[\\-\\+]?[0-9]+\" value=\"32\" name=\"value\" class=\"c1\">", sb.toString());
    }

    @Test
    public void test()
    {
    }
    private class Inp extends AbstractDynamicInput<Integer>
    {

        @Override
        public void append(Appendable out, Integer t) throws IOException
        {
            append(out, t, Integer.class, t);
        }
        
    }
}
