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

/**
 *
 * @author tkv
 */
public final class Encoder
{
    /**
     * Changes HTML special characters to entity references
     * @param text
     * @return 
     * @throws java.io.IOException 
     */
    public static final String encode(String text) throws IOException
    {
        StringBuilder out = new StringBuilder();
        encode(out, text);
        return out.toString();
    }
    
    public static final void encode(Appendable out, String text) throws IOException
    {
        int len = text.length();
        for (int ii=0;ii<len;ii++)
        {
            char cc = text.charAt(ii);
            switch (cc)
            {
                case '"':
                    out.append("&quot;");
                    break;
                case '\'':
                    out.append("&apos;");
                    break;
                case '&':
                    out.append("&amp;");
                    break;
                case '<':
                    out.append("&lt;");
                    break;
                case '>':
                    out.append("&gt;");
                    break;
                default:
                    out.append(cc);
            }
        }
    }
}
