/*
 * Copyright (C) 2022 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.json;

import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.util.Lists;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class JSONBuilderTest
{
    
    public JSONBuilderTest()
    {
    }

    @Test
    public void testObject() throws IOException
    {
        JSONBuilder.Obj string = JSONBuilder.object()
                .number("number", ()->123)
                .string("string", ()->"qwerty")
                .nul("null")
                .bool("boolean", ()->true);
        StringBuilder out = new StringBuilder();
        string.write(out);
        JSONObject res = new JSONObject(out.toString());
        assertEquals(123, res.getDouble("number"), 1e-10);
        assertEquals("qwerty", res.getString("string"));
        assertEquals(true, res.isNull("null"));
        assertEquals(true, res.getBoolean("boolean"));
    }
    @Test
    public void testArray() throws IOException
    {
        JSONBuilder.Array string = JSONBuilder.array()
                .number(()->123)
                .string(()->"qwerty")
                .nul()
                .bool(()->true);
        StringBuilder out = new StringBuilder();
        string.write(out);
        JSONArray res = new JSONArray(out.toString());
        assertEquals(123, res.getDouble(0), 1e-10);
        assertEquals("qwerty", res.getString(1));
        assertEquals(true, res.isNull(2));
        assertEquals(true, res.getBoolean(3));
    }
    @Test
    public void testObjectArray() throws IOException
    {
        List<Object> list = Lists.create(1, 2, 3);
        JSONBuilder.Obj obj = JSONBuilder.object()
                .objectArray("arr", ()->list.stream());
        StringBuilder out = new StringBuilder();
        obj.write(out);
        JSONObject res = new JSONObject(out.toString());
        JSONArray jsonArray = res.getJSONArray("arr");
        assertEquals(1, jsonArray.getInt(0));
        assertEquals(2, jsonArray.getInt(1));
        assertEquals(3, jsonArray.getInt(2));
    }    
}
