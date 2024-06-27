// 비밀번호 확인
function updatePwd() {
    const newPw = $("#newPw").val();
    const rePwd = $("#rePwd").val();

    if($('#pwdCheckError').is(':visible')){
        alert("비밀번호 확인이 일치하지않습니다.");
        $("#rePwd").focus();
        return false;
    }else if($('#pwdDupError').is(':visible')){
        alert("기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.");
        $("#newPw").focus();
        return false;
    }else if(newPw.length != rePwd.length){
        alert("비밀번호 확인이 비밀번호와 일치하지 않습니다.");
        $("#rePwd").focus();
        return false;
    }else{
        $('.needs-validation').submit();
        return true;
    }
}

function showTabFromHash() {
    const hash = location.hash || '#info';
    const tabs = document.querySelectorAll('.form-body');
    tabs.forEach(tab => {
        tab.style.display = tab.id === hash.substring(1) ? 'block' : 'none';
    });

    const tabMenus = document.querySelectorAll('.tab-menu');
    tabMenus.forEach(menu => {
        menu.classList.toggle('active', `#${menu.querySelector('a').getAttribute('href').substring(1)}` === hash);
    });
}

window.addEventListener('hashchange', showTabFromHash);
window.addEventListener('load', showTabFromHash);


$(document).ready(function() {

    $('#newPw').on('keyup',function (){
        const pw = $('#pw').val();
        const newPw = $("#newPw").val();
        const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

        if (!passwordPattern.test(newPw)) {
            $('#pwdError').show();
        } else {
            $('#pwdError').hide();
        }

        if (pw === newPw) {
            $('#pwdDupError').show();
            $('#updatePwdForm').prop('disabled', true);
        } else {
            $('#pwdDupError').hide();
            $('#updatePwdForm').prop('disabled', false);
        }
    });

    $('#rePwd').on('keyup',function () {
        const newPw = $("#newPw").val();
        const rePwd = $("#rePwd").val();

        if (newPw != rePwd) {
            $('#pwdCheckError').show();
        } else {
            $('#pwdCheckError').hide();
        }
    });


});