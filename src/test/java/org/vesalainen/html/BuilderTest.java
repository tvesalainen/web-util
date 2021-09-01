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

import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class BuilderTest
{
    
    public BuilderTest()
    {
    }

    @Test
    public void test()
    {
        Builder<String,?,Element> b = new Builder<>((s)->
        {
            Element e = new Element("div");
            e.addText(s);
            return e;
        });
        b.mapArray((String s)->s.split(" "), (x)->
        {
            Element t = new Element(x);
            t.addText(x);
            return t;
        });
        Stream<String> s = Stream.of("fo o", "b ar");
        Element h1 = new Element("h1");
        b.build(h1, s);
        System.err.println(h1);
    }
    
}
