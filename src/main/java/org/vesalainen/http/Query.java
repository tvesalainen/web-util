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
package org.vesalainen.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.vesalainen.html.Content;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;

/**
 *
 * @author tkv
 */
public class Query extends HashMapList<String,String> implements Content
{
    public Query()
    {
    }
    
    public Query(HttpServletRequest req)
    {
        this(req.getQueryString());
    }
    
    public Query(String queryString)
    {
        if (queryString != null)
        {
            if (queryString.startsWith("?"))
            {
                throw new IllegalArgumentException(queryString+" illegal");
            }
            String[] param = queryString.split("&");
            for (String ps : param)
            {
                String[] pss = ps.split("=");
                if (pss.length != 2)
                {
                    throw new IllegalArgumentException(queryString+" illegal");
                }
                add(pss[0], pss[1]);
            }
        }
    }

    @Override
    public void add(String name, String value)
    {
        try
        {
            super.add(name, URLDecoder.decode(value, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    @Override
    public void append(Appendable out) throws IOException
    {
        boolean first = true;
        for (Entry<String,List<String>> e : entrySet())
        {
            for (String value : e.getValue())
            {
                if (!first)
                {
                    out.append('&');
                }
                first = false;
                out.append(e.getKey());
                out.append('=');
                out.append(URLEncoder.encode(value, "UTF-8"));
            }
        }
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
}
