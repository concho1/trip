// 비밀번호 확인
function pwd() {
    const pw = $("#pw").val();
    const rePwd = $("#rePwd").val();

    if($('#pwdCheckError').is(':visible')){
        alert("비밀번호 확인이 일치하지않습니다.");
        $("#rePwd").focus();
        return false;
    }else if(pw.length != rePwd.length){
        alert("비밀번호 확인이 비밀번호와 일치하지 않습니다.");
        $("#rePwd").focus();
        return false;
    }else if($("#pwdDupError").is(':visible')){
        alert("이미 존재하는 이메일입니다.");
        return false;
    } else{
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

        $.ajax({
            url: 'checkDupPwd',
            type: 'post',
            data: {pw: pw},
            dataType: "json",
            success: function(result){
                console.log("받아온 값 : "+result)
                if(result){
                    $('#pwdDupError').show();
                    $('#pwdBtn').prop('disabled', true);
                }else {
                    $('#pwdDupError').hide();
                    $('#pwdBtn').prop('disabled', false);
                }
            },
            error: function(result){
                console.log(result);
                eflag = false;
            }
        });
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