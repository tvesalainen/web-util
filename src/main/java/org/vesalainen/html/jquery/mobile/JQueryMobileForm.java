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

import org.vesalainen.html.Element;
import org.vesalainen.html.Page;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.js.ScriptContainer;
import org.vesalainen.web.InputType;
import org.vesalainen.web.servlet.bean.BeanDocument;
import org.vesalainen.web.servlet.bean.BeanForm;

/**
 *
 * @author tkv
 */
public class JQueryMobileForm extends BeanForm
{
    private static final SimpleAttribute<String> ControlGroup = new SimpleAttribute<>("data-role", "controlgroup");
    
    private final Page page;
    
    public JQueryMobileForm(JQueryMobileDocument document, Page page, String method, String action)
    {
        super(document, method, action);
        this.page = page;
        this.setAttr("data-ajax", document.isAjax());
    }
    
    @Override
    protected void addHideScript()
    {
        if (!hasHideScript)
        {
            hasHideScript = true;
            ScriptContainer sc = page.getScriptContainer();
            sc.addCode("$('.hidden').hide();");
        }
    }

    @Override
    protected Element multiCheckboxContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.multiCheckboxContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .setAttr(ControlGroup);
    }

    @Override
    protected Element radioContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.radioContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .setAttr(ControlGroup);
    }

    @Override
    protected Element selectContainer(String field, String inputType, Class type, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.selectContainer(field, inputType, type, labelText, placeholder, inputTypeAnnotation)
                .addClasses("ui-field-contain");
    }

    @Override
    protected Element singleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.singleSelectorContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation)
                .addClasses("ui-field-contain");
    }

    @Override
    protected Element multipleSelectorContainer(String field, String inputType, Object value, String labelText, String placeholder, InputType inputTypeAnnotation)
    {
        return super.multipleSelectorContainer(field, inputType, value, labelText, placeholder, inputTypeAnnotation)
                .addClasses("ui-field-contain");
    }
    
}
