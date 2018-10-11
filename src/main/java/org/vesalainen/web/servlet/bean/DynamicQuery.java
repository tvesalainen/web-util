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
package org.vesalainen.web.servlet.bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Content;
import org.vesalainen.html.Renderer;
import org.vesalainen.util.CollectionHelp;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T>
 */
public class DynamicQuery<T> implements Renderer
{   
    private ThreadLocal<T> local;
    private Charset charset;
    private Collection<String> fields;
    
    public DynamicQuery(ThreadLocal<T> local, Charset charset, String... fields)
    {
        this(local, charset, CollectionHelp.create(fields));
    }

    public DynamicQuery(ThreadLocal<T> local, Charset charset, Collection<String> fields)
    {
        this.local = local;
        this.charset = charset;
        this.fields = fields;
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        T data = local.get();
        boolean delim = false;
        for (String field : fields)
        {
            Object value = BeanHelper.getValue(data, field);
            if (value == null)
            {
                add(out, delim, field, null);
                delim = true;
            }
            else
            {
                if (value instanceof Collection)
                {
                    Collection col = (Collection) value;
                    if (col.isEmpty())
                    {
                        add(out, delim, field, null);
                        delim = true;
                    }
                    else
                    {
                        for (Object ob : col)
                        {
                            add(out, delim, field, ob);
                            delim = true;
                        }
                    }
                }
                else
                {
                    add(out, delim, field, value);
                    delim = true;
                }
            }
        }
    }
    private void add(Appendable sb, boolean delim, String field, Object value) throws IOException
    {
        if (delim)
        {
            sb.append("&");
        }
        sb.append(field);
        sb.append('=');
        if (value != null)
        {
            try
            {
                sb.append(URLEncoder.encode(value.toString(), charset.name()));
            }
            catch (UnsupportedEncodingException ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }
    }
}
