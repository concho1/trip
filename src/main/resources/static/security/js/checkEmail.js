// 이메일 인증
let eflag = false;
let flag = false;
let emailchk = false;

$('#id').on('keyup',function (){
    const id = $("#id").val();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

    if(!emailPattern.test(id)){
        $('#idError').hide();
    }else{
        $('#idError').show();
        $('#sendEmail').prop('disabled', true);
    }
});

$('#sendEmail').on('click', function() {
    var data = {
        email: $('#id').val()
    };

    console.log(data);
    $.ajax({
        url: 'sendEmail',
        type: 'post',
        data: data,
        dataType: "json",
        success: [function (map) {
            alert(map.message);
            console.log(map.message);
            eflag = false;
        }],
        error: function (resp) {
            console.log(resp);
            eflag = false;
        }
    })

    if (flag) {
        return;
    } else {
        flag = true;
    }

    let input = "";
    input += "<fieldset class='fieldset'>";
    input += "<legend class='legend'>인증번호 확인</legend>"
    input += "<div class='input-group flex-style'>";
    input += "<div class='tedu-inputset input-group'>";
    input += "<input type='text' name='checkCode' class='tedu-input lightmode' id='checkCode'><br>";
    input += "<button class='tedu-btn tedu-btn-dark btn-link btn-chk' id='checkCodeButton' type='button' onclick='al()'>인증번호 확인</button>";
    input += "</div>";
    input += "</div>";
    input += "</fieldset>";


    $('#sendEmail').after(input);
});

function al() {

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