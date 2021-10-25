/*
 * Copyright (C) 2021 Timo Vesalainen <timo.vesalainen@iki.fi>
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
import java.util.function.Function;

/**
 * Subclass must implements abstract append method which must call typed append.
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @see org.vesalainen.html.BoundAppendable#append(java.lang.Appendable, java.lang.Object) 
 * @see org.vesalainen.html.AbstractDynamicInput#append(java.lang.Appendable, java.lang.Class, java.lang.Object) 
 * @param <T>
 */
public abstract class AbstractDynamicInput<T> extends Tag implements BoundAppendable<T>
{
    
    public AbstractDynamicInput()
    {
        super("input");
    }

    public void append(Appendable out, T t, Class<?> type, Object value) throws IOException
    {
        out.append('<');
        out.append(name);
        appendType(out, type);
        appendValue(out, type, value);
        if (attributes != null)
        {
            for (Attribute<?> attr : attributes.values())
            {
                out.append(' ');
                if (attr instanceof BoundAppendable)
                {
                    BoundAppendable<T> a = (BoundAppendable<T>) attr;
                    a.append(out, t);
                }
                else
                {
                    attr.append(out);
                }
            }
        }
        out.append('>');
    }
    public <A> Tag setAttr(String name, Function<T,A> value)
    {
        return setAttr(new FunctionAttribute<>(name, value));
    }

    public <A> Tag setDataAttr(String name, Function<T,A> value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }
    
    private void appendType(Appendable out, Class<?> type) throws IOException
    {
        switch (type.getSimpleName())
        {
            case "Boolean":
                appendAttr(out, "type", "checkbox");
                break;
            case "Integer":
                appendAttr(out, "type", "number");
                appendAttr(out, "min", Integer.MIN_VALUE);
                appendAttr(out, "max", Integer.MAX_VALUE);
                appendAttr(out, "pattern", "[\\-\\+]?[0-9]+");
                break;
            case "Short":
                appendAttr(out, "type", "number");
                appendAttr(out, "min", Short.MIN_VALUE);
                appendAttr(out, "max", Short.MAX_VALUE);
                appendAttr(out, "pattern", "[\\-\\+]?[0-9]+");
                break;
            case "Long":
                appendAttr(out, "type", "number");
                appendAttr(out, "min", Long.MIN_VALUE);
                appendAttr(out, "max", Long.MAX_VALUE);
                appendAttr(out, "pattern", "[\\-\\+]?[0-9]+");
                break;
            case "Float":
                appendAttr(out, "type", "number");
                appendAttr(out, "min", Float.MIN_VALUE);
                appendAttr(out, "max", Float.MAX_VALUE);
                break;
            case "Double":
                appendAttr(out, "type", "number");
                appendAttr(out, "min", Double.MIN_VALUE);
                appendAttr(out, "max", Double.MAX_VALUE);
                break;
            case "String":
                appendAttr(out, "type", "text");
                break;
            default:
                throw new UnsupportedOperationException(type+" not supported");
        }
    }
    private void appendValue(Appendable out, Class<?> type, Object value) throws IOException
    {
        switch (type.getSimpleName())
        {
            case "Boolean":
                if (value != null && (Boolean)value)
                {
                    out.append(" checked");
                }
                break;
            case "Integer":
            case "Short":
            case "Long":
            case "Float":
            case "Double":
            case "String":
                appendAttr(out, "value", value!=null?value:"");
                break;
        }
    }

    private void appendAttr(Appendable out, String name, Object value) throws IOException
    {
        out.append(' ');
        out.append(name);
        out.append("=\"");
        out.append(value.toString());
        out.append('"');
    }

    
}
