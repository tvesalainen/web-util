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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.vesalainen.html.DataAttributeName;
import org.vesalainen.util.HashMapList;
import org.vesalainen.util.MapList;
import org.vesalainen.util.logging.JavaLogging;

/**
 *
 * @author tkv
 */
public abstract class AbstractSSESource extends JavaLogging implements Runnable
{
    public static final String EventSink = DataAttributeName.name("sse-sink");
    public static final String EventCSS = "["+EventSink+"]";
    
    private final MapList<String,SSEObserver> eventMap = new HashMapList<>();
    
    protected String urlPattern;
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    protected ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    protected ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    protected ArrayBlockingQueue<SSEObserver> trash = new ArrayBlockingQueue<>(100);
    private Thread thread;

    protected AbstractSSESource(String urlPattern)
    {
        super(AbstractSSESource.class);
        this.urlPattern = urlPattern;
    }

    public void start()
    {
        thread = new Thread(this, AbstractSSESource.class.getSimpleName());
        thread.start();
        config("started sse-source for %s", urlPattern);
    }
    public void stop()
    {
        thread.interrupt();
        thread = null;
        config("stopped sse-source for %s", urlPattern);
    }
    public SSEObserver register()
    {
        return new SSEObserver();
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
        finer("fire(%s, %s)", event, seq);
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
    protected void add(SSEObserver sseo, String event)
    {
        config("add event %s for sse-source %s", event, urlPattern);
        writeLock.lock();
        try
        {
            if (!eventMap.containsKey(event))
            {
                addEvent(event);    // first observer for event
            }
            eventMap.add(event, sseo);
        }
        finally
        {
            writeLock.unlock();
        }
    }
    
    protected void remove(SSEObserver sseo, String event)
    {
        config("remove event %s for sse-source %s", event, urlPattern);
        writeLock.lock();
        try
        {
            if (eventMap.removeItem(event, sseo))
            {
                List<SSEObserver> list = eventMap.get(event);
                if (list.isEmpty())
                {
                    removeEvent(event); // last observer for event quit
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
                for (String ev : sseo.events)
                {
                    remove(sseo, ev);
                }
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
            add(this, event);
        }

        public void removeEvent(String event)
        {
            events.remove(event);
            remove(this, event);
        }

        public void observe(Writer writer) throws IOException
        {
            try
            {
                this.writer = writer;
                warning("%s waiting", Thread.currentThread());
                semaphore.acquire();
                warning("%s released", Thread.currentThread());
            }
            catch (InterruptedException ex)
            {
                warning("%s interrupted", Thread.currentThread());
                throw new IOException(ex);
            }
        }

        private boolean fireEvent(String event, CharSequence seq)
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
