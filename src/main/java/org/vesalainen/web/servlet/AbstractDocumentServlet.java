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
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.html.Document;

/**
 *
 * @author tkv
 * @param <D>
 */
public abstract class AbstractDocumentServlet<D> extends HttpServlet
{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Document document = getDocument(null);
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
    protected abstract Document getDocument(D datagetPlaceholdergetPlaceholdergetLabelgetLabelgetLabel) throws IOException;
    
    protected String getLabel(String key) throws IOException
    {
        return getLabel(Locale.getDefault(), key);
    }
    protected String getLabel(Locale locale, String key) throws IOException
    {
        return "["+key+"]";
    }
    protected String getPlaceholder(String key) throws IOException
    {
        return getPlaceholder(Locale.getDefault(), key);
    }
    protected String getPlaceholder(Locale locale, String key) throws IOException
    {
        return "{"+key+"}";
    }
}
