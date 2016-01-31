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

import java.io.IOException;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.Element;
import org.vesalainen.web.InputType;
import org.vesalainen.web.servlet.bean.AbstractBeanServlet;

/**
 *
 * @author tkv
 * @param <D>
 */
public abstract class JQueryMobileBeanServlet<D> extends AbstractBeanServlet<D>
{
    private static final Attribute<String> ControlGroup = new Attribute<>("data-role", "controlgroup");
    
    @Override
    protected Element multiCheckboxContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.multiCheckboxContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .addAttr(ControlGroup);
    }

    @Override
    protected Element radioContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.radioContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .addAttr(ControlGroup);
    }

    @Override
    protected Element selectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.selectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .addAttr(ControlGroup);
    }
    
}
