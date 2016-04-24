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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.vesalainen.http.Query;
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
            server.startAndWait();
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
            JQueryMobileForm form = page.addForm(null, null);
            form.addInputs(
                "submit",
                "submit2",
                "selector2",
                "selector",
                "range",
                "url",
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
        protected void onSubmit(Data data, String field, Query query)
        {
            System.err.println("submit("+field+")="+query);
        }

    }
    public static class Data
    {
        public List<Integer> hset = new ArrayList<>();
        public long hidden;
        public enum En {E1, E2, E3};
        public En en;
        @InputType(value="textarea",attrs={
            @Attr(name="rows", value="3"),
            @Attr(name="cols", value="40")
        })
        public String area;
        @InputType("submit")
        public String submit;
        @InputType("submit")
        public String submit2;
        @InputType(value="text",attrs={
            @Attr(name="size", value="40")
        })
        public String text;
        public int number;
        public boolean on;
        @InputType(value="checkbox")
        public EnumSet<En> es = EnumSet.noneOf(En.class);
        @InputType(value="select")
        public En sel;
        @InputType(value="select")
        public EnumSet<En> mul = EnumSet.noneOf(En.class);
        public Color color;
        public LocalDate date;
        @InputType("time")
        public LocalTime time;
        @InputType("datetime-local")
        public LocalDateTime datetimelocal;
        public URL url;
        @InputType(value="range",attrs={
            @Attr(name="min", value="3"),
            @Attr(name="max", value="40")
        })
        public int range;
        public MultipleSelectorImpl<Integer> selector = new MultipleSelectorImpl<>(1, 2, 3, 4);
        public SingleSelectorImpl<Double> selector2 = new SingleSelectorImpl<>(1.0, 2.0, 3.0, 4.0);


    }
}
