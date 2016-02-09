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
import java.util.Map.Entry;
import java.util.Set;
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
                    col.add(fromJSONObject(itemType, o));
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
                            mapCollection.add(fromJSONObject(itemType, key), fromJSONObject(itemType2, o));
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
                            if (jj.isNull(key))
                            {
                                map.put(fromJSONObject(itemType, key), null);
                            }
                            else
                            {
                                map.put(fromJSONObject(itemType, key), fromJSONObject(itemType2, o));
                            }
                        }
                    }
                    else
                    {
                        Class type = BeanHelper.getType(base, field);
                        if (JSONBean.class.isAssignableFrom(type))
                        {
                            BeanHelper.setFieldValue(base, field, fromJSONObject(JSONBean.class, jo));
                        }
                        else
                        {
                            BeanHelper.setFieldValue(base, field, jo);
                        }
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
        json.put(field, toJSONObject(value));
    }
    private static Object fromJSONObject(Class<?> expected, Object value)
    {
        if (JSONObject.NULL.equals(value))
        {
            return null;
        }
        if (JSONBean.class.isAssignableFrom(expected))
        {
            JSONObject jo = (JSONObject) value;
            String classname = jo.getString("class");
            if (classname == null)
            {
                throw new IllegalArgumentException("class missing in "+jo);
            }
            try
            {
                Class<?> cls = Class.forName(classname);
                JSONBean jsonBean = (JSONBean) cls.newInstance();
                jo.remove("class");
                setValues(jo, jsonBean);
                return jsonBean;
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
        return ConvertUtility.convert(expected, value);
    }
    
    private static Object toJSONObject(Object value)
    {
        if (value == null)
        {
            return JSONObject.NULL;
        }
        Class<?> type = value.getClass();
        if (isBoolean(type))
        {
            return ConvertUtility.convert(Boolean.class, value);
        }
        if (isDouble(type))
        {
            return ConvertUtility.convert(Double.class, value);
        }
        if (isInteger(type))
        {
            return ConvertUtility.convert(Integer.class, value);
        }
        if (isLong(type))
        {
            return ConvertUtility.convert(Long.class, value);
        }
        if (value instanceof String)
        {
            return value;
        }
        if (value instanceof Collection)
        {
            Collection collection = (Collection) value;
            JSONArray jarr = new JSONArray();
            for (Object obj : collection)
            {
                jarr.put(toJSONObject(obj));
            }
            return jarr;
        }
        if (value instanceof Map)
        {
            Map map = (Map) value;
            JSONObject jo = new JSONObject();
            Set<Entry> entrySet = map.entrySet();
            for (Entry<Object,Object> e : entrySet)
            {
                jo.put(toJSONKey(e.getKey()), toJSONObject(e.getValue()));
            }
            return jo;
        }
        if (value.getClass().isArray())
        {
            JSONArray jarr = new JSONArray();
            Class componentType = value.getClass().getComponentType();
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
                        jarr.put(toJSONObject(o));
                    }
            }
            return jarr;
        }
        if (value instanceof JSONBean)
        {
            JSONObject jo = new JSONObject();
            Class<?> cls = value.getClass();
            jo.put("class", cls.getName());
            for (String field : BeanHelper.getFields(cls))
            {
                Object fieldValue = BeanHelper.getFieldValue(value, field);
                jo.put(field, toJSONObject(fieldValue));
            }
            return jo;
        }
        return value.toString();
    }
    private static String toJSONKey(Object value)
    {
        return ConvertUtility.convert(String.class, value);
    }
    private static boolean isBoolean(Class<?> type)
    {
        return (
                boolean.class.equals(type) ||
                Boolean.class.equals(type)
                );
    }
    private static boolean isDouble(Class<?> type)
    {
        return (
                double.class.equals(type) ||
                Double.class.equals(type) ||
                float.class.equals(type) ||
                Float.class.equals(type)
                );
    }
    private static boolean isInteger(Class<?> type)
    {
        return (
                int.class.equals(type) ||
                Integer.class.equals(type) ||
                short.class.equals(type) ||
                Short.class.equals(type) ||
                char.class.equals(type) ||
                Character.class.equals(type)
                );
    }
    private static boolean isLong(Class<?> type)
    {
        return (
                long.class.equals(type) ||
                Long.class.equals(type)
                );
    }
}
