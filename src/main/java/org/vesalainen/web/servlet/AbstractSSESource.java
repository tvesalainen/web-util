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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.vesalainen.html.Element;
import org.vesalainen.html.Frameworks;
import org.vesalainen.html.Page;
import org.vesalainen.html.ScriptElement;
import org.vesalainen.html.jquery.DocumentReadyEvent;
import org.vesalainen.html.jquery.SelectorFunction;
import org.vesalainen.js.Script;
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
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    protected ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    protected ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

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
        ScriptElement script = new ScriptElement(createScript());
        head.addContent(script);
    }

    private Script createScript()
    {
        DocumentReadyEvent ready = new DocumentReadyEvent();
        ready.addCode("var events;");
        SelectorFunction sf = new SelectorFunction(".server_sent_event", "each");
        sf.addCode("if (events) events=events+','+$(this).attr('id'); else events=$(this).attr('id');");
        ready.addScript(sf);
        ready.addCode("localStorage.org_vesalainen_html_events=events;");
        ready.addCode("if (events) {");
        ready.addCode("var url = '");
        ready.addCode(urlPattern);
        ready.addCode("'+'?events='+events;");
        ready.addCode("var eventSource = new EventSource(url);");
        ready.addScript(new Events());
        ready.addCode("}");
        return ready;
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
        List<SSEObserver> trash = null;
        
        readLock.lock();
        try
        {
            for (SSEObserver sseo : eventMap.get(event))
            {
                if (!sseo.fireEvent(event, data))
                {
                    if (trash == null)
                    {
                        trash = new ArrayList<>();
                    }
                    trash.add(sseo);
                }
            }
        }
        finally
        {
            readLock.unlock();
        }
        if (trash != null)
        {
            for (SSEObserver sseo : trash)
            {
                removeObserver((SSEObserverImpl) sseo);
            }
            trash = null;
        }
    }
    private void addObserver(SSEObserverImpl sseo)
    {
        System.err.println("addObserver");
        writeLock.lock();
        try
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
        finally
        {
            writeLock.unlock();
        }
    }
    
    private void removeObserver(SSEObserverImpl sseo)
    {
        System.err.println("removeObserver");
        writeLock.lock();
        try
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
        finally
        {
            writeLock.unlock();
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
        public boolean fireEvent(String event, String data)
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
                return true;
            }
            catch (Exception ex)
            {
                semaphore.release();
                return false;
            }
        }

    }
    public class Events implements Script
    {

        @Override
        public void append(Appendable out) throws IOException
        {
            for (String ev : allEvents)
            {
                out.append("eventSource.addEventListener('");
                out.append(ev);
                out.append("', function(event) {document.getElementById('");
                out.append(ev);
                out.append("').innerHTML = event.data;}, false);");
            }
        }
        
    }
}
