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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Builder builds nested Containers
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 * @param <T> Stream item
 * @param <U> Middle item
 * @param <C> Container
 */
public class Builder<T,U,C extends Container>
{
    private final Function<T,Stream<U>> mapper;
    private final Function<T, C> factory;
    private final List<Builder<T,U,C>> list = new ArrayList<>();
    /**
     * Creates root builder with factory making Container from T
     * @param factory 
     */
    public Builder(Function<T,C> factory)
    {
        this(null, factory);
    }

    private Builder(Function<T, Stream<U>> mapper, Function<T, C> factory)
    {
        this.mapper = mapper;
        this.factory = factory;
    }
    /**
     * Maps T to Collection and a factory making Container from U.
     * Mapping is possible from member of T.
     * @param <T>
     * @param <U>
     * @param <R>
     * @param mapper
     * @param factory
     * @return 
     */
    public <T,U,R extends Container> Builder<U,?,R> mapCollection(Function<T,Collection<U>> mapper, Function<U,R> factory)
    {
        return mapStream((T t)->mapper.apply(t).stream(), factory);
    }
    /**
     * Maps T to array U and a factory making Container from U.
     * Mapping is possible from member of T.
     * @param <T>
     * @param <U>
     * @param <R>
     * @param mapper
     * @param factory
     * @return 
     */
    public <T,U,R extends Container> Builder<U,?,R> mapArray(Function<T,U[]> mapper, Function<U,R> factory)
    {
        return mapStream((T t)->Stream.of(mapper.apply(t)), factory);
    }
    /**
     * Maps T to Stream and a factory making Container from U.
     * Mapping is possible from member of T.
     * @param <T>
     * @param <U>
     * @param <R>
     * @param mapper
     * @param factory
     * @return 
     */
    public <T,U,R extends Container> Builder<U,?,R> mapStream(Function<T,Stream<U>> mapper, Function<U,R> factory)
    {
        Builder<U,?,R> builder = new Builder(mapper, factory);
        list.add((Builder) builder);
        return builder;
    }
    /**
     * Builds nested Container from array
     * @param container
     * @param c 
     */
    public void build(Container container, T... c)
    {
        build(container, Stream.of(c));
    }
    /**
     * Builds nested Container from Collection
     * @param container
     * @param c 
     */
    public void build(Container container, Collection<T> c)
    {
        build(container, c.stream());
    }
    /**
     * Builds nested Container from Stream<T>
     * @param container
     * @param c 
     */
    public void build(Container container, Stream<T> c)
    {
        c.forEach((t)->
        {
            C r = factory.apply(t);
            container.add(r);
            list.forEach((b)->b.build(r, (Stream<T>) b.mapper.apply(t)));
        });
    }
    
}
