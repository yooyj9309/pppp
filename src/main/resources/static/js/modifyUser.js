$(document).ready(function () {
    $("#btnModifyNick").click(function () {
        var nickName = jQuery.trim($("#nickName").val());

        if (nickName == "" || nickName == null) {
            alert("닉네임을 입력해주세요.");
            return;
        }

        $.ajax({
            type: "put",
            url: "../nick_name",
            data: {'nickName' : nickName},
            success: function () {
                alert("닉네임이 변경되었습니다.");
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
                url: "../member_out",
                success: function () {
                    alert("회원 탈퇴하셨습니다.");
                    location.href = "/login"
                },
                error: function () {
                    alert("회원 탈퇴 실패");
                }
            });
        }
    });
});


