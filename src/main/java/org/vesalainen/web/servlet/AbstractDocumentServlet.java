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
package org.vesalainen.web.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.html.Checker;
import org.vesalainen.html.Document;
import org.vesalainen.html.PrettyPrinter;
import org.vesalainen.html.Renderer;
import org.vesalainen.test.DebugHelper;
import org.vesalainen.web.I18n;
import org.vesalainen.web.I18nSupport;

/**
 *
 * @author tkv
 * @param <D> Document type
 */
public abstract class AbstractDocumentServlet<D extends Document> extends HttpServlet
{
    protected D document;
    protected String title;
    protected I18nSupport i18nSupport = I18n.getI18n();
    
    public AbstractDocumentServlet()
    {
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        document = createDocument();
        document.setTitle(title);
        document.init();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        I18n.set(i18nSupport, req.getLocale());
        response(resp, document);
    }

    protected void response(HttpServletResponse resp, Renderer renderer) throws IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        resp.setStatus(HttpServletResponse.SC_OK);
        Writer writer = resp.getWriter();
        Appendable out = writer;
        if (DebugHelper.guessDebugging())
        {
            Checker.checkIds(renderer);
            out = new PrettyPrinter(out); 
        }
        renderer.append(out);
        writer.flush();
    }

    protected abstract D createDocument();

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
