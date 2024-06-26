let flag = false;
let eflag = false;
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

});

$('#sendEmail').on('click', function(){
    const id = $("#id").val();
    const data = {
        email: $('#id').val()
    };

    $.ajax({
        url: 'checkDupId',
        type: 'post',
        data: {id: id},
        dataType: "json",
        success: [function(result){
            if(!result){
                alert("일치하는 사용자를 찾을수없습니다.");
                return;
            }

            if(eflag){
                alert('이메일을 전송 중');
            }else{
                eflag = true;
                $.ajax({
                    url: 'sendPwdEmail',
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
            }

            if(flag){
                return;
            }else{
                flag = true;
            }

            /*let input = "";
            input += "<div class='tedu-inputset'>";
            input += "<label class='input-label' style='font-size: 13px; color: #828A8F;'>인증번호 확인</label>"
            input += "<div class='input-group'>";
            input += "<input type='text' name='checkCode' class='tedu-input lightmode' id='checkCode'><br>";
            input += "<button class='tedu-btn tedu-btn-dark btn-link btn-chk' id='checkCodeButton' type='button' onclick='al()'>인증번호 확인</button>";
            input += "</div>";
            input += "</div>";

            $('#sendEmail').parent().parent().after(input);*/
        }],
        error: function(resp){
            console.log(resp);

        }
    })

});


function al(){

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