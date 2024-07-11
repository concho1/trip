$(document).ready(function() {
    $('#modify').on('submit', function() {
        const lastName = $('#lastName').val();
        const firstName = $('#firstName').val();

        $('#name').val(lastName + ' ' + firstName);
    });

    $('#firstName').on('keyup',function () {
        const firstName = $("#firstName").val();
        const namePattern = /^[a-zA-Z]+$/;

        if(!namePattern.test(firstName)){
            $('#firstNameError').show();
        } else{
            $('#firstNameError').hide();
            $('#firstName').val(firstName.toUpperCase()); // 대문자로 변환하여 저장
        }
    });

    $('#lastName').on('keyup',function () {
        const lastName = $("#lastName").val();
        const namePattern = /^[a-zA-Z]+$/;

        if(!namePattern.test(lastName)){
            $('#lastNameError').show();
        } else{
            $('#lastNameError').hide();
            $('#lastName').val(lastName.toUpperCase()); // 대문자로 변환하여 저장
        }
    });

});

