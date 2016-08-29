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

import java.io.File;
import java.io.IOException;
import org.vesalainen.web.servlet.JarServlet;
import javax.servlet.http.HttpServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.JavaUtilLog;
import org.eclipse.jetty.util.log.Log;
import org.vesalainen.util.logging.JavaLogging;

/**
 *
 * @author tkv
 */
public class EmbeddedServer extends JavaLogging
{
    private int port = 8080;
    private Server server;
    private ServletHandler handler;
    private HashSessionManager sessionManager;

    public EmbeddedServer()
    {
        super(EmbeddedServer.class);
        init();
    }

    public EmbeddedServer(int port)
    {
        super(EmbeddedServer.class);
        this.port = port;
        init();
    }

    private void init()
    {
        JavaUtilLog javaUtilLog = new JavaUtilLog("org.vesalainen.web.server");
        Log.setLog(javaUtilLog);
        server = new Server(port);
        server.setStopAtShutdown(true);
        handler = new ServletHandler();
        //server.setHandler(handler);
        SessionHandler sessionHandler = new SessionHandler();
        sessionManager = new HashSessionManager();
        sessionManager.setIdleSavePeriod(60);
        sessionHandler.setSessionManager(sessionManager);
        sessionHandler.setHandler(handler);
        server.setHandler(sessionHandler);
        HashSessionIdManager sessionIdManager = new HashSessionIdManager();
        server.setSessionIdManager(sessionIdManager);
    }
    
    public void setSessionStoreDirectory(File dir) throws IOException
    {
        sessionManager.setStoreDirectory(dir);
        config("setSessionStoreDirectory(%s)", dir);
    }
    
    public void addServlet(HttpServlet servlet, String mapping)
    {
        ServletHolder servletHolder = new ServletHolder(servlet);
        handler.addServletWithMapping(servletHolder, mapping);
    }
    
    public void addServlet(Class<? extends HttpServlet> servlet, String mapping)
    {
        handler.addServletWithMapping(servlet, mapping);
    }
    
    public void start() throws Exception
    {
        handler.addServletWithMapping(JarServlet.class, "/*");
        server.start();
    }
    
    public void startAndWait() throws Exception
    {
        handler.addServletWithMapping(JarServlet.class, "/*");
        server.start();
        server.join();
    }
    
}
