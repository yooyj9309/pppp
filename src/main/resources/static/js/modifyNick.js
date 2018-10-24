$(document).ready(function () {
    $("#btnModifyNick").click(function () {
        var nickName = jQuery.trim($("#nickName").val());

        if (nickName == "" || nickName == null) {
            alert("제목을 입력해주세요.");
            return;
        }

        $.ajax({
            type: "post",
            url: "nick",
            data: {'nickName' : nickName},
            success: function (response) {
                alert(response);
                location.href = "/main"
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });
});


