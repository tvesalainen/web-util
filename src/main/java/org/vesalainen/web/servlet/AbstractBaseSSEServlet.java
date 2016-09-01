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

import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author tkv
 */
public abstract class AbstractBaseSSEServlet extends HttpServlet
{
    
    protected static final String SSEOMapName = "org.vesalainen.web.servlet.sseoMap";
    protected AbstractSSESource source;

    protected AbstractBaseSSEServlet(AbstractSSESource source)
    {
        this.source = source;
    }

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        SSEOMap sseoMap = new SSEOMap();
        servletContext.setAttribute(SSEOMapName, sseoMap);
        servletContext.addListener(sseoMap);
    }

    protected class SSEOMap extends HashMap<HttpSession,AbstractSSESource.SSEObserver> implements HttpSessionListener
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
