function PlaneReservationCheck(){
    var firstname = $("#callFname").val();
    var lastname = $("#callLname").val();
    var phone = $("#phone").val();
    var email = $("#email").val();
    var isValid = true;

    if(firstname == ""){
        alert('영문 이름을 입력해주세요.');
        $('#FirstnameError').show();
        return false;
    }else if(lastname == ""){
        alert('영문 성을 입력해주세요.');
        $('#LastnameError').show();
        return false;
    }else if(phone == ""){
        alert('전화번호를 입력해주세요.');
        $('#phoneError').show();
        return false;
    }else if(email == ""){
        alert('이메일을 입력해주세요.');
        $('#emailError').show();
        return false;
    }else if(!$('#FirstnameEngError').is(':hidden')){
        alert('이름은 대,소문자 영어로 작성해주세요..');
        $('#FirstnameEngError').show();
        return false;
    }else if(!$('#LastnameEngError').is(':hidden')) {
        alert('이름은 대,소문자 영어로 작성해주세요..');
        $('#LastnameEngError').show();
        return false;
    }else if(!$('#phoneNumError').is(':hidden')){
        alert("'-'를 제외한 숫자만 입력해주세요.");
        $('#phoneNumError').show();
        return false;
    }else if(!$('#notEmailError').is(':hidden')){
        alert('올바른 이메일 형식으로 입력해주세요.');
        $('#notEmailError').show();
        return false;
    }

    $('input[name="birth"]').each(function(index) {
        const birthValue = $(this).val();
        const birthErrorId = "birthError"+index;

        if (birthValue === "") {
            alert((index + 1) +'번째 탑승객의 생년월일을 입력해주세요.');
            $('#' + birthErrorId).show();
            isValid = false;
        } else {
            $('#' + birthErrorId).hide();
        }
    });

    return isValid;
}

$('#callFname').on('keyup',function (){
    var firstname = $("#callFname").val();
    var nameTest = /^[a-zA-Z]+$/;

    if(firstname == ""){
        $('#FirstnameError').show();
    }else{
        $('#FirstnameError').hide();
    }
    if(nameTest.test(firstname)){
        $('#FirstnameEngError').hide();
    }else{
        $('#FirstnameEngError').show();
    }
});

$('#callLname').on('keyup',function (){
    var lastname = $("#callLname").val();
    var nameTest = /^[a-zA-Z]+$/;

    if(lastname == ""){
        $('#LastnameError').show();
    }else{
        $('#LastnameError').hide();
    }
    if(nameTest.test(lastname)){
        $('#LastnameEngError').hide();
    }else{
        $('#LastnameEngError').show();
    }
});

$('#phone').on('keyup',function (){
    var phone = $("#phone").val();
    var phoneTest = /^[0-9]*$/;

    if(phone == ""){
        $('#phoneError').show();
    }else{
        $('#phoneError').hide();
    }
    if(phoneTest.test(phone)){
        $('#phoneNumError').hide();
    }else{
        $('#phoneNumError').show();
    }
});

$('#email').on('keyup',function (){
    var email = $("#email").val();
    var emailTest = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

    if(email == ""){
        $('#emailError').show();
    }else{
        $('#emailError').hide();
    }
    if(emailTest.test(email)){
        $('#notEmailError').hide();
    }else{
        $('#notEmailError').show();
    }
});