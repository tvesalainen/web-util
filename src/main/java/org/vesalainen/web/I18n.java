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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.vesalainen.html.Content;
import org.vesalainen.html.Renderer;

/**
 *
 * @author tkv
 */
public class I18n
{
    private static I18nSupport i18n = new StupidI18n();
    private static final ThreadLocal<Locale> locale = new ThreadLocal<>();
    private static final Set<Object> missing = new HashSet<>();
    private static final Map<Object,LabelWrap> labelMap = new HashMap<>();
    private static final Map<Object,LabelWrap> placeholderMap = new HashMap<>();

    public static final void setLocale(Locale locale)
    {
        I18n.locale.set(locale);
    }
    
    public static final Locale getLocale()
    {
        return locale.get();
    }
    
    public static final I18nSupport getI18n()
    {
        return i18n;
    }

    public static final void setI18n(I18nSupport i18n)
    {
        I18n.i18n = i18n;
    }

    public static final Renderer getLabel(Object key)
    {
        LabelWrap wrap = labelMap.get(key);
        if (wrap == null)
        {
            wrap = new LabelWrap(key, false);
            labelMap.put(key, wrap);
        }
        return wrap;
    }

    public static final Renderer getPlaceholder(Object key)
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
        return i18n.getPlaceholder(locale, key);
    }

    private static void printMissing(Object key)
    {
        if (!missing.contains(key))
        {
            missing.add(key);
            System.err.println(key+" = ");
        }
    }
    public static class LabelWrap implements Renderer
    {
        private final Object key;
        private final boolean placeholder;

        public LabelWrap(Object key, boolean placeholder)
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
                string = i18n.getPlaceholder(locale.get(), key);
            }
            else
            {
                string = i18n.getLabel(locale.get(), key);
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
