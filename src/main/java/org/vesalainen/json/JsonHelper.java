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

import java.util.Collection;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.util.ConvertUtility;
import org.vesalainen.util.MapCollection;
import org.vesalainen.web.InputType;

/**
 *
 * @author tkv
 */
public class JsonHelper
{
    
    public static void setValues(JSONObject json, Object base)
    {
        for (String field : json.keySet())
        {
            setValue(json, base, field);
        }
    }
    
    public static void setValue(JSONObject json, Object base, String field)
    {
        Object value = BeanHelper.getFieldValue(base, field);
        InputType inputType = BeanHelper.getAnnotation(base, field, InputType.class);
        Object jo = json.get(field);
        if (JSONObject.NULL.equals(jo))
        {
            BeanHelper.setFieldValue(base, field, null);
        }
        else
        {
            if (value instanceof Collection)
            {
                JSONArray ja = (JSONArray) jo;
                int length = ja.length();
                if (inputType == null || Object.class.equals(inputType.itemType()))
                {
                    throw new IllegalArgumentException("@InputType.itemType() is missing");
                }
                Collection col = (Collection) value;
                col.clear();
                Class<?> itemType = inputType.itemType();
                for (int ii=0;ii<length;ii++)
                {
                    Object o = ja.get(ii);
                    col.add(ConvertUtility.convert(itemType, o));
                }
            }
            else
            {
                if (value instanceof MapCollection)
                {
                    if (inputType == null || Object.class.equals(inputType.itemType()) || Object.class.equals(inputType.itemType2()))
                    {
                        throw new IllegalArgumentException("@InputType.itemType() and/or @InputType.itemType2() is missing");
                    }
                    Class<?> itemType = inputType.itemType();
                    Class<?> itemType2 = inputType.itemType2();
                    MapCollection mapCollection = (MapCollection) value;
                    mapCollection.clear();
                    JSONObject jj = (JSONObject) jo;
                    for (String key : jj.keySet())
                    {
                        JSONArray ja = (JSONArray) jj.get(key);
                        for (Object o : ja)
                        {
                            mapCollection.add(ConvertUtility.convert(itemType, key), ConvertUtility.convert(itemType2, o));
                        }
                    }
                }
                else
                {
                    if (value instanceof Map)
                    {
                        if (inputType == null || Object.class.equals(inputType.itemType()) || Object.class.equals(inputType.itemType2()))
                        {
                            throw new IllegalArgumentException("@InputType.itemType() and/or @InputType.itemType2() is missing");
                        }
                        Class<?> itemType = inputType.itemType();
                        Class<?> itemType2 = inputType.itemType2();
                        Map map = (Map) value;
                        map.clear();
                        JSONObject jj = (JSONObject) jo;
                        for (String key : jj.keySet())
                        {
                            Object o = jj.get(key);
                            map.put(ConvertUtility.convert(itemType, key), ConvertUtility.convert(itemType2, o));
                        }
                    }
                    else
                    {
                        BeanHelper.setFieldValue(base, field, jo);
                    }
                }
            }
        }
    }
    public static String toString(Object base, String field)
    {
        JSONObject json = new JSONObject();
        add(json, base, field);
        return json.toString();
    }
    public static void add(JSONObject json, Object base, String field)
    {
        Object value = BeanHelper.getFieldValue(base, field);
        if (value == null)
        {
            json.put(field, JSONObject.NULL);
        }
        else
        {
            Class type = BeanHelper.getType(base, field);
            if (type.isArray())
            {
                JSONArray jarr = new JSONArray();
                Class componentType = type.getComponentType();
                switch (componentType.getSimpleName())
                {
                    case "boolean":
                        boolean[] ba = (boolean[]) value;
                        for (boolean i : ba)
                        {
                            jarr.put(i);
                        }
                    break;
                    case "long":
                        long[] la = (long[]) value;
                        for (long i : la)
                        {
                            jarr.put(i);
                        }
                    break;
                    case "int":
                        int[] ia = (int[]) value;
                        for (int i : ia)
                        {
                            jarr.put(i);
                        }
                    break;
                    case "double":
                        double[] da = (double[]) value;
                        for (double i : da)
                        {
                            jarr.put(i);
                        }
                    break;
                    default:
                        if (componentType.isPrimitive())
                        {
                            throw new IllegalArgumentException(componentType+" not supported");
                        }
                        Object[] oa = (Object[]) value;
                        for (Object o : oa)
                        {
                            jarr.put(o.toString());
                        }
                }
                json.put(field, jarr);
            }
            else
            {
                if (value instanceof Collection)
                {
                    Collection collection = (Collection) value;
                    json.put(field, collection);
                }
                else
                {
                    if (value instanceof Map)
                    {
                        Map map = (Map) value;
                        json.put(field, map);
                    }
                    else
                    {
                        json.put(field, value);
                    }
                }
            }
        }
    }
}
