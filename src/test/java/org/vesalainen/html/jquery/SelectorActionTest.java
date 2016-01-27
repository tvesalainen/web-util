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
package org.vesalainen.html.jquery;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tkv
 */
public class SelectorActionTest
{
    
    public SelectorActionTest()
    {
    }

    @Test
    public void test1()
    {
        try
        {
            String exp = "$(this).css(\"background-color\",\"#ffffff\");";
            StringBuilder sb = new StringBuilder();
            SelectorAction sa = new SelectorAction(null, "css", "background-color", "#ffffff");
            sa.append(sb);
            assertEquals(exp, sb.toString());
        }
        catch (IOException ex)
        {
            Logger.getLogger(SelectorActionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
