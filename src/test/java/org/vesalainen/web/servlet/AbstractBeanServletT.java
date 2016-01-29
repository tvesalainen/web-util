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
package org.vesalainen.web.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.Frameworks;
import org.vesalainen.web.Attr;
import org.vesalainen.web.InputType;
import org.vesalainen.web.server.EmbeddedServer;
import org.vesalainen.web.server.EmbeddedServerT;

/**
 *
 * @author tkv
 */
public class AbstractBeanServletT
{
    
    public AbstractBeanServletT()
    {
    }

    @Test
    public void test1()
    {
        try
        {
            EmbeddedServer server = new EmbeddedServer();
            server.addServlet(AbstractBeanServletT.BeanServletImpl.class, "/bs");
            server.start();
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
            Logger.getLogger(EmbeddedServerT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static class BeanServletImpl extends AbstractBeanServlet<Data>
    {

        @Override
        protected Data createData() throws IOException
        {
            return new Data();
        }

        @Override
        protected Document getDocument(Data data) throws IOException
        {
            Document doc = new Document("BeanServletTest");
            doc.use(Frameworks.JQueryMobile);
            Element page = doc.getBody().addElement("div")
                    .addAttr("data-role", "page");
            Element main = page.addElement("div")
                    .addAttr("data-role", "main")
                    .addClasses("ui-content");
            Element form = main.addElement("form")
                    .addAttr("method", "post");
            form.addContent(createInput(data, "text"));
            form.addContent(createInput(data, "number"));
            form.addContent(createInput(data, "area"));
            Element fs = (Element) createInput(data, "en");
            fs.addAttr("data-role", "controlgroup");
            form.addContent(fs);
            form.addContent(createInput(data, "on"));
            
            form.addContent(createInput(data, "submit"));
            return doc;
        }
        
    }
    public static class Data
    {
        enum En {E1, E2, E3};
        En en;
        String area;
        String submit;
        String text;
        int number;
        boolean on;

        public boolean isOn()
        {
            return on;
        }

        public void setOn(boolean on)
        {
            this.on = on;
        }

        public En getEn()
        {
            return en;
        }

        public void setEn(En en)
        {
            this.en = en;
        }

        @InputType(value="textarea",attrs={
            @Attr(name="rows", value="3"),
            @Attr(name="cols", value="40")
        })
        public String getArea()
        {
            return area;
        }

        public void setArea(String area)
        {
            this.area = area;
        }
        
        @InputType("submit")
        public String getSubmit()
        {
            return submit;
        }

        public void setSubmit(String submit)
        {
            this.submit = submit;
        }

        public int getNumber()
        {
            return number;
        }

        public void setNumber(int number)
        {
            this.number = number;
        }

        @InputType(value="text",attrs={
            @Attr(name="size", value="40")
        })
        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }
        
    }
}
