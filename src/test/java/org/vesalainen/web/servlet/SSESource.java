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
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author tkv
 */
public class SSESource extends AbstractSSESource
{
    private Timer timer = new Timer();
    
    public SSESource(String urlPattern)
    {
        super(urlPattern, "ev1", "ev2");
    }

    @Override
    protected void addEvent(String event)
    {
        System.err.println("addEvent(" + event + ")");
        TimerTask tt = new TimerTask() {

            @Override
            public void run()
            {
                fireEvent("ev1", new Date().toString());
            }
        };
        timer.schedule(tt, 1000, 1000);
    }

    @Override
    protected void removeEvent(String event)
    {
        System.err.println("removeEvent(" + event + ")");
        timer.cancel();
    }
    
}
