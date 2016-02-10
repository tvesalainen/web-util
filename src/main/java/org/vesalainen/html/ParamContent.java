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
 * Parameterized content
 * @author tkv
 * @param <P> DynContent parameter type
 * @param <K>
 */
public class ParamContent<P extends DynParam<K>,K> implements Content
{
    private final P param;
    private DynContent<P,K> content;

    public ParamContent()
    {
        this.param = null;
    }

    public ParamContent(P ctx)
    {
        this.param = ctx;
    }

    public P getParam()
    {
        return param;
    }

    public void setContent(DynContent<P,K> content)
    {
        this.content = content;
    }
    
    @Override
    public void append(Appendable out) throws IOException
    {
        if (content != null)
        {
            content.provision(param);
            content.append(param, out);
        }
    }
    
}
