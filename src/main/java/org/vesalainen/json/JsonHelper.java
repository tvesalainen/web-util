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

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class JsonHelper
{
    /**
     * Clears to and puts values in from to to.
     * If to == null a new JSONObject is returned otherwise returns to
     * @param from
     * @param to 
     */
    public static JSONObject copy(JSONObject from, JSONObject to)
    {
        if (to == null)
        {
            to = new JSONObject();
        }
        else
        {
            to.keySet().clear();
        }
        for (String key : from.keySet())
        {
            to.put(key, from.get(key));
        }
        return to;
    }
    
    public static void setValues(JSONObject json, Object base)
    {
        for (String field : json.keySet())
        {
            setValue(json, base, field);
        }
    }
    
    public static void setValue(JSONObject json, Object base, String field)
    {
        Object value = BeanHelper.getValue(base, field);
        Object jo = json.get(field);
        if (JSONObject.NULL.equals(jo))
        {
            BeanHelper.setValue(base, field, null);
        }
        else
        {
            if (value instanceof Collection)
            {
                JSONArray ja = (JSONArray) jo;
                int length = ja.length();
                Collection col = (Collection) value;
                Class[] pt = BeanHelper.getParameterTypes(base, field);
                col.clear();
                for (int ii=0;ii<length;ii++)
                {
                    Object o = ja.get(ii);
                    col.add(fromJSONObject(pt[0], o));
                }
            }
            else
            {
                if (value instanceof MapCollection)
                {
                    MapCollection mapCollection = (MapCollection) value;
                    Class[] pt = BeanHelper.getParameterTypes(base, field);
                    mapCollection.clear();
                    JSONObject jj = (JSONObject) jo;
                    for (String key : jj.keySet())
                    {
                        JSONArray ja = (JSONArray) jj.get(key);
                        for (Object o : ja)
                        {
                            mapCollection.add(fromJSONObject(pt[0], key), fromJSONObject(pt[1], o));
                        }
                    }
                }
                else
                {
                    if (value instanceof Map)
                    {
                        Map map = (Map) value;
                        Class[] pt = BeanHelper.getParameterTypes(base, field);
                        map.clear();
                        JSONObject jj = (JSONObject) jo;
                        for (String key : jj.keySet())
                        {
                            Object o = jj.get(key);
                            if (jj.isNull(key))
                            {
                                map.put(fromJSONObject(pt[0], key), null);
                            }
                            else
                            {
                                map.put(fromJSONObject(pt[0], key), fromJSONObject(pt[1], o));
                            }
                        }
                    }
                    else
                    {
                        Class type = BeanHelper.getType(base, field);
                        if (JSONBean.class.isAssignableFrom(type))
                        {
                            BeanHelper.setValue(base, field, fromJSONObject(JSONBean.class, jo));
                        }
                        else
                        {
                            BeanHelper.setValue(base, field, jo);
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
        Object value = BeanHelper.getValue(base, field);
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
    
    public static Object toJSONObject(Object value)
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
            for (String field : BeanHelper.getProperties(cls))
            {
                Object fieldValue = BeanHelper.getValue(value, field);
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
