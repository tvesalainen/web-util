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
package org.vesalainen.web.servlet.jaxb;

import java.io.File;
import java.net.URL;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.junit.Test;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.nmea.jaxb.router.NmeaType;
import org.vesalainen.web.server.EmbeddedServer;
import org.vesalainen.web.server.EmbeddedServerT;

/**
 *
 * @author tkv
 */
public class AbstractJAXBServletT
{
    
    public AbstractJAXBServletT()
    {
    }

    @Test
    public void testSomeMethod()
    {
        try
        {
            URL url = AbstractJAXBServletT.class.getResource("/router4.xml");
            File store = new File(url.toURI());
            EmbeddedServer server = new EmbeddedServer();
            RouterConfigServlet rcs = new RouterConfigServlet("org.vesalainen.nmea.jaxb.router", store, "router", JQueryMobileDocument::new);
            server.addServlet(rcs, "/router");
            server.startAndWait();
        }
        catch (Exception ex)
        {
            Logger.getLogger(EmbeddedServerT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class RouterConfigServlet extends AbstractJAXBServlet<JQueryMobileDocument,JAXBElement<NmeaType>>
    {

        public RouterConfigServlet(String packageName, File storage, String action, BiFunction<ThreadLocal<JAXBElement<NmeaType>>,String,JQueryMobileDocument> documentFactory)
        {
            super(packageName, storage, action, documentFactory);
        }
        
    }
}
