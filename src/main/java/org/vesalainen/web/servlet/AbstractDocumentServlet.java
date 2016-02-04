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

import org.vesalainen.web.I18n;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.html.Document;
import org.vesalainen.web.StupidI18n;

/**
 *
 * @author tkv
 * @param <D> Document type
 */
public abstract class AbstractDocumentServlet<D extends Document> extends HttpServlet
{
    protected D document;
    
    public AbstractDocumentServlet()
    {
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        document = createDocument();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        response(resp, document);
    }

    protected void response(HttpServletResponse resp, Document document) throws IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        ServletOutputStream os = resp.getOutputStream();
        document.write(os);
        os.flush();
    }

    protected abstract D createDocument();
}
