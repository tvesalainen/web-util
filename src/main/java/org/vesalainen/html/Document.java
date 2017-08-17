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
package org.vesalainen.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.vesalainen.js.AbstractScriptContainer;
import org.vesalainen.js.ScriptContainer;
import org.vesalainen.test.DebugHelper;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class Document implements Page, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Element html;
    protected Element head;
    protected Element body;
    protected ScriptContainer script;
    protected Set<Framework> frameworks;
    private final SimpleAttribute<Charset> charset;
    protected Supplier<Form> formfactory;
    protected String title;

    public Document()
    {
        this(null);
    }
    public Document(String title)
    {
        this.title = title;
        html = new Element(null, "html");
        head = html.addElement("head");
        charset = new SimpleAttribute<>("charset", StandardCharsets.UTF_8);
        head.addTag("meta")
                .setAttr(charset);
        body = html.addElement("body");
    }

    public void init()
    {
        if (title != null)
        {
            addTitle(title);
        }
    }
    protected void addTitle(String title)
    {
        head.addElement("title")
                .addText(title);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public void addToHeader(Renderer content)
    {
        html.insert(content);
    }

    @Override
    public void addToFooter(Renderer content)
    {
        html.add(content);
    }
    
    @Override
    public Form addForm(String id, String method, Object action)
    {
        Form form = createForm(body, id, method, action);
        body.addContent(form);
        return form;
    }

    @Override
    public Form createForm(Content parent, String id, String method, Object action)
    {
        Form form = new Form(parent, id, method, action);
        return form;
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
    
    @Override
    public ScriptContainer getScriptContainer()
    {
        if (script == null)
        {
            Element se = head.addElement("script");
            script = new AbstractScriptContainer("", "");
            se.addRenderer(script);
        }
        return script;
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
    
    public final Element getHtml()
    {
        return html;
    }

    public final Element getHead()
    {
        return head;
    }

    public Element getBody()
    {
        return body;
    }

    public final Element getRawBody()
    {
        return body;
    }

    @Override
    public Element getContent()
    {
        return body;
    }

    public void write(OutputStream os) throws IOException
    {
        Charset cs = getCharset();
        if (StandardCharsets.UTF_8.contains(cs))
        {
            Writer writer = new OutputStreamWriter(os, cs);
            Appendable out = writer;
            if (DebugHelper.guessDebugging())
            {
                out = new PrettyPrinter(out); 
            }
            append(out);
            writer.flush();
        }
        else
        {
            EntityReferences.write(os, toString(), cs);
        }
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        out.append("<!DOCTYPE HTML>\n");
        html.append(out);
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

    @Override
    public void visit(Consumer<? super Renderer> consumer)
    {
        html.visit(consumer);
    }
    
}
