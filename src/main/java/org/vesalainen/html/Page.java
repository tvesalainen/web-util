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
package org.vesalainen.html;

import java.nio.charset.Charset;
import org.vesalainen.js.ScriptContainer;

/**
 *
 * @author tkv
 */
public interface Page extends Renderer
{
    Element getContent();
    void addToHeader(Renderer content);
    void addToFooter(Renderer content);
    default Form addForm(Object action)
    {
        return addForm("post", null, action);
    }
    default Form addForm(String method, Object action)
    {
        return addForm(method, null, action);
    }
    Form addForm(String method, String id, Object action);
    Form createForm(Content parent, String method, String id, Object action);
    /**
     * Return a single script container for page level scripts
     * @return 
     */
    ScriptContainer getScriptContainer();
    
}
