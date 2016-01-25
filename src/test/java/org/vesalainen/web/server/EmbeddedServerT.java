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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.html.Page;
import org.vesalainen.html.bootstrap.Bootstrap;

/**
 *
 * @author tkv
 */
public class EmbeddedServerT
{

    public EmbeddedServerT()
    {
    }

    @Test
    public void test1()
    {
        try
        {
            EmbeddedServer server = new EmbeddedServer();
            server.addServlet(TestServlet.class, "/test");
            server.start();
        }
        catch (Exception ex)
        {
            Logger.getLogger(EmbeddedServerT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class TestServlet extends HttpServlet
    {
        private Page page;

        public TestServlet()
        {
            page = new Page("Test");
            page.use(new Bootstrap());
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            resp.setContentType("text/html");
            ServletOutputStream outputStream = resp.getOutputStream();
            page.write(outputStream);
            outputStream.flush();
        }
        
    }

}
