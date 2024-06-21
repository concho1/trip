/*function login() {

}*/

/*
$('#id').on('keyup',function () {
    const id = $("#id").val();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

    if (emailPattern.test(id)) {
        $('#idError').hide();
    } else {
        $('#idError').show();
    }
});
*/

$(document).ready(function() {
    $("#checkset-c-3-1").click(function() {
        /*if($("#checkset-all").is(":checked")) {
            $("input[name=check2], input[name=check3], input[name=check4], input[name=check5], input[name=sns]").prop("checked", true);
        } else {
            $("input[name=check2], input[name=check3], input[name=check4], input[name=check5], input[name=sns]").prop("checked", false);
        }
        checkAllSelected();*/
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
