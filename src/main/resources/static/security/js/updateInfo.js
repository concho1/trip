/*
function pwOk() {
    const pw = $("#pw").val();
    const rePwd = $("#rePwd").val();

    if($('#pwdCheckError').is(':visible')){
        alert("비밀번호가 일치하지않습니다.");
        $("#rePwd").focus();
        return false;
    }else if(pw.length != rePwd.length){
        alert("비밀번호 확인이 비밀번호와 일치하지 않습니다.");
        $("#rePwd").focus();
        return false;
    }else{
        $('.needs-validation').submit();
        return true;
    }
}
*/

$(document).ready(function() {
    $('#modify').on('submit', function() {
        const lastName = $('#lastName').val();
        const firstName = $('#firstName').val();

        $('#name').val(lastName + ' ' + firstName);
    });

    $('#firstName').on('keyup',function () {
        const firstName = $("#firstName").val();
        const namePattern = /^[a-zA-Z]+$/;

        if(!namePattern.test(firstName)){
            $('#firstNameError').show();
        } else{
            $('#firstNameError').hide();
            $('#firstName').val(firstName.toUpperCase()); // 대문자로 변환하여 저장
        }
    });

    $('#lastName').on('keyup',function () {
        const lastName = $("#lastName").val();
        const namePattern = /^[a-zA-Z]+$/;

        if(!namePattern.test(lastName)){
            $('#lastNameError').show();
        } else{
            $('#lastNameError').hide();
            $('#lastName').val(lastName.toUpperCase()); // 대문자로 변환하여 저장
        }
    });

});

