/*
 * Copyright (C) 2021 Timo Vesalainen <timo.vesalainen@iki.fi>
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
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vesalainen.util.MimeTypes;
import org.vesalainen.util.ThreadSafeTemporary;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class AbstractJarServlet extends HttpServlet
{
    
    protected static final int BUFFER_SIZE = 4096;
    protected static final String ETAG = "\"" + String.valueOf(System.currentTimeMillis()) + "\"";
    protected static ThreadSafeTemporary<byte[]> BUFFER_STORE = new ThreadSafeTemporary<>(() ->
    {
        return new byte[BUFFER_SIZE];
    });

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log(request.toString());
        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ETAG.equals(ifNoneMatch))
        {
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.setHeader("ETag", ETAG);
        String page = getPage(request);
        InputStream is = getClass().getResourceAsStream(page);
        if (is != null)
        {
            String mimeType = MimeTypes.getMimeType(page);
            response.setContentType(mimeType);
            response.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream os = response.getOutputStream();
            byte[] buf = BUFFER_STORE.get();
            int rc = is.read(buf);
            while (rc != -1)
            {
                os.write(buf, 0, rc);
                rc = is.read(buf);
            }
            os.flush();
        }
        else
        {
            System.err.println(page + " not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected abstract String getPage(HttpServletRequest request);
}
