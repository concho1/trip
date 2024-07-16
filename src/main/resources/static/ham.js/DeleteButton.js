function deleteAir(num) {

    var AirKeyVal = $('#airKey_'+num).val();

    alert(AirKeyVal);

    $.ajax({
        url: '/member/hamster/airplane/cartAirDelete',
        method: 'POST',
        data: { AirKeyVal: AirKeyVal},
        dataType: 'json',
        success: function(data) {
            if (data.success) {
                $('#airList').find('#airKey_'+num).closest('.myedu-area').remove();
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

function deleteHotel(num) {

    var hotelKeyVal = $('#hotelKey_'+num).val();

    alert(hotelKeyVal)

    $.ajax({
        url: '/member/hamster/airplane/cartHotelDelete',
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
