function deleteItem(num) {

    var hotelKeyVal = $('#hotelKey_'+num).text().trim();

    $.ajax({
        url: '/member/hamster/airplane/categoryDelete',
        method: 'POST',
        data: { hotelKeyVal: hotelKeyVal},
        dataType: 'json',
        success: function(data) {
            if (data.success) {
                $('#hotelList').find('#hotelKey_'+num).closest('.myedu-area').remove();
            } else {
                alert('장바구니 삭제 중 오류 발생' + data.message);
            }
        },
        error: function(xhr, status, error) {
            console.error(error);
            alert('AJAX 오류 발생: ' + error);
        }
    });
}
