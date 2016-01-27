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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Frameworks;
import org.vesalainen.html.Page;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;

/**
 *
 * @author tkv
 */
public abstract class AbstractSSESource
{
    private final MapList<String,SSEObserver> eventMap = new HashMapList<>();
    
    protected String urlPattern;
    protected Set<String> allEvents;
    protected Page page;

    protected AbstractSSESource(String urlPattern, String... allEvents)
    {
        this.urlPattern = urlPattern;
        this.allEvents = new TreeSet<>();
        for (String ev : allEvents)
        {
            this.allEvents.add(ev);
        }
        this.page = new Page();
        page.use(Frameworks.JQuery);
        Element head = page.getHead();
        head.addElement("script")
                .addContent(new Script());
    }

    public Page getPage()
    {
        return page;
    }
    
    public SSEObserver register(String events)
    {
        String[] evs = events.split(",");
        SSEObserver sseo = new SSEObserverImpl();
        for (String event : evs)
        {
            sseo.addEvent(event);
        }
        return sseo;
    }

    protected abstract void addEvent(String event);
    
    protected abstract void removeEvent(String event);
    
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
            List<SSEObserver> list = eventMap.get(ev);
            eventMap.add(ev, sseo);
            if (list.isEmpty())
            {
                addEvent(ev);
            }
        }
    }
    
    private void removeObserver(SSEObserverImpl sseo)
    {
        for (String ev : sseo.events)
        {
            if (eventMap.removeItem(ev, sseo))
            {
                List<SSEObserver> list = eventMap.get(ev);
                if (list.isEmpty())
                {
                    removeEvent(ev);
                }
            }
        }
    }
    
    public class SSEObserverImpl implements SSEObserver
    {
        private final Set<String> events = new HashSet<>();
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
    public class Script implements Content
    {

        @Override
        public void append(Appendable out) throws IOException
        {
            out.append("if (localStorage.org_vesalainen_html_events) {");
            out.append("var url = '");
            out.append(urlPattern);
            out.append("'+'?events='+localStorage.org_vesalainen_html_events;");
            out.append("var eventSource = new EventSource(url);");
            for (String ev : allEvents)
            {
                out.append("eventSource.addEventListener('");
                out.append(ev);
                out.append("', function(event) {document.getElementById('");
                out.append(ev);
                out.append("').innerHTML = event.data;}, false);");
            }
            out.append("}");
        }
        
    }
}
