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
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;
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
public final class DynamicElement<T,U> implements AttributedContent, BoundAppendable<U>
{
    private static final long serialVersionUID = 1L;
    private Supplier<Stream<T>> streamSupplier;
    private Function<U,Stream<T>> mapper;
    protected String name;
    protected Map<String,Attribute<?>> attributes;
    protected ClassAttr classes;
    protected Function<T,String> textSupplier;
    private final List<BoundAppendable<T>> content = new ArrayList<>();
    private final List<BiConsumer<T,AttributedContent>> attributors = new ArrayList<>();
    private Content parent;

    /**
     * Creates root builder with tag
     * @param streamSupplier
     * @param tag 
     */
    private DynamicElement(String tag, Supplier<Stream<T>> streamSupplier)
    {
        Objects.requireNonNull(streamSupplier, "streamSupplier null");
        this.streamSupplier = streamSupplier;
        this.name = tag;
    }

    private DynamicElement(String tag, Function<U, Stream<T>> mapper)
    {
        Objects.requireNonNull(mapper, "mapper null");
        this.mapper = mapper;
        this.name = tag;
    }
    public static <T,U> DynamicElement<T,U> getFrom(String tag, Supplier<Stream<T>> streamSupplier)
    {
        return new DynamicElement<>(tag, streamSupplier);
    }
    public static <T,U> DynamicElement<T,U> getFromCollection(String tag, Supplier<Collection<T>> c)
    {
        return new DynamicElement<>(tag, ()->c.get().stream());
    }
    public static <T,U> DynamicElement getFromArray(String tag, T... items)
    {
        return getFromArray(tag, ()->items);
    }
    public static <T,U> DynamicElement getFromArray(String tag, Supplier<T[]> items)
    {
        return new DynamicElement<>(tag, ()->Stream.of(items.get()));
    }
    /**
     * Makes child element with identity mapping
     * @param tag
     * @return 
     */
    public DynamicElement<T,T> child(String tag)
    {
        return childFromStream(tag, (T t)->Stream.of(t));
    }
    /**
     * Makes child element mapping to collection
     * @param mapper
     * @param tag If tag is null only content is rendered
     * @return 
     */
    public <V> DynamicElement<V,T> childFromCollection(String tag, Function<T,Collection<V>> mapper)
    {
        return childFromStream(tag, (T t)->mapper.apply(t).stream());
    }
    /**
     * Makes child element mapping to array
     * @param mapper
     * @param tag If tag is null only content is rendered
     * @return 
     */
    public <V> DynamicElement<V,T> childFromArray(String tag, Function<T,V[]> mapper)
    {
        return childFromStream(tag, (T t)->Stream.of(mapper.apply(t)));
    }
    /**
     * Makes child element mapping to stream
     * @param mapper
     * @param tag If tag is null only content is rendered
     * @return 
     */
    public <V> DynamicElement<V,T> childFromStream(String tag, Function<T,Stream<V>> mapper)
    {
        DynamicElement<V,T> builder = new DynamicElement(tag, mapper);
        content.add((DynamicElement) builder);
        return builder;
    }
    /**
     * Adds child element that can be dynamic.
     * @param element
     * @return 
     */
    public DynamicElement<T, U> addContent(BoundAppendable<T> element)
    {
        content.add(element);
        return this;
    }
    @Override
    public void append(Appendable out) throws IOException
    {
        append(out, null);
    }
    @Override
    public void append(Appendable out, U u) throws IOException
    {
        Stream<T> c = getStream(u);
        c.forEach((t)->
        {
            try
            {
                if (t == null)
                {
                    throw new IllegalArgumentException("null not accepted");
                }
                attributors.forEach((a)->a.accept(t,this));
                if (name != null)
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
                }
                content.forEach((b)->
                {
                    try
                    {
                        b.append(out, t);
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                });
                if (name != null)
                {
                    out.append("</");
                    out.append(name);
                    out.append('>');
                }
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public Content getParent()
    {
        return parent;
    }

    @Override
    public void setParent(Content parent)
    {
        this.parent = parent;
    }

    @Override
    public String getName()
    {
        return name;
    }
    /**
     * Add consumer for manipulating attributes before rendering
     * @param attributor
     * @return 
     */
    public DynamicElement<T,U> attribute(BiConsumer<T,AttributedContent> attributor)
    {
        attributors.add(attributor);
        return this;
    }
    
    @Override
    public DynamicElement<T,U> addClasses(String... cls)
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
    public DynamicElement<T,U> addClasses(Function<T,String> ... cls)
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
    public DynamicElement<T,U> setText(Function<T,String> textSupplier)
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
    @Override
    public <A> DynamicElement<T,U> setAttr(String name, A value)
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
    public <A> DynamicElement<T,U> setAttr(String name, Function<T,A> value)
    {
        return setAttr(new FunctionAttribute<>(name, value));
    }

    @Override
    public <A> DynamicElement<T,U> setDataAttr(String name, A value)
    {
        return setDataAttr(name, (T t)->value);
    }

    public <A> DynamicElement<T,U> setDataAttr(String name, Function<T,A> value)
    {
        return setAttr(DataAttributeName.name(name), value);
    }
    
    /**
     * Set Attribute
     * @param <A>
     * @param attr
     * @return this
     */
    @Override
    public <A> DynamicElement<T,U> setAttr(Attribute<A> attr)
    {
        if (attributes == null)
        {
            attributes = new TreeMap<>();
        }
        attributes.put(attr.getName(), attr);
        return this;
    }

    @Override
    public Attribute<?> getAttr(String name)
    {
        return attributes.get(name);
    }

    @Override
    public AttributedContent removeAttr(String name)
    {
        attributes.remove(name);
        return this;
    }

    @Override
    public <A> AttributedContent setAttr(String name, Supplier<A> value)
    {
        return setAttr(name, (t)->value.get());
    }

    @Override
    public <A> AttributedContent setDataAttr(String name, Supplier<A> value)
    {
        return setDataAttr(name, (t)->value.get());
    }

    @Override
    public boolean hasAttr(String name)
    {
        return attributes.containsKey(name);
    }

    private Stream<T> getStream(U u)
    {
        if (u == null)
        {
            return streamSupplier.get();
        }
        else
        {
            return mapper.apply(u);
        }
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
