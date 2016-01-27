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
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tkv
 */
public class JarServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException
    {
        String pathInfo = request.getPathInfo();
        System.err.println(pathInfo);
        InputStream is = JarServlet.class.getResourceAsStream(pathInfo);
        if (is != null)
        {
            String mimeType = getServletContext().getMimeType(pathInfo);
            System.err.println(mimeType);
            response.setContentType(mimeType);
            response.setStatus(HttpServletResponse.SC_OK);
            ServletOutputStream os = response.getOutputStream();
            byte[] buf = new byte[4096];    // TODO
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
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}