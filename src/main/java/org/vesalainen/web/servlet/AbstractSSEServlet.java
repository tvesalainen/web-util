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
import java.util.HashMap;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.vesalainen.web.servlet.AbstractSSESource.SSEObserver;

/**
 *
 * @author tkv
 */
public abstract class AbstractSSEServlet extends HttpServlet
{
    private static final String SSEOMapName = "org.vesalainen.web.servlet.sseoMap";
    protected AbstractSSESource source;
    private long asyncTimeout = -1;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        SSEOMap sseoMap = new SSEOMap();
        servletContext.setAttribute(SSEOMapName, sseoMap);
        servletContext.addListener(sseoMap);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log(req.toString());
        HttpSession session = req.getSession(true);
        ServletContext servletContext = getServletContext();
        SSEOMap sseoMap = (SSEOMap) servletContext.getAttribute(SSEOMapName);
        SSEObserver sseo = sseoMap.get(session);
        if (sseo == null)
        {
            sseo = source.register();
            sseoMap.put(session, sseo);
            log("registered sseo");
        }
        String[] events = req.getParameterValues("add");
        if (events != null)
        {
            for (String ev : events)
            {
                log("add "+ev);
                sseo.addEvent(ev);
            }
        }
        else
        {
            events = req.getParameterValues("remove");
            if (events != null)
            {
                for (String ev : events)
                {
                    log("remove "+ev);
                    sseo.removeEvent(ev);
                }
            }
            else
            {
                resp.setContentType("text/event-stream");
                resp.setCharacterEncoding("UTF-8");
                resp.addHeader("Connection", "close");
                resp.flushBuffer();
                log("async started");
                AsyncContext startAsync = req.startAsync();
                startAsync.setTimeout(asyncTimeout);
                sseo.start(startAsync);
            }
        }
    }

    public void setAsyncTimeout(long asyncTimeout)
    {
        this.asyncTimeout = asyncTimeout;
    }
    
    private class SSEOMap extends HashMap<HttpSession,SSEObserver> implements HttpSessionListener
    {

        @Override
        public void sessionCreated(HttpSessionEvent se)
        {
        }

        @Override
        public void sessionDestroyed(HttpSessionEvent se)
        {
            remove(se.getSession());
        }
        
    }
}
