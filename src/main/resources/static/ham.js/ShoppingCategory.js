$(document).ready(function() {
    $('.tab-menu').click(function() {
        var category = $(this).data('category');

        alert(category);

        $.ajax({
            url: '/member/hamster/airplane/categoryUpdate',
            method: 'POST',
            data: { category: category },
            success: function(data) {
                $('.myedu-cont-wrap').html(data);
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });

        $('.tab-menu').removeClass('active');
        $(this).addClass('active');
    });
});
