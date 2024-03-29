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
package org.vesalainen.html;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class AbstractFrameworkTest
{
    
    public AbstractFrameworkTest()
    {
    }

    @Test
    public void test1()
    {
        Document page = new Document("Test");
        page.init();
        page.use(Frameworks.Bootstrap);
        String exp = "<!DOCTYPE HTML>\n" +
            "<html><head>"+
                "<meta charset=\"UTF-8\">"+
                "<title>Test</title>"+
                "<script src=\"/jquery-3.6.0.min.js\"></script>"+
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"+
                "<link rel=\"stylesheet\" href=\"/bootstrap-3.3.6-dist/css/bootstrap.min.css\">"+
                "<script src=\"/bootstrap-3.3.6-dist/js/bootstrap.min.js\"></script>"+
                "</head><body></body></html>";
        String got = page.toString();
        assertEquals(exp, got);
    }
    
}
