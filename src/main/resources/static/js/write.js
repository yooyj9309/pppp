$(document).ready(function () {
    $("#btnWrite").click(function () {
        var fileData = new FormData($("#fileForm")[0]);

        var subject = jQuery.trim($("#writeSubject").val());
        var content = jQuery.trim($("#writeContent").val());

        if (subject == "" || subject == null) {
            alert("제목을 입력해주세요.");
            return;
        }

        if (content == "" || content == null) {
            alert("세부사항을 입력해주세요.");
            return;
        }

        $.ajax({
            type: "post",
            url: "board",
            data: fileData,
            processData: false,
            contentType: false,
            success: function () {
                alert("게시물을 등록하였습니다.");
                location.href = "/main"
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });
});


