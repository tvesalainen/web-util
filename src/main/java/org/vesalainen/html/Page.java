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
package org.vesalainen.html;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tkv
 */
public class Page 
{
    protected Tag html;
    protected Tag head;
    protected Tag body;

    public Page()
    {
        html = new Tag("html");
        head = html.addTag("head");
        body = html.addTag("body");
    }

    public Tag getHtml()
    {
        return html;
    }

    public Tag getHead()
    {
        return head;
    }

    public Tag getBody()
    {
        return body;
    }

    public void write(Writer writer) throws IOException
    {
        writer.append("<!DOCTYPE HTML>\n");
        html.append(writer);
    }
    
    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE HTML>\n");
            html.append(sb);
            return sb.toString();
        }
        catch (IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
}
