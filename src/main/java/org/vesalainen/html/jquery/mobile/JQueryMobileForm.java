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
import org.vesalainen.html.Attribute;
import org.vesalainen.html.Content;
import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.web.InputType;
import org.vesalainen.web.servlet.bean.BeanForm;

/**
 *
 * @author tkv
 * @param <C>
 */
public class JQueryMobileForm<C> extends BeanForm<C>
{
    private static final SimpleAttribute<String> ControlGroup = new SimpleAttribute<>("data-role", "controlgroup");
    
    private final Page page;
    
    public JQueryMobileForm(JQueryMobileDocument document, Page page, String method, Object action)
    {
        super(document, method, action);
        this.page = page;
        this.setAttr("data-ajax", document.isAjax());
    }
    
    @Override
    public Element multiCheckboxContainer(String field, String inputType, Class type, Content labelText, Content placeholder, Class innerType, Collection<Attribute> attrs)
    {
        return super.multiCheckboxContainer(field, inputType, type, labelText, placeholder, innerType, attrs)
                .setAttr(ControlGroup);
    }

    @Override
    public Element radioContainer(String field, String inputType, Class<?> type, Content labelText, Content placeholder, Collection<Attribute> attrs)
    {
        return super.radioContainer(field, inputType, type, labelText, placeholder, attrs)
                .setAttr(ControlGroup);
    }

    @Override
    public Element singleSelectorContainer(String field, String inputType, Object value, Content labelText, Content placeholder, Collection<Attribute> attrs)
    {
        return super.singleSelectorContainer(field, inputType, value, labelText, placeholder, attrs)
                .addClasses("ui-field-contain");
    }

    @Override
    public Element multipleSelectorContainer(String field, String inputType, Object value, Content labelText, Content placeholder, Collection<Attribute> attrs)
    {
        return super.multipleSelectorContainer(field, inputType, value, labelText, placeholder, attrs)
                .addClasses("ui-field-contain");
    }
    
}
