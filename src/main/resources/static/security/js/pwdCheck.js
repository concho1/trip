// 비밀번호 확인
function pwd() {
    const pw = $("#pw").val();
    const rePwd = $("#rePwd").val();

    if($('#pwdCheckError').is(':visible')){
        alert("비밀번호 확인이 비밀번호와 일치하지 않습니다.");
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

$(document).ready(function() {

    $('#pw').on('keyup',function (){
        const pw = $("#pw").val();
        const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

        if (!passwordPattern.test(pw)) {
            $('#pwdError').show();
        } else {
            $('#pwdError').hide();
        }
    });

    $('#rePwd').on('keyup',function () {
        const pw = $("#pw").val();
        const rePwd = $("#rePwd").val();

        if (pw != rePwd) {
            $('#pwdCheckError').show();
        } else {
            $('#pwdCheckError').hide();
        }
    });


});