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
package org.vesalainen.html.jquery;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class SelectorFunctionTest
{
    
    public SelectorFunctionTest()
    {
    }

    @Test
    public void test1()
    {
        try
        {
            String exp = "$(\"p\").click(function(){$(this).hide();});";
            SelectorFunction sf = new SelectorFunction("p", "click");
            sf.addScript(new SelectorAction(null, "hide"));
            StringBuilder sb = new StringBuilder();
            sf.append(sb);
            assertEquals(exp, sb.toString());
        }
        catch (IOException ex)
        {
            fail(ex.getMessage());
            Logger.getLogger(SelectorFunctionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
