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
package org.vesalainen.html.jquery.mobile;

import java.util.Collection;
import java.util.EnumSet;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.Element;
import org.vesalainen.html.Renderer;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.web.MultipleSelector;
import org.vesalainen.web.SingleSelector;
import org.vesalainen.web.servlet.bean.BeanForm;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 * @param <M>
 */
public class JQueryMobileForm<M> extends BeanForm<M>
{
    private static final SimpleAttribute<String> ControlGroup = new SimpleAttribute<>("data-role", "controlgroup");
    
    private final Element page;
    
    public JQueryMobileForm(Element page, ThreadLocal<Context<M>> threadLocalData, String method, Object action)
    {
        super(page, threadLocalData, method, action);
        this.page = page;
        this.setDataAttr("ajax", false);
    }

    @Override
    public Element multiCheckboxContainer(String field, EnumSet enumSet, String inputType, Class type, Renderer labelText, Renderer placeholder, Class<Enum> innerType, Collection<Attribute> attrs)
    {
        return super.multiCheckboxContainer(field, enumSet, inputType, type, labelText, placeholder, innerType, attrs)
                .setAttr(ControlGroup);
    }

    @Override
    public Element radioContainer(String field, Enum value, String inputType, Class<Enum> type, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        return super.radioContainer(field, value, inputType, type, labelText, placeholder, attrs)
                .setAttr(ControlGroup);
    }

    @Override
    public Element singleSelectorContainer(String field, SingleSelector selector, String inputType, Object value, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        return super.singleSelectorContainer(field, selector, inputType, value, labelText, placeholder, attrs)
                .setAttr(ControlGroup);
    }

    @Override
    public Element multipleSelectorContainer(String field, MultipleSelector selector, String inputType, Object value, Renderer labelText, Renderer placeholder, Collection<Attribute> attrs)
    {
        return super.multipleSelectorContainer(field, selector, inputType, value, labelText, placeholder, attrs)
                .setAttr(ControlGroup);
    }
    
}
