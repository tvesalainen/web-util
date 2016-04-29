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
 * along with this program.  If not, see http://www.gnu.org/licenses/"),  //.
 */
package org.vesalainen.html;

import java.io.IOException;

/**
 *
 * @author tkv
 */
public class PrettyPrinter implements Appendable
{
    private Appendable out;
    private int prefix = 0;
    private boolean start = false;
    private boolean slash = false;
    private boolean ender = false;

    public PrettyPrinter(Appendable out)
    {
        this.out = out;
    }
    
    private void prefix(Appendable out, int length) throws IOException
    {
        out.append('\n');
        for (int ii=0;ii<length;ii++)
        {
            out.append(' ');
        }
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException
    {
        return append(csq, 0, csq.length());
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException
    {
        for (int ii=start;ii<end;ii++)
        {
            append(csq.charAt(ii));
        }
        return this;
    }

    @Override
    public Appendable append(char cc) throws IOException
    {
        switch (cc)
        {
            case '<':
                start = true;
                break;
            case '/':
                if (start)
                {
                    prefix--;
                    prefix(out, prefix);
                    out.append("</");
                    start = false;
                    ender = true;
                }
                else
                {
                    slash = true;
                }
                break;
            case '>':
                if (slash)
                {
                    out.append("/>");
                    slash = false;
                }
                else
                {
                    if (!ender)
                    {
                        prefix++;
                    }
                    ender = false;
                    out.append('>');
                }
                break;
            case '\r':
            case '\n':
            case '\t':
                break;
            default:
                if (start)
                {
                    prefix(out, prefix);
                    out.append('<');
                    start = false;
                }
                slash = false;
                out.append((char)cc);
                break;
        }
        return this;
    }
}
