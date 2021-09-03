/*
 * Copyright (C) 2016 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.html;

import java.net.URL;
import static org.vesalainen.web.servlet.JarServlet.PATH;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public abstract class AbstractFramework implements Framework
{
    private final Framework[] dependencies;
    protected String version;

    public AbstractFramework(Framework... dependencies)
    {
        this.dependencies = dependencies;
    }
    
    public AbstractFramework(String version, Framework... dependencies)
    {
        checkVersion(version);
        this.version = version;
        this.dependencies = dependencies;
    }
    
    @Override
    public Framework[] dependencies()
    {
        return dependencies;
    }
    
    protected abstract String path(String version);
    
    protected final void checkVersion(String version)
    {
        String path = path(version);
        URL resource = AbstractFramework.class.getResource(PATH + path(version));
        if (resource == null)
        {
            throw new IllegalArgumentException(PATH + path+" not found");
        }
    }
    
}
