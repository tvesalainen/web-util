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

$(document).ready(function () {
    var events;
    $("[data-sse-sink]").each(function () {
        if (events)
            events = events + ',' + $(this).attr('data-sse-sink');
        else
            events = $(this).attr('data-sse-sink');
    });
    if (events) {
        var url = '/sse' + '?events=' + events;
        localStorage.events = events;
        var eventSource = new EventSource(url);
        $("[data-sse-sink]").each(function () {
            eventSource.addEventListener($(this).attr('data-sse-sink'), function (event) {
                $("[data-sse-sink="+event.type+"]").each(function(){
                    $(this).html(event.data);
                });
            }, false);
        });
    }
});
