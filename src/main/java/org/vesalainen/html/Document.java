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
package org.vesalainen.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tkv
 */
public class Document 
{
    protected Element html;
    protected Element head;
    protected Element body;
    protected Set<Framework> frameworks;
    private final SimpleAttribute<Charset> charset;

    public Document()
    {
        this(null);
    }
    public Document(String title)
    {
        html = new Element("html");
        head = html.addElement("head");
        charset = new SimpleAttribute<>("charset", StandardCharsets.UTF_8);
        head.addTag("meta")
                .setAttr(charset);
        if (title != null)
        {
            head.addElement("title")
                    .addText(title);
        }
        body = html.addElement("body");
    }

    public void use(Framework framework)
    {
        if (frameworks == null)
        {
            frameworks = new HashSet<>();
        }
        frameworks.add(framework);
        for (Framework depends : framework.dependencies())
        {
            if (!frameworks.contains(depends))
            {
                use(depends);
            }
        }
        framework.useIn(this);
    }
    /**
     * Sets charset. Using charset other than default UTF-8 affects performance.
     * @param charset 
     */
    public void setCharset(Charset charset)
    {
        this.charset.setValue(charset);
    }
    
    public Charset getCharset()
    {
        return this.charset.getValue();
    }
    
    public Element getHtml()
    {
        return html;
    }

    public Element getHead()
    {
        return head;
    }

    public Element getBody()
    {
        return body;
    }

    public void write(OutputStream os) throws IOException
    {
        Charset cs = getCharset();
        if (StandardCharsets.UTF_8.contains(cs))
        {
            OutputStreamWriter writer = new OutputStreamWriter(os, cs);
            writer.append("<!DOCTYPE HTML>\n");
            html.append(writer);
            writer.flush();
        }
        else
        {
            EntityReferences.write(os, toString(), cs);
        }
    }
    
    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE HTML>\n");
            html.append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
}
