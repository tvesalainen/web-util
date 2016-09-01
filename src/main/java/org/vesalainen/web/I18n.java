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
package org.vesalainen.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.vesalainen.html.Renderer;

/**
 *
 * @author tkv
 */
public class I18n
{
    public static final I18nSupport stupidI18n = new StupidI18n();
    public static final I18nSupport camelCaseI18n = new CamelCaseI18n();
    private static final ThreadLocal<Locale> locale = new ThreadLocal<>();
    private static final ThreadLocal<I18nSupport> support = new ThreadLocal<>();
    private static final Set<Object> missing = new HashSet<>();
    private static final Map<Object,LabelWrap> labelMap = new HashMap<>();
    private static final Map<Object,LabelWrap> placeholderMap = new HashMap<>();
    static
    {
        support.set(stupidI18n);
    }
    public static final void set(I18nSupport support, Locale locale)
    {
        I18n.locale.set(locale);
        I18n.support.set(support);
    }
    
    public static final Locale getLocale()
    {
        return locale.get();
    }
    
    public static final I18nSupport getI18n()
    {
        return support.get();
    }

    public static final Renderer getLabel(Serializable key)
    {
        LabelWrap wrap = labelMap.get(key);
        if (wrap == null)
        {
            wrap = new LabelWrap(key, false);
            labelMap.put(key, wrap);
        }
        return wrap;
    }

    public static final Renderer getPlaceholder(Serializable key)
    {
        LabelWrap wrap = placeholderMap.get(key);
        if (wrap == null)
        {
            wrap = new LabelWrap(key, true);
            placeholderMap.put(key, wrap);
        }
        return wrap;
    }

    public static final String getPlaceholder(Locale locale, Object key)
    {
        return support.get().getPlaceholder(locale, key);
    }

    private static void printMissing(Object key)
    {
        if (!missing.contains(key))
        {
            missing.add(key);
            System.err.println(key+" = ");
        }
    }
    public static class LabelWrap implements Renderer, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final Serializable key;
        private final boolean placeholder;

        public LabelWrap(Serializable key, boolean placeholder)
        {
            this.key = key;
            this.placeholder = placeholder;
        }
        
        
        @Override
        public void append(Appendable out) throws IOException
        {
            out.append(toString());
        }

        @Override
        public String toString()
        {
            String string;
            if (placeholder)
            {
                string = support.get().getPlaceholder(locale.get(), key);
            }
            else
            {
                string = support.get().getLabel(locale.get(), key);
            }
            if (string != null)
            {
                return string;
            }
            else
            {
                printMissing(key);
                return "?"+key+"?";
            }
        }
        
    }
}
