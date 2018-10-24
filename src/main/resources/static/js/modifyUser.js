$(document).ready(function () {
    $("#btnModifyNick").click(function () {
        var nickName = jQuery.trim($("#nickName").val());

        if (nickName == "" || nickName == null) {
            alert("닉네임을 입력해주세요.");
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
    $("#btnDeleteMember").click(function () {
        if(confirm("정말 탈퇴하시겠습니까?")){
            $.ajax({
                type: "delete",
                url: "withdraw",
                success: function (response) {
                    alert(response);
                    location.href = "/login"
                },
                error: function (response) {
                    alert(response.responseText);
                }
            });
        }
    });
});


