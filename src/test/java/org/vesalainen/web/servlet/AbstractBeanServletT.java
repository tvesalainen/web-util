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
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.html.jquery.mobile.JQueryMobileServlet;
import org.vesalainen.html.jquery.mobile.JQueryMobileDocument;
import org.vesalainen.html.jquery.mobile.JQueryMobilePage;
import org.vesalainen.html.jquery.mobile.JQueryMobileForm;
import org.vesalainen.web.Attr;
import org.vesalainen.web.InputType;
import org.vesalainen.web.MultipleSelectorImpl;
import org.vesalainen.web.SingleSelectorImpl;
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
    public static class BeanServletImpl extends JQueryMobileServlet<JQueryMobileDocument,Data>
    {

        @Override
        protected Data createData()
        {
            return new Data();
        }

        @Override
        protected JQueryMobileDocument createDocument()
        {
            JQueryMobileDocument<Data> doc = new JQueryMobileDocument(threadLocalData, "BeanServletTest");
            JQueryMobilePage page = doc.getPage("page1");
            JQueryMobileForm form = page.addForm(null);
            form.addInputs(
                "submit",
                "submit2",
                "selector2",
                "selector",
                "range",
                "url",
                "week",
                "month",
                "datetimelocal",
                "time",
                "date",
                "color",
                "mul",
                "sel",
                "text",
                "number",
                "area",
                "en",
                "on",
                "es");
            
            form.addRestAsHiddenInputs();
            return doc;
        }

        @Override
        protected void onSubmit(Data data, String field, String value)
        {
            System.err.println("submit("+field+")="+value);
        }

    }
    public static class Data
    {
        List<Integer> hset = new ArrayList<>();
        long hidden;
        enum En {E1, E2, E3};
        En en;
        String area;
        String submit;
        String submit2;
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
        MultipleSelectorImpl<Integer> selector = new MultipleSelectorImpl<>(1, 2, 3, 4);
        SingleSelectorImpl<Double> selector2 = new SingleSelectorImpl<>(1.0, 2.0, 3.0, 4.0);

        @InputType(itemType=Integer.class)
        public List<Integer> getHset()
        {
            return hset;
        }

        public void setHset(List<Integer> hset)
        {
            this.hset = hset;
        }

        public long getHidden()
        {
            return hidden;
        }

        public void setHidden(long hidden)
        {
            this.hidden = hidden;
        }

        public SingleSelectorImpl<Double> getSelector2()
        {
            return selector2;
        }

        public void setSelector2(SingleSelectorImpl<Double> selector2)
        {
            this.selector2 = selector2;
        }

        public MultipleSelectorImpl<Integer> getSelector()
        {
            return selector;
        }

        public void setSelector(MultipleSelectorImpl<Integer> selector)
        {
            this.selector = selector;
        }

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
        

        @InputType(value="select", itemType=En.class)
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
        
        @InputType(value="checkbox", itemType=En.class)
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
        
        @InputType("submit")
        public String getSubmit()
        {
            return submit;
        }

        public void setSubmit(String submit)
        {
            this.submit = submit;
        }

        @InputType("submit")
        public String getSubmit2()
        {
            return submit2;
        }

        public void setSubmit2(String submit2)
        {
            this.submit2 = submit2;
            int size = hset.size();
            if (size > 1)
            {
                int i1 = hset.get(size-2);
                int i2 = hset.get(size-1);
                hset.add(i1+i2);
            }
            else
            {
                hset.add(1);
            }
        }

    }
}
