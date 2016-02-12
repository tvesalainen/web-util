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

import java.util.Locale;

/**
 *
 * @author tkv
 */
public class I18n
{
    private static I18nSupport i18n = new StupidI18n();
    private static final ThreadLocal<Locale> locale = new ThreadLocal<>();

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

    public static final String getLabel(Object key)
    {
        return i18n.getLabel(locale.get(), key);
    }

    public static final String getLabel(Locale locale, Object key)
    {
        return i18n.getLabel(locale, key);
    }

    public static final String getPlaceholder(Object key)
    {
        return i18n.getPlaceholder(locale.get(), key);
    }

    public static final String getPlaceholder(Locale locale, Object key)
    {
        return i18n.getPlaceholder(locale, key);
    }
}
