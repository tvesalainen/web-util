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
package org.vesalainen.web.servlet.bean;

import java.io.IOException;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.bean.ExpressionParser;
import org.vesalainen.html.Renderer;

/**
 *
 * @author tkv
 */
public class BeanRenderer
{
    public static final void render(Object bean, Appendable out)
    {
        Prefix prefix = new Prefix();
        StringBuilder sb = new StringBuilder();
        BeanHelper.stream(bean).forEach((String pattern)->
        {
            if (!prefix.skip(pattern))
            {
                try
                {
                    Object ob = BeanHelper.getValue(bean, pattern);
                    if (ob instanceof Renderer)
                    {
                        Renderer renderer = (Renderer) ob;
                        if (ob instanceof BeanContent)
                        {
                            prefix.setPrefix(pattern);
                            sb.setLength(0);
                            renderer.append(sb);
                            ExpressionParser parser = new ExpressionParser(ob);
                            parser.replace(sb, out);
                        }
                        else
                        {
                            renderer.append(out);
                        }
                    }
                }
                catch (IOException ex)
                {
                    throw new IllegalArgumentException(ex);
                }
            }
        });
    }
    private static class Prefix
    {
        String prefix;

        public void setPrefix(String prefix)
        {
            this.prefix = prefix;
        }
        
        boolean skip(String pattern)
        {
            return prefix != null ? pattern.startsWith(prefix) : false;
        }
    }
}
