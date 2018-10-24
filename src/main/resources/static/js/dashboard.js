$(document).ready(function () {
    $("#btnWrite").click(function () {
        var formData = new FormData($("#writeForm")[0]);

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
            url: "main",
            data: formData,
            processData: false,
            contentType: false,
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


