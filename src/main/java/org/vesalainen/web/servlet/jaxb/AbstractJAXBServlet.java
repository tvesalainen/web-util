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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiFunction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.vesalainen.http.Query;
import org.vesalainen.web.servlet.bean.AbstractBeanServlet;
import org.vesalainen.web.servlet.bean.BeanDocument;

/**
 *
 * @author tkv
 * @param <D>
 * @param <C>
 */
public class AbstractJAXBServlet<D extends BeanDocument,C> extends AbstractBeanServlet<D,C>
{
    private File storage;
    private final JAXBContext jaxbCtx;
    private final Object factory;
    private final String action;
    private BiFunction<ThreadLocal<C>,String,D> documentFactory;

    public AbstractJAXBServlet(String packageName, File storage, String action, BiFunction<ThreadLocal<C>,String,D> documentFactory)
    {
        try
        {
            this.storage = storage;
            this.action = action;
            this.documentFactory = documentFactory;
            jaxbCtx = JAXBContext.newInstance(packageName);
            Class<?> cls = Class.forName(packageName+".ObjectFactory");
            factory = cls.newInstance();
        }
        catch (JAXBException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
    
    @Override
    protected void onSubmit(C data, String field, Query query)
    {
        try
        {
            try (FileOutputStream out = new FileOutputStream(storage))
            {
                Marshaller marshaller = jaxbCtx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(data, out);
            }
        }
        catch (JAXBException | IOException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    protected C createData()
    {
        try
        {
            Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            return (C) unmarshaller.unmarshal(storage);
        }
        catch (JAXBException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    protected D createDocument()
    {
        D doc = documentFactory.apply(threadLocalData, title);
        doc.getBody().addContent(new JAXBContent(threadLocalData, doc, action));
        return doc;
    }
}
