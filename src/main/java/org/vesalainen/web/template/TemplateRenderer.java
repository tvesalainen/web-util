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
package org.vesalainen.web.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.WeakHashMap;
import org.vesalainen.bean.ExpressionParser;
import org.vesalainen.html.Renderer;
import org.vesalainen.util.ThreadSafeTemporary;

/**
 * A Renderer that loads it's content from jar. Loaded template string may have
 * ${property} expressions which are resolved using bean properties of this 
 * class instance.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class TemplateRenderer implements Renderer
{
    /**
     * Jar path where templates are stored
     */
    public static final String Path = "/org/vesalainen/web/template/";
    private static final int BufferSize = 4096;
    private static final Map<String,String> map = new WeakHashMap<>();
    private static ThreadSafeTemporary<byte[]> bufferStore = new ThreadSafeTemporary<>(()->{return new byte[BufferSize];});
    protected ExpressionParser parser;
    protected String path;
    protected String charset;
    /**
     * Creates renderer for Path + path using utf-8.
     * @param path 
     */
    public TemplateRenderer(String path)
    {
        this(path, StandardCharsets.UTF_8);
    }
    /**
     * Creates renderer for Path + path using given charset.
     * @param path
     * @param charset 
     */
    public TemplateRenderer(String path, Charset charset)
    {
        this.path = Path + path;
        this.charset = charset.name();
        this.parser = new ExpressionParser(this);
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        String template = map.get(path);
        if (template == null)
        {
            InputStream is = TemplateRenderer.class.getResourceAsStream(path);
            if (is == null)
            {
                throw new IllegalArgumentException(path+" not found");
            }
            byte[] buf = bufferStore.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int rc = is.read(buf);
            while (rc != -1)
            {
                baos.write(buf, 0, rc);
                rc = is.read(buf);
            }
            template = baos.toString(charset);
            map.put(path, template);
        }
        parser.replace(template, out);
    }
    
}
