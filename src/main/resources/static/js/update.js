$(document).ready(function () {
    var boardId = $("#boardId").val();
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
            url: "../board",
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

    $("#btnUpdate").click(function () {
        var formData = new FormData($("#updateForm")[0]);
        var subject = $("#updateSubject").val();
        var content = $("#updateContent").val();

        console.log(boardId);

        if (subject == "" || subject == null) {
            alert("제목을 입력해주세요.");

        }
        if (content == "" || content == null) {
            alert("세부 사항을 입력 해주세요.");
        }

        $.ajax({
            type: "put",
            url: boardId,
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("게시물을 수정하였습니다.");
                location.href = "/board/" + boardId;
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });

    $("#btnDelete").click(function () {
        $.ajax({
            type: "delete",
            url: boardId,
            success: function () {
                alert("게시물을 삭제하였습니다.");
                location.href = "/main";
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });
});
