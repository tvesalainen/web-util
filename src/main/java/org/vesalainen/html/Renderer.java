/*
 * Copyright (C) 2016 Timo Vesalainen <timo.vesalainen@iki.fi>
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
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.vesalainen.util.concurrent.TryAdvanceSpliterator;

/**
 *
 * @author Timo Vesalainen <timo.vesalainen@iki.fi>
 */
public interface Renderer
{
    void append(Appendable out) throws IOException;
    default void visit(Consumer<? super Renderer> consumer)
    {
        consumer.accept(this);
    }
    default Stream<Renderer> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }
    default Spliterator<Renderer> spliterator()
    {
        return new TryAdvanceSpliterator<>(new SpliteratorImpl(this), 5, TimeUnit.SECONDS);
    }
    public static class SpliteratorImpl implements Spliterator<Renderer>
    {
        private Renderer renderer;

        public SpliteratorImpl(Renderer renderer)
        {
            this.renderer = renderer;
        }

        @Override
        public void forEachRemaining(Consumer<? super Renderer> action)
        {
            renderer.visit(action);
        }
        
        @Override
        public boolean tryAdvance(Consumer<? super Renderer> action)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Spliterator<Renderer> trySplit()
        {
            return null;
        }

        @Override
        public long estimateSize()
        {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics()
        {
            return 0;
        }
        
    }
}
