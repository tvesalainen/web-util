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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.servlet.http.HttpServletRequest;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;

/**
 *
 * @author tkv
 */
public class SSESource
{
    private static final SSESource source = new SSESource();

    private final MapList<String,SSEObserver> eventMap = new HashMapList<>();
    
    private SSESource()
    {
    }
    
    public static SSESource getSource()
    {
        return source;
    }
    
    public SSEObserver register(HttpServletRequest req)
    {
        SSEObserver sseo = new SSEObserverImpl();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String event = parameterNames.nextElement();
            sseo.addEvent(event);
        }
        return sseo;
    }
    
    public void fireEvent(String event, String data)
    {
        for (SSEObserver sseo : eventMap.get(event))
        {
            sseo.fireEvent(event, data);
        }
    }
    private void addObserver(SSEObserverImpl sseo)
    {
        for (String ev : sseo.events)
        {
            eventMap.add(ev, sseo);
        }
    }
    
    private void removeObserver(SSEObserverImpl sseo)
    {
        for (String ev : sseo.events)
        {
            eventMap.removeItem(ev, sseo);
        }
    }
    
    public class SSEObserverImpl implements SSEObserver
    {
        private final List<String> events = new ArrayList<>();
        private Writer writer;
        private Semaphore semaphore = new Semaphore(0);
        
        @Override
        public void addEvent(String event)
        {
            events.add(event);
        }

        @Override
        public void observe(Writer writer) throws IOException
        {
            try
            {
                addObserver(this);
                this.writer = writer;
                semaphore.acquire();
            }
            catch (InterruptedException ex)
            {
                throw new IOException(ex);
            }
        }

        @Override
        public void fireEvent(String event, String data)
        {
            try
            {
                writer.write("event:");
                writer.write(event);
                writer.write("\n");
                writer.write("data:");
                writer.write(data);
                writer.write("\n\n");
                writer.flush();
            }
            catch (IOException ex)
            {
                removeObserver(this);
                semaphore.release();
            }
        }
        
    }
}
