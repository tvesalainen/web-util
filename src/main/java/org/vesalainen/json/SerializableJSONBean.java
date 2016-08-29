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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.json.JSONObject;
import org.vesalainen.bean.BeanHelper;

/**
 * A class that implements it's subclasses serialization by json
 * @author tkv
 */
public class SerializableJSONBean implements JSONBean, Serializable
{

    static final long serialVersionUID = 1L;

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        JSONObject jo = new JSONObject();
        Class<?> cls = this.getClass();
        for (String field : BeanHelper.getProperties(cls))
        {
            Object fieldValue = BeanHelper.getValue(this, field);
            jo.put(field, JsonHelper.toJSONObject(fieldValue));
        }
        out.writeUTF(jo.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        String str = in.readUTF();
        JSONObject jo = new JSONObject(str);
        JsonHelper.setValues(jo, this);
    }

}
