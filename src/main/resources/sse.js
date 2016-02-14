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
                var x;
                $("[data-sse-sink=" + event.type + "]").each(function () {
                    var json = JSON.parse(event.data);
                    for (p in json) {
                        if (p === 'html') {
                            $(this).html(json[p]);
                        }
                        else
                        {
                            if (p === 'text') {
                                $(this).text(json[p]);
                            }
                            else
                            {
                                $(this).attr(p, json[p]);
                            }
                        }
                    }
                });
            }, false);
        });
    }
});
