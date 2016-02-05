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
package org.vesalainen.web.servlet.bean;

import java.util.Set;
import org.json.JSONObject;
import org.vesalainen.bean.BeanField;
import org.vesalainen.bean.ThreadLocalBeanField;
import org.vesalainen.json.JsonHelper;

/**
 *
 * @author tkv
 * @param <T>
 */
public class JSONInput<T> implements BeanField
{
    public static final String Fieldname = "__JSON__";
    private final ThreadLocal<T> local;
    private final Set<String> fields;
    
    public JSONInput(ThreadLocal<T> local, Set<String> fields)
    {
        this.local = local;
        this.fields = fields;
    }

    @Override
    public void set(Object value)
    {
        String[] arr = (String[]) value;
        JSONObject json = new JSONObject(arr[0]);
        T data = local.get();
        JsonHelper.setValues(json, data);
    }

    @Override
    public String toString()
    {
        T data = local.get();
        JSONObject json = new JSONObject();
        for (String field : fields)
        {
            JsonHelper.add(json, data, field);
        }
        return json.toString();
    }

    @Override
    public Object get()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
