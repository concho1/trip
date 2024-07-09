function PlaneReservationCheck(){
    var firstname = $("#callFirstName").val();
    var lastname = $("#callLastName").val();
    var phone = $("#callNumber").val();
    var email = $("#callEmail").val();

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
    var isValid = true;

    $('input[name="rideBirth"]').each(function(index) {
        const birthValue = $(this).val();
        const birthErrorId = "birthError"+index;

        if (birthValue === "") {
            alert((index + 1) +'번째 탑승객의 생년월일을 입력해주세요.');
            $('#' + birthErrorId).show();
            isValid = false;
            return false;
        } else {
            $('#' + birthErrorId).hide();
        }
    });

    if(!isValid){
        return false;
    }

    $('input[name="ridePassportExdate"]').each(function(index) {
        const passPortDateValue = $(this).val();
        const passPortDateErrorId = "passDateError"+index;

        if (passPortDateValue === "") {
            alert((index + 1) +'번째 탑승객의 여권만료일을 입력해주세요.');
            $('#' + passPortDateErrorId).show();
            isValid = false;
            return false;
        } else {
            $('#' + passPortDateErrorId).hide();
        }
    });

    return isValid
}


$('#callFirstName').on('keyup',function (){
    var firstname = $("#callFirstName").val();
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

$('#callLastName').on('keyup',function (){
    var lastname = $("#callLastName").val();
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

$('#callNumber').on('keyup',function (){
    var phone = $("#callNumber").val();
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

$('#callEmail').on('keyup',function (){
    var email = $("#callEmail").val();
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

$(document).ready(function() {
    $('input[name="rideFirstName"]').on('keyup', function() {
        var $this = $(this);
        var index = $this.index('input[name="rideFirstName"]');
        var isValid = true;

        var rideFirstNameValue = $this.val();
        var rideFirstnameError = '#rideFirstnameError' + index;
        var rideFirstnameEngError = '#rideFirstnameEngError' + index;

        if (rideFirstNameValue === "") {
            $(rideFirstnameError).show();
            isValid = false;
        } else {
            $(rideFirstnameError).hide();

            var isEnglish = /^[A-Za-z]+$/.test(rideFirstNameValue);
            if (!isEnglish) {
                $(rideFirstnameEngError).show();
                isValid = false;
            } else {
                $(rideFirstnameEngError).hide();
            }
        }
        return isValid;
    });
});

$(document).ready(function() {
    $('input[name="rideLastName"]').on('keyup', function() {
        var $this = $(this);
        var index = $this.index('input[name="rideLastName"]');
        var isValid = true;

        var rideLastName = $this.val();
        var rideLastnameError = '#rideLastnameError' + index;
        var rideLastnameEngError = '#rideLastnameEngError' + index;

        if (rideLastName === "") {
            $(rideLastnameError).show();
            isValid = false;
        } else {
            $(rideLastnameError).hide();

            var isEnglish = /^[A-Za-z]+$/.test(rideLastName);
            if (!isEnglish) {
                $(rideLastnameEngError).show();
                isValid = false;
            } else {
                $(rideLastnameEngError).hide();
            }
        }
        return isValid;
    });
});

$(document).ready(function() {
    $('input[name="ridePassport"]').on('keyup', function() {
        var $this = $(this);
        var index = $this.index('input[name="ridePassport"]');
        var isValid = true;

        var ridePassport = $this.val();
        var ridePassportError = '#ridePassportError' + index;
        var ridePassportFormError = '#ridePassportFormError' + index;

        if (ridePassport === "") {
            $(ridePassportError).show();
            isValid = false;
        } else {
            $(ridePassportError).hide();

            var passPortForm =/^[A-Za-z][0-9]{7}$/.test(ridePassport);
            if (!passPortForm) {
                $(ridePassportFormError).show();
                isValid = false;
            } else {
                $(ridePassportFormError).hide();
            }
        }
        return isValid;
    });
});