function searchChangeTo() {
    let origin = document.getElementById("search-origin").value;
    document.getElementById("search-origin").value = document.getElementById("search-destination").value;
    document.getElementById("search-destination").value = origin;
}

$(document).ready(function() {
    let comeback = $('#comebackType').val();
    if(comeback === "") {
        $('#oneWay').prop('checked', true);
    }else {
        $('#round').prop('checked', true);
    }

    $('.search-value-button').click(function() {
        $('#flight-search-modal').show();
    })

    $('.close').click(function() {
        $(this).closest('.modal').hide();
    })

    $(window).on('click', function(event) {
        if ($(event.target).hasClass('modal')) {
            $(event.target).hide();
        }
    })

    let searchInput = $('#search-origin');
    let searchOrgModal = $("#searchOriginModal");

    let searchDes = $("#search-destination");
    let searchDesModal = $("#search-desModal");

    let searchPeopleModal = $("#searchPeopleModal");
    let searchPeo = $("#searchPeopleAndClass");

    function searchOriginAirport() {
        if (searchInput.val().trim() !== "") {
            $('#searchOriginModalBody').empty();
            $("#searchModalOrigin").val(searchInput.val());
            $.ajax({
                type : 'post',
                url : 'findAirport',
                data : { input : searchInput.val() },
                dataType : 'json',
                success : function(res) {

                    $('#searchOriginModalBody').empty();

                    $.each(res, function(i) {
                        let txt = "<span style='cursor: pointer' class='searchOriginAirportItem'>" + res[i].korName + '(' + res[i].iata + ')' + "</span>" + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                            "<span>" + res[i].korCountry + "&nbsp;&nbsp;&nbsp;" + res[i].korCity + "</span><br>";

                        $('#searchOriginModalBody').append(txt);

                    });

                    $('.searchOriginAirportItem').click(function() {
                        searchInput.val($(this).text());
                        $('#searchOriginModalBody').empty();
                        searchOrgModal.hide();
                    });
                },
                error : function() {
                    alert('공항 이름 로드 중 에러 발생');
                }
            })
        } else {
            searchOrgModal.hide();
        }
    }

    searchInput.on('input', function() {
        if(searchInput.val().trim() !== "") {
            searchOrgModal.show();
            searchOriginAirport();
        }else {
            searchOrgModal.hide();
        }
    });

    function searchDesAirport() {
        if (searchDes.val().trim() !== "") {
            $('#searchDesModalBody').empty();
            $("#searchModalDes").val(searchDes.val());
            $.ajax({
                type : 'post',
                url : 'findAirport',
                data : { input : searchDes.val() },
                dataType : 'json',
                success : function(res) {

                    $('#searchDesModalBody').empty();

                    $.each(res, function(i) {

                        let txt = "<span style='cursor: pointer' class='searchDesAirportItem'>" + res[i].korName + '(' + res[i].iata + ')' + "</span>" +"&nbsp;&nbsp;&nbsp;&nbsp;" +
                            "<span>" + res[i].korCountry + "&nbsp;&nbsp;&nbsp;" + res[i].korCity + "</span><br>";

                        $('#searchDesModalBody').append(txt);

                    });

                    $('.searchDesAirportItem').click(function() {
                        searchDes.val($(this).text());
                        $('#desModalBody').empty();
                        searchDesModal.hide();
                    });
                },
                error : function() {
                    alert('공항 이름 로드 중 에러 발생');
                }
            })
        } else {
            searchDesModal.hide();
        }
    }

    searchDes.on('input', function() {
        if (searchDes.val().trim() !== "") {
            searchDesModal.show();
            searchDesAirport();
        } else {
            searchDesModal.hide();
        }
    });

    searchPeo.on('click', function() {
        searchPeopleModal.toggle();
    });

    $('#searchDepDate').click(function() {
        $('#search-dep-wrapper').toggle();
        $('#search-dep-calendar').focus();
    });

    $('#search-dep-calendar').attr('tabindex', '-1');

    $('#searchCombDate').click(function() {
        $('#search-comb-wrapper').toggle();
        $('#search-comb-calendar').focus();
    });

    $('#search-comb-calendar').attr('tabindex', '-1');

    const searchDate = new Date();
    let searchDepCurrYear = searchDate.getFullYear();
    let searchCombCurrYear = searchDate.getFullYear();
    let searchDepCurrMonth = searchDate.getMonth();
    let searchCombCurrMonth = searchDate.getMonth();
    const searchMonths = ['Jenuary', 'February', 'March',
        'April', 'May', 'June',
        'July', 'August', 'September',
        'October', 'November', 'December'];

    function renderSearchDepCalendar() {
        $('#search-dep-currentDate').html(`${searchMonths[searchDepCurrMonth]} ${searchDepCurrYear}`);

        let lastDay = new Date(searchDepCurrYear, searchDepCurrMonth+1, 0).getDate();
        let first_yoil = new Date(searchDepCurrYear, searchDepCurrMonth, 1).getDay();
        let last_yoil = new Date(searchDepCurrYear, searchDepCurrMonth, lastDay).getDay();
        let lastDayofLastMonth = new Date(searchDepCurrYear, searchDepCurrMonth, 0).getDate();
        let dayTag = "";

        for(let i=first_yoil; i>0; i--) {
            dayTag += `<li class="inactive">${lastDayofLastMonth - i + 1}</li>`;
        }

        for(let i=1; i<= lastDay; i++) {

            let isToday = i === searchDate.getDate() &&
            searchDepCurrMonth === new Date().getMonth() &&
            searchDepCurrYear === new Date().getFullYear()
                ? 'search-dep-active' : '';
            dayTag += `<li id="${isToday}">${i}</li>`
        }

        for(let i=last_yoil; i<6; i++) {
            dayTag += `<li class="inactive">${i - last_yoil + 1}</li>`;
        }

        $('#search-dep-days').html(dayTag);

        $('#search-dep-days li').on('click', function() {
            if($(this).attr('class') != "inactive"){
                $('#search-dep-days li').removeAttr('id');
                $(this).attr('id', 'search-dep-active');
                let monVal = (searchDepCurrMonth+1).toString().padStart(2, "0")
                let dayVal = `${searchDepCurrYear}-${monVal}-${$(this).text().padStart(2, "0")}`;
                $('#searchDepDate').val(dayVal);
                $('#search-dep-wrapper').hide();
            }
        });
    }

    $('#search-dep-shang').on('click', function() {
        if(searchDepCurrMonth == 0) {
            searchDepCurrMonth = 11;
            searchDepCurrYear = searchDepCurrYear - 1;
        }else {
            searchDepCurrMonth = searchDepCurrMonth - 1;
        }
        renderSearchDepCalendar();
    });

    $('#search-dep-xia').on('click', function() {
        if(searchDepCurrMonth == 11) {
            searchDepCurrMonth = 0;
            searchDepCurrYear = searchDepCurrYear + 1;
        }else {
            searchDepCurrMonth = searchDepCurrMonth + 1;
        }
        renderSearchDepCalendar();
    });

    function renderSearchCombCalendar() {
        $('#search-comb-currentDate').html(`${searchMonths[searchCombCurrMonth]} ${searchCombCurrYear}`);

        let lastDay = new Date(searchCombCurrYear, searchCombCurrMonth+1, 0).getDate();
        let first_yoil = new Date(searchCombCurrYear, searchCombCurrMonth, 1).getDay();
        let last_yoil = new Date(searchCombCurrYear, searchCombCurrMonth, lastDay).getDay();
        let lastDayofLastMonth = new Date(searchCombCurrYear, searchCombCurrMonth, 0).getDate();
        let dayTag = "";

        for(let i=first_yoil; i>0; i--) {
            dayTag += `<li class="inactive">${lastDayofLastMonth - i + 1}</li>`;
        }

        for(let i=1; i<= lastDay; i++) {

            let isToday = i === searchDate.getDate() &&
            searchCombCurrMonth === new Date().getMonth() &&
            searchCombCurrYear === new Date().getFullYear()
                ? 'search-comb-active' : '';
            dayTag += `<li id="${isToday}">${i}</li>`
        }

        for(let i=last_yoil; i<6; i++) {
            dayTag += `<li class="inactive">${i - last_yoil + 1}</li>`;
        }

        $('#search-comb-days').html(dayTag);

        $('#search-comb-days li').on('click', function() {
            if($(this).attr("class") != "inactive") {
                $('#search-comb-days li').removeAttr('id');
                $(this).attr('id', 'search-comb-active');
                let monVal = (searchCombCurrMonth+1).toString().padStart(2, "0")
                let dayVal = `${searchCombCurrYear}-${monVal}-${$(this).text().padStart(2, "0")}`;
                $('#searchCombDate').val(dayVal);
                $('#search-comb-wrapper').hide();
            }
        });
    }

    $('#search-comb-shang').on('click', function() {
        if(searchCombCurrMonth == 0) {
            searchCombCurrMonth = 11;
            searchCombCurrYear = searchCombCurrYear - 1;
        }else {
            searchCombCurrMonth = searchCombCurrMonth - 1;
        }
        renderSearchCombCalendar();
    });

    $('#search-comb-xia').on('click', function() {
        if(searchCombCurrMonth == 11) {
            searchCombCurrMonth = 0;
            searchCombCurrYear = searchCombCurrYear + 1;
        }else {
            searchCombCurrMonth = searchCombCurrMonth + 1;
        }
        renderSearchCombCalendar();
    });

    let firstOneWay = $('input[name=search-radioset-a]:checked').val();

    if(firstOneWay == 'false') {
        $('#search-comb-field').show();
    }

    $('input[name=search-radioset-a]').change(function() {
        let oneWay = $('input[name=search-radioset-a]:checked').val();
        if(oneWay == "false") {
            $('#search-comb-field').show();
        }else if(oneWay == "true") {
            $('#search-comb-field').hide();
        }
    });

    let airplane = $('#search-airplane').val();
    let searchClass = "";
    let adults = $('#search-adults').val();
    let children = $('#search-children').val();
    let infants = $('#search-infants').val();
    let searchPac = "";

    $('select[name=adults]').val(adults);
    $('select[name=children]').val(children);
    $('select[name=infants]').val(infants);

    switch (airplane) {
        case "":
            $('#classNone').prop('checked', true);
            searchClass = "선택 안 함";
            break;
        case "ECONOMY":
            $('#economy').prop('checked', true);
            searchClass = "이코노미";
            break;
        case "PREMIUM_ECONOMY":
            $('#premium-economy').prop('checked', true);
            searchClass = "프리미엄 이코노미";
            break;
        case "BUSINESS":
            $('#business').prop('checked', true);
            searchClass = "비즈니스";
            break;
        case "FIRST":
            $('#first').prop('checked', true);
            searchClass = "퍼스트";
            break;
    }

    searchPac = "[";

    if(adults != 0) {
        searchPac = searchPac + "성인 : "+adults+"명 ";
    }
    if(children != 0) {
        searchPac = searchPac + "아동 : "+children+"명 ";
    }
    if(infants != 0) {
        searchPac = searchPac + "유아 : "+infants+"명"
    }
    searchPac = searchPac + "] & 좌석 : " + searchClass;

    searchPeo.val(searchPac);

    $('#searchPeopleSubmitBtn').on('click', function() {
        adults = $('select[name=adults]').val();
        children = $('select[name=children]').val();
        infants = $('select[name=infants]').val();
        searchClass = $('input[name=airplaneClass]:checked').next('span').text();

        searchPac = "인원 [ ";
        if(adults != 0) {
            searchPac = searchPac + "성인 : "+adults+"명 ";
        }
        if(children != 0) {
            searchPac = searchPac + "아동 : "+children+"명 ";
        }
        if(infants != 0) {
            searchPac = searchPac + "유아 : "+infants+"명"
        }
        searchPac = searchPac + "] & 좌석 : " + searchClass;

        searchPeo.val(searchPac);

        searchPeo.css('font-size', '15px');

        searchPeopleModal.hide();
    });

    $('#researchBtn').on('click', function() {
        $('#re-span').hide();
        $(this).css('background-color', 'white');
        $('#re-spinner').show();
    });


    renderSearchDepCalendar();
    renderSearchCombCalendar();
})