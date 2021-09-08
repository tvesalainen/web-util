/* 
 * Copyright (C) 2021 Timo Vesalainen <timo.vesalainen@iki.fi>
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
    $('#jstree')
  // listen for event
  .on('changed.jstree', function (e, data) {
    id = data.node.id;
    if (!id.endsWith('*'))
    {
        $('#mbean').load('ajax_nodes.html', { 'id' : data.node.id }, function(responseTxt, statusTxt, xhr){
            $('.attributeValue').each(function(index, element){
                var objectname = $(this).attr('data-objectname');
                var id = this.id;
                $(this).load('ajax_nodes.html', { 'id' : objectname, 'attribute' : id }, function(responseTxt, statusTxt, xhr){
                    $('.attributeInput').change(function(){
                        $.post('ajax_nodes.html', $('form').serialize()), function(responseTxt, statusTxt, xhr){
                            a = this;
                        };
                    })
                });
            })
        })
    }
  })
.jstree({
      'core' : {
        'data' : {
          'url' : 'ajax_nodes.html',
          'data' : function (node) {
            return { 'id' : node.id };
          }
        }
      }
    });
});


