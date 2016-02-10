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
package org.vesalainen.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.Lists;
import org.vesalainen.util.MapList;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 */
public class JsonHelperTest
{
    enum E {E1, E2};
    
    public JsonHelperTest()
    {
    }

    @Test
    public void test()
    {
        T t = new T();
        String js = JsonHelper.toString(t, "str");
        assertEquals("{\"str\":null}", js);
        JsonHelper.setValue(new JSONObject(js), t, "str");
        assertNull(t.str);
        
        js = JsonHelper.toString(t, "e");
        assertEquals("{\"e\":\"E2\"}", js);
        JsonHelper.setValue(new JSONObject(js), t, "e");
        assertEquals(E.E2, t.getE());
        
        js = JsonHelper.toString(t, "l");
        assertEquals("{\"l\":123456789}", js);
        JsonHelper.setValue(new JSONObject(js), t, "l");
        assertEquals(123456789L, t.getL());
        
        t.setStr("qwerty");
        js = JsonHelper.toString(t, "str");
        assertEquals("{\"str\":\"qwerty\"}", js);
        JsonHelper.setValue(new JSONObject(js), t, "str");
        assertEquals("qwerty", t.getStr());
        
        js = JsonHelper.toString(t, "iarr");
        assertEquals("{\"iarr\":[1,2,3,4,5]}", js);
        
        List<Integer> ints = Lists.create(9,8,7,6,5);
        js = JsonHelper.toString(t, "ints");
        assertEquals("{\"ints\":[9,8,7,6,5]}", js);
        JsonHelper.setValue(new JSONObject(js), t, "ints");
        assertEquals(ints, t.getInts());
        
        Map<String,String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("n", null);
        js = JsonHelper.toString(t, "map");
        assertEquals("{\"map\":{\"a\":\"1\",\"b\":\"2\",\"n\":null}}", js);
        JsonHelper.setValue(new JSONObject(js), t, "map");
        assertEquals(map, t.getMap());
        
        MapList<String,Integer> mapList = new HashMapList<>();
        mapList.add("a1", 1);
        mapList.add("a1", null);
        mapList.add("a2", 3);
        js = JsonHelper.toString(t, "mapList");
        assertEquals("{\"mapList\":{\"a1\":[1,null],\"a2\":[3]}}", js);
        JsonHelper.setValue(new JSONObject(js), t, "mapList");
        assertEquals(map, t.getMap());
        
        JB jb = new JB();
        js = JsonHelper.toString(t, "jb");
        assertEquals("{\"jb\":{\"ll\":123456,\"str1\":\"qwerty\",\"class\":\"org.vesalainen.json.JsonHelperTest$JB\"}}", js);
        JsonHelper.setValue(new JSONObject(js), t, "jb");
        assertEquals(jb, t.getJb());
        
    }
    public static class T
    {
        JB jb = new JB();
        E e = E.E2;
        String str;
        int[] iarr = new int[] {1,2,3,4,5};
        List<Integer> ints = Lists.create(9,8,7,6,5);
        Map<String,String> map = new HashMap<>();
        long l = 123456789L;
        MapList<String,Integer> mapList = new HashMapList<>();

        public T()
        {
            map.put("a", "1");
            map.put("b", "2");
            map.put("n", null);
            
            mapList.add("a1", 1);
            mapList.add("a1", null);
            mapList.add("a2", 3);
        }

        public JB getJb()
        {
            return jb;
        }

        public void setJb(JB jb)
        {
            this.jb = jb;
        }

        public E getE()
        {
            return e;
        }

        public void setE(E e)
        {
            this.e = e;
        }

        @InputType(itemType=String.class, itemType2=Integer.class)
        public MapList<String, Integer> getMapList()
        {
            return mapList;
        }

        public void setMapList(MapList<String, Integer> mapList)
        {
            this.mapList = mapList;
        }

        public long getL()
        {
            return l;
        }

        public void setL(long l)
        {
            this.l = l;
        }

        @InputType(itemType=String.class, itemType2=String.class)
        public Map<String, String> getMap()
        {
            return map;
        }

        public void setMap(Map<String, String> map)
        {
            this.map = map;
        }

        @InputType(itemType=Integer.class)
        public List<Integer> getInts()
        {
            return ints;
        }

        public void setInts(List<Integer> ints)
        {
            this.ints = ints;
        }

        public int[] getIarr()
        {
            return iarr;
        }

        public void setIarr(int[] iarr)
        {
            this.iarr = iarr;
        }

        public String getStr()
        {
            return str;
        }

        public void setStr(String str)
        {
            this.str = str;
        }
        
    }
    public static class JB implements JSONBean
    {
        private String str1 = "qwerty";
        public long ll = 123456L;

        public String getStr1()
        {
            return str1;
        }

        public void setStr1(String str1)
        {
            this.str1 = str1;
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.str1);
            hash = 97 * hash + (int) (this.ll ^ (this.ll >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final JB other = (JB) obj;
            if (!Objects.equals(this.str1, other.str1))
            {
                return false;
            }
            if (this.ll != other.ll)
            {
                return false;
            }
            return true;
        }
        
    }
}
