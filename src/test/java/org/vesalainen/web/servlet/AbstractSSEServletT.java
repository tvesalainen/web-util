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

import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.web.server.EmbeddedServer;
import org.vesalainen.web.server.EmbeddedServerT;

/**
 *
 * @author tkv
 */
public class AbstractSSEServletT
{
    
    public AbstractSSEServletT()
    {
    }

    @Test
    public void test1()
    {
        try
        {
            EmbeddedServer server = new EmbeddedServer();
            server.addServlet(SSEServlet.class, "/sse");
            server.start();
        }
        catch (Exception ex)
        {
            Logger.getLogger(EmbeddedServerT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class SSEServlet extends AbstractSSEServlet
    {

        public SSEServlet()
        {
            source = new SSESource("/sse");
            source.getPage()
                    .getBody()
                    .addElement("div")
                    .addAttr("id", "ev1");
        }
        
    }
}