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
package org.vesalainen.web.servlet.sse;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.vesalainen.web.servlet.sse.AbstractSSESource.SSEObserver;

/**
 *
 * @author tkv
 */
public class SSEServlet extends AbstractBaseSSEServlet
{
    public static final String Path = "/sse";
    protected long asyncTimeout = -1;
    
    public SSEServlet(AbstractSSESource source)
    {
        super(source);
    }

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        SSEOMap sseoMap = new SSEOMap();
        servletContext.setAttribute(SSEOMapName, sseoMap);
        //servletContext.addListener(sseoMap);
        log("test");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log(req.toString());
        HttpSession session = req.getSession(true);
        ServletContext servletContext = getServletContext();
        SSEOMap sseoMap = (SSEOMap) servletContext.getAttribute(SSEOMapName);
        SSEObserver sseo = null;
        synchronized(sseoMap)
        {
            sseo = sseoMap.get(session);
            if (sseo == null)
            {
                sseo = source.register();
                sseoMap.put(session, sseo);
                log("registered sseo");
            }
        }
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.flushBuffer();
        log("async started");
        AsyncContext startAsync = req.startAsync();
        startAsync.setTimeout(asyncTimeout);
        sseo.start(startAsync);
    }

    public void setAsyncTimeout(long asyncTimeout)
    {
        this.asyncTimeout = asyncTimeout;
    }
    
}
