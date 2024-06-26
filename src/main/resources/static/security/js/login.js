// 이메일 형식인지 확인
$('#id').on('keyup',function () {
    const id = $("#id").val();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

    if (emailPattern.test(id)) {
        $('#idError').hide();
    } else {
        $('#idError').show();
    }
});