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
package org.vesalainen.web.servlet.jaxb;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import org.vesalainen.html.Attribute;
import org.vesalainen.html.AttributedContent;
import org.vesalainen.html.SimpleAttribute;
import org.vesalainen.util.Bijection;
import org.vesalainen.util.HashBijection;

/**
 *
 * @author tkv
 */
public class Xml2Html
{
    private static final Bijection<String,String> map = new  HashBijection<>();
    
    static
    {
        map.put("fractionDigits", null);    //	Specifies the maximum number of decimal places allowed. Must be equal to or greater than zero
        map.put("length", null);    //		Specifies the exact number of characters or list items allowed. Must be equal to or greater than zero
        map.put("maxExclusive", null);    //		Specifies the upper bounds for numeric values (the value must be less than this value)
        map.put("maxInclusive", null);    //		Specifies the upper bounds for numeric values (the value must be less than or equal to this value)
        map.put("maxLength", "maxLength");    //		Specifies the maximum number of characters or list items allowed. Must be equal to or greater than zero
        map.put("minExclusive", null);    //		Specifies the lower bounds for numeric values (the value must be greater than this value)
        map.put("minInclusive", null);    //		Specifies the lower bounds for numeric values (the value must be greater than or equal to this value)
        map.put("minLength", null);    //		Specifies the minimum number of characters or list items allowed. Must be equal to or greater than zero
        map.put("pattern", null);    //		Defines the exact sequence of characters that are acceptable
        map.put("totalDigits", null);    //		Specifies the maximum number of digits allowed. Must be greater than zero
        map.put("whiteSpace", null);    //		Specifies how white space (line feeds, tabs, spaces, and carriage returns) is handled    }
    }
    
    public static final void inject(String xmlAttr, String xmlValue, AttributedContent ac)
    {
        
    }
    public static final void inject(Annotation[] annotations, List<Attribute> attributes)
    {
        for (Annotation a : annotations)
        {
            if (a instanceof XmlElement)
            {
                XmlElement element = (XmlElement) a;
                if (element != null && element.required())
                {
                    attributes.add(new SimpleAttribute("required", true));
                }
            }
            if (a instanceof XmlAttribute)
            {
                XmlAttribute attribute = (XmlAttribute) a;
                if (attribute != null && attribute.required())
                {
                    attributes.add(new SimpleAttribute("required", true));
                }
            }
            if (a instanceof XmlSchemaType)
            {
                XmlSchemaType schemaType = (XmlSchemaType) a;
                if (schemaType != null)
                {
                    switch (schemaType.name())
                    {
                        case "ENTITIES":
                        case "ENTITY":
                        case "ID":  // A string that represents the ID attribute in XML (only used with schema attributes)
                        case "IDREF":  // 	A string that represents the IDREF attribute in XML (only used with schema attributes)
                        case "IDREFS":  // 	 
                        case "language":  // 	A string that contains a valid language id
                        case "Name":  // 	A string that contains a valid XML name
                        case "NCName":  // 	 
                        case "NMTOKEN":  // 	A string that represents the NMTOKEN attribute in XML (only used with schema attributes)
                        case "NMTOKENS":  // 	 
                        case "normalizedString":  // 	A string that does not contain line feeds, carriage returns, or tabs
                        case "QName":  // 	 
                        case "string":  // 	A string
                        case "token":  // 	A string that does not contain line feeds, carriage returns, tabs, leading or trailing spaces, or multiple spaces            }
                            attributes.add(new SimpleAttribute("type", "text"));
                            break;

                        case "date":  // 	Defines a date value 2002-09-24 / 2002-09-24Z / 2002-09-24-06:00
                            attributes.add(new SimpleAttribute("type", "date"));
                            break;
                        case "dateTime":  // 	Defines a date and time value  YYYY-MM-DDThh:mm:ss / 2002-05-30T09:30:10Z
                            attributes.add(new SimpleAttribute("type", "datetime-local"));
                            break;
                        case "duration":  // 	Defines a time interval PnYnMnDTnHnMnS
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("placeholder", "PnYnMnDTnHnMnS"));
                            break;
                        case "gDay":  // 	Defines a part of a date - the day (DD)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "1"));
                            attributes.add(new SimpleAttribute("max", "31"));
                            attributes.add(new SimpleAttribute("placeholder", "1-31"));
                            break;
                        case "gMonth":  // 	Defines a part of a date - the month (MM)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "1"));
                            attributes.add(new SimpleAttribute("max", "12"));
                            attributes.add(new SimpleAttribute("placeholder", "1-12"));
                            break;
                        case "gMonthDay":  // 	Defines a part of a date - the month and day (MM-DD)
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("pattern", "[0-9]{2}-[0-9]{2}"));
                            attributes.add(new SimpleAttribute("placeholder", "99-99"));
                            break;
                        case "gYear":  // 	Defines a part of a date - the year (YYYY)
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("pattern", "[0-9]{4}"));
                            attributes.add(new SimpleAttribute("placeholder", "9999"));
                            break;
                        case "gYearMonth":  // 	Defines a part of a date - the year and month (YYYY-MM)
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("pattern", "[0-9]{4}-[0-9]{2}"));
                            attributes.add(new SimpleAttribute("placeholder", "9999-99"));
                            break;
                        case "time":  // 	Defines a time value 09:00:00 / 09:30:10Z / 09:30:10+06:00
                            attributes.add(new SimpleAttribute("type", "time"));
                            break;

                        case "byte":  // 	A signed 8-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", Byte.MIN_VALUE));
                            attributes.add(new SimpleAttribute("max", Byte.MAX_VALUE));
                            attributes.add(new SimpleAttribute("placeholder", "byte"));
                            break;
                        case "decimal":  // 	A decimal value
                        case "double":  // 	 
                        case "float":  // 	 
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("pattern", "[0-9]+[\\.\\,][0-9]*"));
                            attributes.add(new SimpleAttribute("placeholder", "99.99"));
                            break;
                        case "int":  // 	A signed 32-bit integer
                        case "integer":  // 	An integer value
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", Integer.MIN_VALUE));
                            attributes.add(new SimpleAttribute("max", Integer.MAX_VALUE));
                            attributes.add(new SimpleAttribute("placeholder", "int"));
                            break;
                        case "long":  // 	A signed 64-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", Long.MIN_VALUE));
                            attributes.add(new SimpleAttribute("max", Long.MAX_VALUE));
                            attributes.add(new SimpleAttribute("placeholder", "long"));
                            break;
                        case "negativeInteger":  // 	An integer containing only negative values (..,-2,-1)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("max", "-1"));
                            attributes.add(new SimpleAttribute("placeholder", "long"));
                            break;
                        case "nonNegativeInteger":  // 	An integer containing only non-negative values (0,1,2,..)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "0"));
                            attributes.add(new SimpleAttribute("placeholder", "long"));
                            break;
                        case "nonPositiveInteger":  // 	An integer containing only non-positive values (..,-2,-1,0)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("max", "0"));
                            attributes.add(new SimpleAttribute("placeholder", "long"));
                            break;
                        case "positiveInteger":  // 	An integer containing only positive values (1,2,..)
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "1"));
                            attributes.add(new SimpleAttribute("placeholder", "long"));
                            break;
                        case "short":  // 	A signed 16-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", Short.MIN_VALUE));
                            attributes.add(new SimpleAttribute("max", Short.MAX_VALUE));
                            attributes.add(new SimpleAttribute("placeholder", "short"));
                            break;
                        case "unsignedLong":  // 	An unsigned 64-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "0"));
                            attributes.add(new SimpleAttribute("max", Long.toUnsignedString(Long.MIN_VALUE)));
                            attributes.add(new SimpleAttribute("placeholder", "unsignedLong"));
                            break;
                        case "unsignedInt":  // 	An unsigned 32-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "0"));
                            attributes.add(new SimpleAttribute("max", Integer.toUnsignedString(Integer.MIN_VALUE)));
                            attributes.add(new SimpleAttribute("placeholder", "unsignedInt"));
                            break;
                        case "unsignedShort":  // 	An unsigned 16-bit integer
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "0"));
                            attributes.add(new SimpleAttribute("max", Short.toUnsignedInt(Short.MIN_VALUE)));
                            attributes.add(new SimpleAttribute("placeholder", "unsignedInt"));
                            break;
                        case "unsignedByte":  // 	An unsigned 8-bit integer                    
                            attributes.add(new SimpleAttribute("type", "number"));
                            attributes.add(new SimpleAttribute("min", "0"));
                            attributes.add(new SimpleAttribute("max", Short.toUnsignedInt(Short.MIN_VALUE)));
                            attributes.add(new SimpleAttribute("placeholder", "unsignedInt"));
                            break;

                        case "anyURI":  // 	 
                            attributes.add(new SimpleAttribute("type", "url"));
                            break;
                        case "base64Binary":  // 	 
                            attributes.add(new SimpleAttribute("type", "text"));
                            break;
                        case "boolean":  // 	 
                            attributes.add(new SimpleAttribute("type", "check"));
                            break;
                        case "hexBinary":  // 	 
                            attributes.add(new SimpleAttribute("type", "text"));
                            attributes.add(new SimpleAttribute("pattern", "[0-9a-fA-F]*"));
                            break;
                        case "NOTATION":  // 	 
                            break;
                        default:
                            throw new UnsupportedOperationException(schemaType.name()+" not supported");
                    }
                }
            }
        }
    }
}
