function changeTo() {
    let origin = document.getElementById("origin").value;
    document.getElementById("origin").value = document.getElementById("destination").value;
    document.getElementById("destination").value = origin;
}

$(document).ready(function() {
    const orgModal = $("#originModal");
    const desModal = $("#desModal");
    const input = $("#origin");
    const orgCloseBtn = $("#orgClose");
    const desCloseBtn = $("#desClose");
    const des = $("#destination");

    function originAirport() {
        if (input.val().trim() !== "") {
            $('#originModalBody').empty();
            $("#modalOrigin").val(input.val());
            $.ajax({
                type : 'post',
                url : 'findAirport',
                data : { input : input.val() },
                dataType : 'json',
                success : function(res) {

                    $('#originModalBody').empty();

                    $.each(res, function(i) {
                        let txt = "<span style='cursor: pointer' class='originAirportItem'>" + res[i].korName + '(' + res[i].iata + ')' + "</span>" + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                            "<span>" + res[i].korCountry + "&nbsp;&nbsp;&nbsp;" + res[i].engCity + "</span><br>";

                        $('#originModalBody').append(txt);

                    });

                    $('.originAirportItem').click(function() {
                        input.val($(this).text());
                        $('#originModalBody').empty();
                        orgModal.hide();
                    });
                },
                error : function() {
                    alert('공항 이름 로드 중 에러 발생');
                }
            })
        } else {
            orgModal.hide();
        }
    }

    input.on('input', function() {
        if(input.val().trim() !== "") {
            orgModal.show();
            originAirport();
        }else {
            orgModal.hide();
        }
    });

    function desAirport() {
        if (des.val().trim() !== "") {
            $("#modalDes").val(des.val());
            $.ajax({
                type : 'post',
                url : 'findAirport',
                data : { input : des.val() },
                dataType : 'json',
                success : function(res) {

                    $('#desModalBody').empty();

                    $.each(res, function(i) {
                        let txt = "<span style='cursor: pointer' class='desAirportItem'>" + res[i].korName + '(' + res[i].iata + ')' + "</span>" +"&nbsp;&nbsp;&nbsp;&nbsp;" +
                            "<span>" + res[i].korCountry + "&nbsp;&nbsp;&nbsp;" + res[i].engCity + "</span><br>";

                        $('#desModalBody').append(txt);

                    });

                    $('.desAirportItem').click(function() {
                        des.val($(this).text());
                        $('#desModalBody').empty();
                        desModal.hide();
                    });
                },
                error : function() {
                    alert('공항 이름 로드 중 에러 발생');
                }
            })
        } else {
            desModal.hide();
        }
    }

    des.on('input', function() {
        if (des.val().trim() !== "") {
            desModal.show();
            desAirport();
        } else {
            desModal.hide();
        }
    });

    orgCloseBtn.on('click', function() {
        orgModal.hide();
    });

    desCloseBtn.on('click', function() {
        desModal.hide();
    });

    $(window).on('click', function(event) {
        if ($(event.target).is(orgModal)) {
            orgModal.hide();
        }

        if ($(event.target).is(desModal)) {
            desModal.hide();
        }
    });

    /*$('#depDate').click(function() {
        $.ajax({
            url: '/calendar/cal.html',
            type: 'GET',
            success: function(data) {
                $('#depCalendarContainer').html(data).removeClass('hidden'); // 클래스 hidden 제거
            },
            error: function(xhr, status, error) {
                console.error('Fetch error:', error);
            }
        });
    });*/

    $('.toggleBtn').click(function() {
        $(this).closest('.card-body').find('.tog-body').toggle();
    });

    $('.cartBtn').click(function() {
        let card = $(this).closest('.card');
        let ffvData = card.data('ffv');
        let ffvId = $(this).closest('.card').find('#ffvId').val();
        let origin = $(this).closest('.card').find('#origin').val();
        let des = $(this).closest('.card').find('#destination').val();
        let dep = $(this).closest('.card').find('#departure').val();
        let comb = $(this).closest('.card').find('#comeback').val();
        let memberId = $(this).closest('.card').find('#memberId').val();

        insertFfv(ffvData, ffvId, origin, des, dep, comb, memberId);
    });

    function insertFfv(data, ffvId, origin, des, dep, comb, memberId) {
        $.ajax({
           type : 'post',
           url : 'insertFfv',
           data : { ffv : data,
                    ffvId : ffvId,
                    origin : origin,
                    des : des,
                    dep : dep,
                    comb : comb,
                    memberId : memberId
                    },
           success : function() {
               alert("장바구니에 추가되었습니다.");
           },
           error : function() {
               alert('db 저장 중 오류 발생');
           }
        });
    }
})