$(".delete").click(function(){
    var id = $(this).attr("data-pattern");
    var target = $("#"+id);
    target.html("<input name='"+id+"#' type='text' hidden>");
    $('form').submit();
});

$(".add").click(function(){
    var id = $(this).attr("data-pattern");
    var target = $("form");
    target.append("<input name='"+id+"' type='text' hidden>");
    var f = $('form');
    f.submit();
});

$(".submit").click(function(){
    $('form').submit();
});

