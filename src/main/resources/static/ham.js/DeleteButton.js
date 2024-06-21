function deleteItem(num){

    var hotelKeyVal = $('#hotelKey' + num).text();
    alert(hotelKeyVal);

    $.ajax({
        url: '/member/hamster/airplane/categoryDelete',
        method: 'POST',
        data: { hotelKeyVal: hotelKeyVal },
        success: function(data) {
            $('.myedu-cont-wrap').html(data);
        },
        error: function(xhr, status, error) {
            console.error(error);
        }
    });

}