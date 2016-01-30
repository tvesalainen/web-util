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
public class Text implements Content, Changeable
{
    private String text;

    public Text(String text)
    {
        this.text = encode(text);
    }
    /**
     * Changes HTML special characters to entity references
     * @param text
     * @return 
     */
    public static final String encode(String text)
    {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        for (int ii=0;ii<len;ii++)
        {
            char cc = text.charAt(ii);
            switch (cc)
            {
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(cc);
            }
        }
        return sb.toString();
    }
    @Override
    public void append(Appendable out) throws IOException
    {
        out.append(text);
    }

    @Override
    public void change(Object value)
    {
        this.text = encode(value.toString());
    }
    
}
