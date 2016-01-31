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

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.html.Document;
import org.vesalainen.html.Element;
import org.vesalainen.html.jquery.mobile.JQueryMobileBeanServlet;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
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
    public static class BeanServletImpl extends JQueryMobileBeanServlet<Data>
    {

        @Override
        protected Data createData()
        {
            return new Data();
        }

        @Override
        protected Document createDocument()
        {
            Data data = createData();
            JQueryMobileDocument doc = new JQueryMobileDocument("BeanServletTest");
            Element main = doc.getPage("page1");
            Element form = main.addElement("form")
                    .addAttr("method", "post");
            form.addContent(createInput(data, "submit"));

            form.addContent(createInput(data, "range"));
            form.addContent(createInput(data, "url"));
            form.addContent(createInput(data, "week"));
            form.addContent(createInput(data, "month"));
            form.addContent(createInput(data, "datetimelocal"));
            form.addContent(createInput(data, "time"));
            form.addContent(createInput(data, "date"));
            form.addContent(createInput(data, "color"));
            form.addContent(createInput(data, "mul"));
            form.addContent(createInput(data, "sel"));
            form.addContent(createInput(data, "text"));
            form.addContent(createInput(data, "number"));
            form.addContent(createInput(data, "area"));
            form.addContent(createInput(data, "en"));
            form.addContent(createInput(data, "on"));
            form.addContent(createInput(data, "es"));
            
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
        EnumSet<En> es = EnumSet.noneOf(En.class);
        En sel;
        EnumSet<En> mul = EnumSet.noneOf(En.class);
        Color color;
        Date date;
        Date time;
        Date datetimelocal;
        Date month;
        Date week;
        URL url;
        int range;

        @InputType(value="range",attrs={
            @Attr(name="min", value="3"),
            @Attr(name="max", value="40")
        })
        public int getRange()
        {
            return range;
        }

        public void setRange(int range)
        {
            this.range = range;
        }

        public URL getUrl()
        {
            return url;
        }

        public void setUrl(URL url)
        {
            this.url = url;
        }
        

        @InputType("week")
        public Date getWeek()
        {
            return week;
        }

        public void setWeek(Date week)
        {
            this.week = week;
        }

        @InputType("month")
        public Date getMonth()
        {
            return month;
        }

        public void setMonth(Date month)
        {
            this.month = month;
        }

        @InputType("datetime-local")
        public Date getDatetimelocal()
        {
            return datetimelocal;
        }

        public void setDatetimelocal(Date datetimelocal)
        {
            this.datetimelocal = datetimelocal;
        }

        @InputType("time")
        public Date getTime()
        {
            return time;
        }

        public void setTime(Date time)
        {
            this.time = time;
        }

        @InputType("date")
        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
        }
        

        public Color getColor()
        {
            return color;
        }

        public void setColor(Color color)
        {
            this.color = color;
        }
        

        @InputType(value="select", enumType=En.class)
        public EnumSet<En> getMul()
        {
            return mul;
        }

        public void setMul(EnumSet<En> mul)
        {
            this.mul = mul;
        }

        @InputType(value="select")
        public En getSel()
        {
            return sel;
        }

        public void setSel(En sel)
        {
            this.sel = sel;
        }
        
        @InputType(value="checkbox", enumType=En.class)
        public EnumSet<En> getEs()
        {
            return es;
        }

        public void setEs(EnumSet<En> es)
        {
            this.es = es;
        }

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
