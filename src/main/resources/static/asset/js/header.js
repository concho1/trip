$(document).ready(function() {
    $('#ex').click(function() {
        let ck = $(this).find('.current').hasClass('checked');

        if(!ck) {
            $(this).find('.current').addClass('checked');
        }else {
            $(this).find('.current').removeClass('checked');
        }

    });
});