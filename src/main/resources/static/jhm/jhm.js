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

    $('input[name=radioset-a]').change(function() {
        let oneWay = $('input[name=radioset-a]:checked').val();
        if(oneWay == "false") {
            $('#comb-field').show();
        }else if(oneWay == "true") {
            $('#comb-field').hide();
        }
    });

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

    $('#depDate').click(function() {
        $('#depCalendarContainer').toggle();
        $('#dep-calendar').focus();
    });

    $('#dep-calendar').attr('tabindex', '-1');

    $('#combDate').click(function() {
        $('#combCalendarContainer').toggle();
        $('#comb-calendar').focus();
    });

    $('#comb-calendar').attr('tabindex', '-1');

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
        let adults = $(this).closest('.card').find('#adults').val();
        let children = $(this).closest('.card').find('#children').val();
        let infants = $(this).closest('.card').find('#infants').val();
        let memberId = $(this).closest('.card').find('#memberId').val();

        if(memberId == "") {
            alert("회원가입 후 이용하실 수 있습니다.");
        }else {
            insertFfv(ffvData, ffvId, origin, des, dep, comb, memberId, adults, children, infants);
        }
    });

    function insertFfv(data, ffvId, origin, des, dep, comb, memberId, adults, children, infants) {
        $.ajax({
           type : 'post',
           url : 'insertFfv',
           data : { ffv : data,
                    ffvId : ffvId,
                    origin : origin,
                    des : des,
                    dep : dep,
                    comb : comb,
                    adults : adults,
                    children : children,
                    infants : infants,
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

    const date = new Date();
    let depCurrYear = date.getFullYear();
    let combCurrYear = date.getFullYear();
    let depCurrMonth = date.getMonth();
    let combCurrMonth = date.getMonth();
    const months = ['Jenuary', 'February', 'March',
        'April', 'May', 'June',
        'July', 'August', 'September',
        'October', 'November', 'December'];

    function renderDepCalendar() {
        $('#dep-currentDate').html(`${months[depCurrMonth]} ${depCurrYear}`);

        let lastDay = new Date(depCurrYear, depCurrMonth+1, 0).getDate();
        let first_yoil = new Date(depCurrYear, depCurrMonth, 1).getDay();
        let last_yoil = new Date(depCurrYear, depCurrMonth, lastDay).getDay();
        let lastDayofLastMonth = new Date(depCurrYear, depCurrMonth, 0).getDate();
        let dayTag = "";

        for(let i=first_yoil; i>0; i--) {
            dayTag += `<li class="inactive">${lastDayofLastMonth - i + 1}</li>`;
        }

        for(let i=1; i<= lastDay; i++) {

            let isToday = i === date.getDate() &&
            depCurrMonth === new Date().getMonth() &&
            depCurrYear === new Date().getFullYear()
                ? 'dep-active' : '';
            dayTag += `<li id="${isToday}">${i}</li>`
        }

        for(let i=last_yoil; i<6; i++) {
            dayTag += `<li class="inactive">${i - last_yoil + 1}</li>`;
        }

        $('#dep-days').html(dayTag);

        $('#dep-days li').on('click', function() {
           $('#dep-days li').removeAttr('id');
           $(this).attr('id', 'dep-active');
           let monVal = (depCurrMonth+1).toString().padStart(2, "0")
           let dayVal = `${depCurrYear}-${monVal}-${$(this).text().padStart(2, "0")}`;
           $('#depDate').val(dayVal);
           $('#depCalendarContainer').hide();
        });
    }

    $('#dep-shang').on('click', function() {
        if(depCurrMonth == 0) {
            depCurrMonth = 11;
            depCurrYear = depCurrYear - 1;
        }else {
            depCurrMonth = depCurrMonth - 1;
        }
        renderDepCalendar();
    });

    $('#dep-xia').on('click', function() {
        if(depCurrMonth == 11) {
            depCurrMonth = 0;
            depCurrYear = depCurrYear + 1;
        }else {
            depCurrMonth = depCurrMonth + 1;
        }
        renderDepCalendar();
    });

    function renderCombCalendar() {
        $('#comb-currentDate').html(`${months[combCurrMonth]} ${combCurrYear}`);

        let lastDay = new Date(combCurrYear, combCurrMonth+1, 0).getDate();
        let first_yoil = new Date(combCurrYear, combCurrMonth, 1).getDay();
        let last_yoil = new Date(combCurrYear, combCurrMonth, lastDay).getDay();
        let lastDayofLastMonth = new Date(combCurrYear, combCurrMonth, 0).getDate();
        let dayTag = "";

        for(let i=first_yoil; i>0; i--) {
            dayTag += `<li class="inactive">${lastDayofLastMonth - i + 1}</li>`;
        }

        for(let i=1; i<= lastDay; i++) {

            let isToday = i === date.getDate() &&
            combCurrMonth === new Date().getMonth() &&
            combCurrYear === new Date().getFullYear()
                ? 'comb-active' : '';
            dayTag += `<li id="${isToday}">${i}</li>`
        }

        for(let i=last_yoil; i<6; i++) {
            dayTag += `<li class="inactive">${i - last_yoil + 1}</li>`;
        }

        $('#comb-days').html(dayTag);

        $('#comb-days li').on('click', function() {
            $('#comb-days li').removeAttr('id');
            $(this).attr('id', 'comb-active');
            let monVal = (combCurrMonth+1).toString().padStart(2, "0")
            let dayVal = `${combCurrYear}-${monVal}-${$(this).text().padStart(2, "0")}`;
            $('#combDate').val(dayVal);
            $('#combCalendarContainer').hide();
        });
    }

    $('#comb-shang').on('click', function() {
        if(combCurrMonth == 0) {
            combCurrMonth = 11;
            combCurrYear = combCurrYear - 1;
        }else {
            combCurrMonth = combCurrMonth - 1;
        }
        renderCombCalendar();
    });

    $('#comb-xia').on('click', function() {
        if(combCurrMonth == 11) {
            combCurrMonth = 0;
            combCurrYear = combCurrYear + 1;
        }else {
            combCurrMonth = combCurrMonth + 1;
        }
        renderCombCalendar();
    });

    renderDepCalendar();
    renderCombCalendar();
})