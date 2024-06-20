$(document).ready(function() {
    $("#checkset-all").click(function() {
        if($("#checkset-all").is(":checked")) {
            $("input[name=check2], input[name=check3], input[name=check4], input[name=check5], input[name=sns]").prop("checked", true);
        } else {
            $("input[name=check2], input[name=check3], input[name=check4], input[name=check5], input[name=sns]").prop("checked", false);
        }
        checkAllSelected();
    });

    $("input[name=check2], input[name=check3], input[name=check4], input[name=check5], input[name=sns]").click(function() {
        checkAllSelected();
    });

    function checkAllSelected() {
        const total = $("input[name='check2'], input[name='check3'], input[name='check4'], input[name='check5'], input[name='sns']").length;
        const checked = $("input[name='check2']:checked, input[name='check3']:checked, input[name='check4']:checked, input[name='check5']:checked, input[name='sns']:checked").length;

        if (total != checked) {
            $("#checkset-all").prop("checked", false);
        } else {
            $("#checkset-all").prop("checked", true);
        }
    }
});

function check() {
    if (!$("input[name='check2']").is(":checked") ||
        !$("input[name='check3']").is(":checked") ||
        !$("input[name='check4']").is(":checked")) {
        alert("필수 항목에 전부 동의하셔야 합니다.");
        return false;
    } else {
        return true;
    }
}
