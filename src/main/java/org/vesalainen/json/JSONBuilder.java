/*
 * Copyright (C) 2022 Timo Vesalainen <timo.vesalainen@iki.fi>
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
package org.vesalainen.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.vesalainen.util.IntReference;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public class JSONBuilder
{
    
    public static final Obj<?> object()
    {
        return new Obj(null);
    }
    public static final Array<?> array()
    {
        return new Array(null);
    }
    public static abstract class Element<P extends Element>
    {
        private P parent;

        public Element()
        {
        }

        public Element(P parent)
        {
            this.parent = parent;
        }
        
        public abstract void write(Appendable out) throws IOException;
        
        public P end()
        {
            return parent;
        }
    }
    public static abstract class Value extends Element
    {
    
    }
    public static class Member extends Element
    {
        private String key;
        private Element element;

        public Member(String key, Element element)
        {
            this.key = JSONObject.quote(key);
            this.element = element;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append(key);
            out.append(':');
            element.write(out);
        }
        
    }
    public static class Obj<P extends Element> extends Element<P>
    {
        private List<Member> members = new ArrayList<>();

        public Obj(P parent)
        {
            super(parent);
        }
        
        public Obj<Obj> object(String key)
        {
            Obj<Obj> object = new Obj<>(this);
            Member member = new Member(key, object);
            members.add(member);
            return object;
        }
        
        public Array<Obj> array(String key)
        {
            Array<Obj> array = new Array<>(this);
            Member member = new Member(key, array);
            members.add(member);
            return array;
        }
        
        public Obj numberArray(String key, Supplier<DoubleStream> stream)
        {
            NumberArray array = new NumberArray(stream);
            Member member = new Member(key, array);
            members.add(member);
            return this;
        }
        
        public Obj objectArray(String key, Supplier<Stream<Object>> stream)
        {
            ObjectArray array = new ObjectArray(stream);
            Member member = new Member(key, array);
            members.add(member);
            return this;
        }
        
        public Obj string(String key, Supplier<String> string)
        {
            Str str = new Str(string);
            Member member = new Member(key, str);
            members.add(member);
            return this;
        }
        
        public Obj number(String key, DoubleSupplier number)
        {
            Number n = new Number(number);
            Member member = new Member(key, n);
            members.add(member);
            return this;
        }
        
        public Obj bool(String key, BooleanSupplier b)
        {
            Bool n = new Bool(b);
            Member member = new Member(key, n);
            members.add(member);
            return this;
        }
        
        public Obj nul(String key)
        {
            Member member = new Member(key, new Nul());
            members.add(member);
            return this;
        }
        
        @Override
        public void write(Appendable out) throws IOException
        {
            out.append('{');
            boolean comma = false;
            int size = members.size();
            for (int ii=0;ii<size;ii++)
            {
                if (comma)
                {
                    out.append(',');
                }
                else
                {
                    comma = true;
                }
                Member m = members.get(ii);
                m.write(out);
            }
            out.append('}');
        }
        
    }
    public static class Array<P extends Element> extends Element<P>
    {
        private List<Element> elements = new ArrayList<>();

        public Array(P parent)
        {
            super(parent);
        }
        
        public Obj<Array> object()
        {
            Obj<Array> object = new Obj<>(this);
            elements.add(object);
            return object;
        }
        
        public Array<Array> array()
        {
            Array<Array> array = new Array<>(this);
            elements.add(array);
            return array;
        }
        
        public Array string(Supplier<String> string)
        {
            elements.add(new Str(string));
            return this;
        }
        
        public Array number(DoubleSupplier number)
        {
            elements.add(new Number(number));
            return this;
        }
        
        public Array bool(BooleanSupplier b)
        {
            elements.add(new Bool(b));
            return this;
        }
        
        public Array nul()
        {
            elements.add(new Nul());
            return this;
        }
        
        @Override
        public void write(Appendable out) throws IOException
        {
            out.append('[');
            boolean comma = false;
            int size = elements.size();
            for (int ii=0;ii<size;ii++)
            {
                if (comma)
                {
                    out.append(',');
                }
                else
                {
                    comma = true;
                }
                Element m = elements.get(ii);
                m.write(out);
            }
            out.append(']');
        }
        
    }
    private static class Str extends Value
    {
        private Supplier<String> string;

        public Str(Supplier<String> string)
        {
            this.string = string;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append(JSONObject.quote(string.get()));
        }
        
    }
    private static class Number extends Value
    {
        private DoubleSupplier number;

        public Number(DoubleSupplier number)
        {
            this.number = number;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append(JSONObject.doubleToString(number.getAsDouble()));
        }
        
    }
    private static class NumberArray extends Value
    {
        private Supplier<DoubleStream> numberStream;

        public NumberArray(Supplier<DoubleStream> numberStream)
        {
            this.numberStream = numberStream;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append('[');
            try
            {
                IntReference comma = new IntReference(0);
                numberStream.get().mapToObj((d)->JSONObject.doubleToString(d)).forEach((s)->
                {
                    try
                    {
                        if (comma.getValue() > 0)
                        {
                            out.append(',');
                        }
                        else
                        {
                            comma.setValue(1);
                        }
                        out.append(s);
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                });
            }
            catch (RuntimeException ex)
            {
                Throwable cause = ex.getCause();
                if (cause != null && (cause instanceof IOException))
                {
                    throw (IOException)cause;
                }
                else
                {
                    throw ex;
                }
            }
            out.append(']');
        }
        
    }
    private static class ObjectArray extends Value
    {
        private Supplier<Stream<Object>> objectStream;

        public ObjectArray(Supplier<Stream<Object>> objectStream)
        {
            this.objectStream = objectStream;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append('[');
            try
            {
                IntReference comma = new IntReference(0);
                objectStream.get().forEach((o)->
                {
                    try
                    {
                        if (comma.getValue() > 0)
                        {
                            out.append(',');
                        }
                        else
                        {
                            comma.setValue(1);
                        }
                        out.append(JSONObject.valueToString(o));
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                });
            }
            catch (RuntimeException ex)
            {
                Throwable cause = ex.getCause();
                if (cause != null && (cause instanceof IOException))
                {
                    throw (IOException)cause;
                }
                else
                {
                    throw ex;
                }
            }
            out.append(']');
        }
        
    }
    private static class Bool extends Value
    {
        private BooleanSupplier is;

        public Bool(BooleanSupplier is)
        {
            this.is = is;
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append(Boolean.toString(is.getAsBoolean()));
        }
        
    }
    private static class Nul extends Value
    {

        public Nul()
        {
        }

        @Override
        public void write(Appendable out) throws IOException
        {
            out.append("null");
        }
        
    }
}
    