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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.vesalainen.html.DataAttributeName;
import org.vesalainen.json.JsonHelper;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;

/**
 *
 * @author tkv
 */
public abstract class AbstractSSESource implements Runnable
{
    public static final String EventSink = DataAttributeName.name("sse-sink");
    public static final String EventCSS = "["+EventSink+"]";
    
    private final MapList<String,SSEObserver> eventMap = new HashMapList<>();
    
    protected String urlPattern;
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    protected ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    protected ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    protected ArrayBlockingQueue<SSEObserver> trash = new ArrayBlockingQueue<>(100);

    protected AbstractSSESource(String urlPattern)
    {
        this.urlPattern = urlPattern;
        Thread thread = new Thread(this, AbstractSSESource.class.getSimpleName());
        thread.start();
    }

    public SSEObserver register(String events)
    {
        String[] evs = events.split(",");
        SSEObserver sseo = new SSEObserver();
        for (String event : evs)
        {
            sseo.addEvent(event);
        }
        return sseo;
    }

    protected abstract void addEvent(String event);
    
    protected abstract void removeEvent(String event);

    /**
     * Sends JSONObject event. 'html' key sets target html, 'text' sets target
     * text. Other keys set named attributes
     * @param event
     * @param data 
     */
    public void fireEvent(String event, JSONObject data)
    {
        fireEvent(event, data.toString());
    }
    
    public void fireEvent(String event, CharSequence seq)
    {
        readLock.lock();
        try
        {
            for (SSEObserver sseo : eventMap.get(event))
            {
                if (!sseo.fireEvent(event, seq))
                {
                    trash.put(sseo);
                }
            }
        }
        catch (InterruptedException ex)
        {
            throw new IllegalArgumentException(ex);
        }        
        finally
        {
            readLock.unlock();
        }
    }
    private void addObserver(SSEObserver sseo)
    {
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
    
    private void removeObserver(SSEObserver sseo)
    {
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

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                SSEObserver sseo = trash.take();
                removeObserver(sseo);
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(AbstractSSESource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class SSEObserver
    {
        private final Set<String> events = new HashSet<>();
        private Writer writer;
        private final Semaphore semaphore = new Semaphore(0);
        private boolean done;
        private ReentrantLock lock = new ReentrantLock();
        
        public void addEvent(String event)
        {
            events.add(event);
        }

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

        public boolean fireEvent(String event, CharSequence seq)
        {
            lock.lock();
            try
            {
                if (!done)
                {
                    writer.write("event:");
                    writer.write(event);
                    writer.write("\n");
                    writer.write("data:");
                    writer.append(seq);
                    writer.write("\n\n");
                    writer.flush();
                }
                return true;
            }
            catch (Exception ex)
            {
                semaphore.release();
                done = true;
                return false;
            }
            finally
            {
                lock.unlock();
            }
        }

    }
}
