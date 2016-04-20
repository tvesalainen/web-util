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
"use strict";
/* global p */

$(document).ready(function () {
    var events = [];
    var targets = $("[data-sse-sink]");
    targets.each(function () {
        var ev = $(this).attr('data-sse-sink');
        if (events.indexOf(ev) === -1) {
            events.push(ev);
        }
    });
    if (events.length > 0) {
        var url = '/sse' + '?events=' + events;
        localStorage.events = events;
        var eventSource = new EventSource(url);
        targets.each(function () {
            eventSource.addEventListener($(this).attr('data-sse-sink'), function (event) {
                var json = JSON.parse(event.data);
                $("[data-sse-sink=" + event.type + "]").each(function () {
                    for (var p in json) {
                        switch (p){
                            default:
                                $(this).attr(p, json[p]);
                                $(this).attr('data-ttv', '5');
                                break;
                            case 'html':
                                $(this).html(json[p]);
                                $(this).attr('data-ttv', '5');
                                break;
                            case 'text':
                                $(this).text(json[p]);
                                $(this).attr('data-ttv', '5');
                                break;
                            case 'append':
                                $(this).append(json[p]);
                                break;
                            case 'prepend':
                                $(this).prepend(json[p]);
                                break;
                            case 'after':
                                $(this).after(json[p]);
                                break;
                            case 'before':
                                $(this).before(json[p]);
                                break;
                        }
                    }
                });
            }, false);
            //$(this).attr('data-ttv', '5');
        });
    }
    
    var fadeId = setInterval(fade, 5000);
    function fade()
    {
        $("[data-ttv]").each(function(){
            var val = Number($(this).attr("data-ttv"));
            val--;
            if (val === 0)
            {
                $(this).fadeOut("slow");
                $(this).attr("data-ttv", null);
            }
            else
            {
                $(this).attr("data-ttv", val);
                $(this).show();
            }
        })
    }
    var removeId = setInterval(remove, 1000);
    function remove()
    {
        $("[data-ttl]").each(function(){
            var val = Number($(this).attr("data-ttl"));
            val--;
            if (val === 0)
            {
                $(this).remove();
            }
            else
            {
                $(this).attr("data-ttl", val);
            }
        })
    }
});
