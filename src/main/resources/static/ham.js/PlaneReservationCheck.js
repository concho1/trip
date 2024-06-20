function PlaneReservationCheck(){
    var firstname = $("#callFname").val();
    var lastname = $("#callLname").val();
    var phone = $("#phone").val();
    var email = $("#email").val();

    if(firstname == ""){
        alert('영문 이름을 입력해주세요.');
        $('#FirstnameError').show();
    }else if(lastname == ""){
        alert('영문 성을 입력해주세요.');
        $('#LastnameError').show();
    }else if(phone == ""){
        alert('전화번호를 입력해주세요.');
        $('#phoneError').show();
    }else if(email == ""){
        alert('이메일을 입력해주세요.');
        $('#emailError').show();
    }

    if ($('#FirstnameError').is(':hidden') &&
        $('#FirstnameEngError').is(':hidden') &&
        $('#LastnameError').is(':hidden') &&
        $('#LastnameEngError').is(':hidden') &&
        $('#phoneError').is(':hidden') &&
        $('#phoneNumError').is(':hidden') &&
        $('#emailError').is(':hidden') &&
        $('#notEmailError').is(':hidden')) {
        return true;
    } else {
        alert('올바른 값들을 입력해주세요.')
        return false;
    }

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