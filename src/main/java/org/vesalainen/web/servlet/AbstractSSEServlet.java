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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.vesalainen.web.servlet.AbstractSSESource.SSEObserver;

/**
 *
 * @author tkv
 */
public abstract class AbstractSSEServlet extends HttpServlet
{
    protected AbstractSSESource source;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        log(req.toString());
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(true);
        SSEObserver sseo = (SSEObserver) session.getAttribute("sseo");
        if (sseo == null)
        {
            sseo = source.register();
            session.setAttribute("sseo", sseo);
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
                resp.addHeader("Connection", "close");
                resp.flushBuffer();
                log("observe");
                sseo.observe(resp.getWriter());
            }
        }
        log(resp.toString());
    }
    
}
