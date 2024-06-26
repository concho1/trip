// 이메일 인증
let eflag = false;
let flag = false;
let emailchk = false;

$('#id').on('keyup',function (){
    const id = $("#id").val();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

    if(emailPattern.test(id)){
        $('#idError').hide();
        $('#sendEmail').prop('disabled', false);
    }else{
        $('#idError').show();
        $('#sendEmail').prop('disabled', true);
    }

    $.ajax({
        url: 'checkDupId',
        type: 'post',
        data: {id: id},
        dataType: "json",
        success: function(result){
            console.log("받아온 값 : "+result)
            if(result){
                $('#idDupError').show();
                $('#sendEmail').prop('disabled', true);
            }else {
                $('#idDupError').hide();
                $('#sendEmail').prop('disabled', false);
            }
        },
        error: function(result){
            console.log(result);
            $('#sendEmail').prop('disabled', true);
            eflag = false;
        }
    });
});

$('#sendEmail').on('click', function() {
    const data = {
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

    $('#sendEmail').parent().parent().parent().after(input);
});

function al() {

    const code = ($('#checkCode').val());

    const data = {
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
                $('#id').prop('readonly', true).css('background-color', 'lightgray !important');
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