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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import org.vesalainen.bean.BeanHelper;
import org.vesalainen.html.Attribute;
import org.vesalainen.web.servlet.bean.BeanDocument;
import org.vesalainen.web.servlet.bean.BeanForm;
import org.vesalainen.web.servlet.bean.ThreadLocalContent;

/**
 *
 * @author tkv
 * @param <M>
 */
public class JAXBContent<M> extends ThreadLocalContent<M>
{
    private String action;
    
    public JAXBContent(ThreadLocal<M> local, BeanDocument document, String action)
    {
        super(local);
        this.action = action;
    }

    @Override
    public void append(Appendable out) throws IOException
    {
        M data = local.get();
        BeanForm form = new BeanForm(this, local, action);
        BeanHelper.stream(data)
                .filter((String s)->{return filter(data, s, XmlAttribute.class, XmlElement.class, XmlElements.class);})
                //.sorted(new ComparatorImpl<>(data))
                .forEach((String pattern)->
        {
            Annotation[] annotations = BeanHelper.getAnnotations(data, pattern);
            List<Attribute> attributes = new ArrayList<>();
            Xml2Html.inject(annotations, attributes);
            form.createInput(pattern, attributes);
        });
    }
    
    private boolean filter(M data, String pattern, Class<? extends Annotation>... allowed)
    {
        AnnotatedElement ae = BeanHelper.getAnnotatedElement(data, pattern);
        for (Class<? extends Annotation> ac : allowed)
        {
            if (ae.isAnnotationPresent(ac))
            {
                return true;
            }
        }
        return false;
    }
    private static class ComparatorImpl<C> implements Comparator<String>
    {
        private C data;

        public ComparatorImpl(C data)
        {
            this.data = data;
        }
        
        @Override
        public int compare(String o1, String o2)
        {
            String p1 = prefix(o1);
            String p2 = prefix(o2);
            if (p1.equals(p2))
            {
                AnnotatedElement a1 = BeanHelper.getAnnotatedElement(data, o1);
                AnnotatedElement a2 = BeanHelper.getAnnotatedElement(data, o2);
                boolean b1 = a1.isAnnotationPresent(XmlAttribute.class);
                boolean b2 = a2.isAnnotationPresent(XmlAttribute.class);
                if (b1 != b2)
                {
                    return b2 ? 1 : -1;
                }
            }
            return o1.compareTo(o2);
        }
        
        private String prefix(String pattern)
        {
            int idx = pattern.lastIndexOf('.');
            if (idx != -1)
            {
                return pattern.substring(0, idx);
            }
            return "";
        }
    }
}
