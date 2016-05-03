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
import org.vesalainen.bean.BeanHelper;

/**
 *
 * @author tkv
 */
public class RendererSupport
{
    /**
     * Renders all bean members which implement Renderer interface
     * @param bean
     * @param out 
     */
    public static final void render(Object bean, Appendable out)
    {
        Prefix prefix = new Prefix();
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
                        prefix.setPrefix(pattern);
                        renderer.append(out);
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
