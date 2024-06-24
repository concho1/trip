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
            console.log(input.val());
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
            console.log(des.val());
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

    $('#depDate').click(function() {
        // 경로 설정: template/cal.html
        let pathToCalHtml = 'templates/jhm/cal.html';

        // ajax 요청
        $.ajax({
            url: pathToCalHtml,
            type: 'GET',
            dataType: 'html',
            success: function(data) {
                $('#calendarContainer').html(data); // calendarContainer에 삽입
                $('#calendarContainer').removeClass('hidden'); // 클래스 hidden 제거
            },
            error: function(xhr, status, error) {
                console.error('Fetch error:', error);
            }
        });
    });

    $('.toggleBtn').click(function() {
        $(this).closest('.card-body').find('.tog-body').toggle();
    });
})