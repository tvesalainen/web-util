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
package org.vesalainen.http;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class QueryTest
{
    
    public QueryTest()
    {
    }

    @Test
    public void test1()
    {
        Query q1 = new Query();
        q1.add("p1", "foo bar");
        q1.add("p1", "ääliö");
        q1.add("p2", "bar is open");
        String qs = q1.toString();
        Query q2 = new Query(qs);
        List<String> ls = q2.get("p1");
        assertEquals(2, ls.size());
        assertTrue(ls.contains("foo bar"));
        assertTrue(ls.contains("ääliö"));
        ls = q2.get("p2");
        assertEquals(1, ls.size());
        assertTrue(ls.contains("bar is open"));
    }
    
}
