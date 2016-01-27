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
package org.vesalainen.web.server;

import org.vesalainen.web.servlet.JarServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author tkv
 */
public class EmbeddedServer
{
    private int port = 8080;
    private Server server;
    private ServletHandler handler;

    public EmbeddedServer()
    {
        init();
    }

    public EmbeddedServer(int port)
    {
        this.port = port;
        init();
    }

    private void init()
    {
        server = new Server(port);
        handler = new ServletHandler();
        server.setHandler(handler);
    }
    
    public void addServlet(Class<? extends HttpServlet> servlet, String mapping)
    {
        handler.addServletWithMapping(servlet, mapping);
    }
    
    public void start() throws Exception
    {
        handler.addServletWithMapping(JarServlet.class, "/*");
        server.start();
        server.join();
    }
    
}