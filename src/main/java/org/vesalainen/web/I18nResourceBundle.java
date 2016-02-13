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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author tkv
 */
public class I18nResourceBundle implements I18nSupport
{
    private final String name;
    private final Map<Locale,ResourceBundle> map = new HashMap<>();

    public I18nResourceBundle(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getLabel(Locale locale, Object key)
    {
        ResourceBundle rb = find(locale);
        if (rb != null)
        {
            try
            {
                return rb.getString(key.toString());
            }
            catch (MissingResourceException ex)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public String getPlaceholder(Locale locale, Object key)
    {
        ResourceBundle rb = find(locale);
        if (rb != null)
        {
            try
            {
                return rb.getString(key.toString());
            }
            catch (MissingResourceException ex)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    private ResourceBundle find(Locale locale)
    {
        if (map.containsKey(locale))
        {
            return map.get(locale); // can return null
        }
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle(name, locale);
            map.put(locale, rb);
            return rb;
        }
        catch (MissingResourceException ex)
        {
            map.put(locale, null);
            return null;
        }
    }
    
}
