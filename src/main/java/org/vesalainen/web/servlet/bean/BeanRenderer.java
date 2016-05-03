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
import java.util.Map;
import java.util.WeakHashMap;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.bean.ExpressionParser;
import org.vesalainen.html.Renderer;

/**
 *
 * @author tkv
 */
public abstract class BeanRenderer implements Renderer
{
    private static final Map<Class<? extends BeanRenderer>,CharSequence> cache = new WeakHashMap<>();
    private ExpressionParser parser;

    public BeanRenderer()
    {
        this.parser = new ExpressionParser(this);
    }
    
    protected abstract Renderer create();

    @Override
    public void append(Appendable out) throws IOException
    {
        CharSequence code = cache.get(this.getClass());
        if (code == null)
        {
            StringBuilder sb = new StringBuilder();
            create().append(sb);
            cache.put(this.getClass(), sb);
            code = sb;
        }
        parser.replace(code, out);
    }
    
}
