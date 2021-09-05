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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.vesalainen.util.CollectionHelp;

/**
 * DynamicElement builds nested Containers
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T> Stream item
 */
public final class DynamicElement<T> implements Renderer
{
    private static final long serialVersionUID = 1L;
    private Supplier<Stream<T>> streamSupplier;
    private Function<?,Stream<T>> mapper;
    protected String name;
    protected Map<String,Attribute<?>> attributes;
    protected ClassAttr classes;
    protected Function<T,String> textSupplier;
    private final List<DynamicElement<T>> content = new ArrayList<>();

    /**
     * Creates root builder with tag
     * @param streamSupplier
     * @param name 
     */
    private DynamicElement(String name, Supplier<Stream<T>> streamSupplier)
    {
        this.streamSupplier = streamSupplier;
        this.name = name;
    }

    private <U> DynamicElement(String name, Function<U, Stream<T>> mapper)
    {
        this.mapper = mapper;
        this.name = name;
    }
    public static <U> DynamicElement<U> getFrom(String name, Supplier<Stream<U>> streamSupplier)
    {
        return new DynamicElement<>(name, streamSupplier);
    }
    public static <U> DynamicElement<U> getFromCollection(String name, Supplier<Collection<U>> c)
    {
        return new DynamicElement<>(name, ()->c.get().stream());
    }
    public static <U> DynamicElement getFromArray(String name, U... items)
    {
        return getFromArray(name, ()->items);
    }
    public static <U> DynamicElement getFromArray(String name, Supplier<U[]> items)
    {
        return new DynamicElement<>(name, ()->Stream.of(items.get()));
    }
    
    /**
     * Maps T to Collection and a factory making Container from U.Mapping is possible from member of T.
     * @param <U>
     * @param mapper
     * @param name
     * @return 
     */
    public <U> DynamicElement<T> mapCollection(String name, Function<U,Collection<T>> mapper)
    {
        return mapStream(name, (U u)->mapper.apply(u).stream());
    }
    /**
     * Maps T to array U and a factory making Container from U.Mapping is possible from member of T.
     * @param <U>
     * @param mapper
     * @param name
     * @return 
     */
    public <U> DynamicElement<T> mapArray(String name, Function<U,T[]> mapper)
    {
        return mapStream(name, (U u)->Stream.of(mapper.apply(u)));
    }
    /**
     * Maps T to Stream and a factory making Container from U.Mapping is possible from member of T.
     * @param <U>
     * @param mapper
     * @param name
     * @return 
     */
    public <U> DynamicElement<T> mapStream(String name, Function<U,Stream<T>> mapper)
    {
        DynamicElement<T> builder = new DynamicElement(name, mapper);
        content.add((DynamicElement) builder);
        return builder;
    }
    @Override
    public void append(Appendable out) throws IOException
    {
        Stream<T> c = streamSupplier.get();
        c.forEach((t)->
        {
            try
            {
                out.append('<');
                out.append(name);
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
                if (textSupplier != null)
                {
                    out.append(textSupplier.apply(t));
                }
                content.forEach((b)->
                {
                    try
                    {
                        b.append(out);
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                });
                out.append("</");
                out.append(name);
                out.append('>');
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });
    }
    
    public DynamicElement<T> addClasses(String... cls)
    {
        for (String s : cls)
        {
            addClasses((t)->s);
        }
        return this;
    }
    /**
     * Add entr(y/ies) to clas attribute
     * @param cls
     * @return this
     */
    public DynamicElement<T> addClasses(Function<T,String> ... cls)
    {
        if (cls.length > 0)
        {
            if (classes == null)
            {
                classes = new ClassAttr(cls);
                setAttr(classes);
            }
            else
            {
                classes.addClasses(cls);
            }
        }
        return this;
    }
    public DynamicElement<T> setText(Function<T,String> textSupplier)
    {
        this.textSupplier = textSupplier;
        return this;
    }
    /**
     * Set SimpleAttribute
     * @param <A>
     * @param name
     * @param value
     * @return this
     */
    public <A> DynamicElement<T> setAttr(String name, A value)
    {
        return setAttr(name, (T t)->value);
    }
    /**
     * Set functional attribute.
     * @param <A>
     * @param name
     * @param value
     * @return 
     */
    public <A> DynamicElement<T> setAttr(String name, Function<T,A> value)
    {
        return setAttr(new FunctionAttribute<>(name, value));
    }

    public <A> DynamicElement<T> setDataAttr(String name, A value)
    {
        return setDataAttr(name, (T t)->value);
    }

    public <A> DynamicElement<T> setDataAttr(String name, Function<T,A> value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }
    
    /**
     * Set Attribute
     * @param <A>
     * @param attr
     * @return this
     */
    public <A> DynamicElement<T> setAttr(Attribute<A> attr)
    {
        if (attributes == null)
        {
            attributes = new TreeMap<>();
        }
        attributes.put(attr.getName(), attr);
        return this;
    }

    private class ClassAttr implements Attribute, BoundAppendable<T>
    {
        List<Function<T, String>> list = new ArrayList<>();
        
        public ClassAttr(Function<T, String>... cls)
        {
            addClasses(cls);
        }

        private void addClasses(Function<T, String>... cls)
        {
            CollectionHelp.addAll(list, cls);
        }

        @Override
        public void append(Appendable out, T t) throws IOException
        {
            out.append("class=");
            out.append(list.stream().map((f)->f.apply(t)).collect(Collectors.joining(" ", "\"", "\"")));
        }

        @Override
        public String getName()
        {
            return "class";
        }

        @Override
        public Object getValue()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void append(Appendable out) throws IOException
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
