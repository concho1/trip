// 이메일 인증
let eflag = false;
let flag = false;
let emailchk = false;
let email_regex = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;


$('#sendEmail').on('click', function(){
    var data = {
        email : $('#id').val()
    };

    if(eflag){
        alert('이메일을 보내는 중...');
        return;
    }else{
        eflag = true;
    }

    console.log(data);
    $.ajax({
        url: 'sendEmail',
        type: 'post',
        data: data,
        dataType: "json",
        success: [function(map){
            alert(map.message);
            console.log(map.message);
            eflag = false;
        }],
        error: function(resp){
            console.log(resp);
            eflag = false;
        }
    })

    if(flag){
        return;
    }else{
        flag = true;
    }

    let input = "";
    input += "<div class='col-12'>";
    input += "<label class='form-label'>인증번호 확인</label>"
    input += "<div class='input-group'>";
    input += "<input type='text' name='checkCode' class='form-control' id='checkCode'><br>";
    input += "<input type='button' value='인증번호 확인' class='btn btn-primary' onclick='al()' id='checkCodeButton' >";
    input += "</div>";
    input += "</div>";


    $('#sendEmail').after(input);
});

function al(){

    var code = ($('#checkCode').val());

    var data = {
        code: code
    };

    $.ajax({
        url: 'codeCatch',
        type: 'post',
        data: $.param(data),
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: "json",
        success: [function(map){
            if(map.good ==  null){
                alert(map.fail);
                $('#checkCode').focus();
            }else{
                alert(map.good);
                $('#checkCode').prop('readonly', true).css('background-color', 'lightgray');
                $('#checkCodeButton').prop('disabled', true).css('background-color', 'lightgray');
                $('#email').prop('readonly', true).css('background-color', 'lightgray !important');
                $('#sendEmail').prop('disabled', true).css('background-color', 'lightgray');
                eflag = false;
                emailchk = true;
            }
        }],
        error: function(resp){
            console.log(resp);
            eflag = false;
        }
    })
}