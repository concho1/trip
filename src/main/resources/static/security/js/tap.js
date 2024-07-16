$(document).ready(function() {
    // 상위 탭 메뉴 클릭 이벤트
    $('.tab-menu').click(function() {
        $('.tab-menu').removeClass('active');
        $(this).addClass('active');

        var tabId = $(this).attr('id').split('-')[1];
        $('.myedu-cont-wrap').hide();
        $('#' + tabId).show();

        // 하위 탭 메뉴를 항상 표시
        $('.sub-tab-menu-wrap').show();
    });

    // 하위 탭 메뉴 클릭 이벤트
    $('.sub-tab-menu').click(function() {
        $('.sub-tab-menu').removeClass('active');
        $(this).addClass('active');

        var subTabId = $(this).attr('id').split('-')[2];
        $('.myedu-area').hide();
        $('#' + subTabId).show();
    });

    // 페이지 로드 시 기본적으로 모든 예약과 다가오는 예약을 표시
    $('#tab-all').click();
    $('#sub-tab-upcoming').click();
});
