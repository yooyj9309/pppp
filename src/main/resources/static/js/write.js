$(document).ready(function () {
    getBoardList();

    $("#btnWrite").click(function () {
        var formData = new FormData($("#writeForm")[0]);

        var subject = jQuery.trim( $("#writeSubject").val());
        var content = jQuery.trim( $("#writeContent").val());

        if(subject == "" || subject ==null){
            alert("제목을 입력해주세요.");
        }

        if(content == "" || content ==null){
            alert("제목을 입력해주세요.");
        }

        $.ajax({
            type: "post",
            url: "main",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("게시에 성공하였습니다.");
                getBoardList();
                //$("#savePostsModal").hide();
            },
            error: function (response) {
                alert(response.responseText);
              //  $("#savePostsModal").hide();
            }
        });
    });
});

function getBoardElement(result) {

    var output = "";
    output += "<div class=\"card mb-4\">";
    output += "<img class=\"card-img-top\" src=\"" + result[0].filePath + "\" alt=\"Card image cap\">";
    output += " <div class=\"card-body\">";
    output += "<h2 class=\"card-title\">" + result[0].boardSubject + "</h2>";
    output += "<p class=\"card-text\">" + result[0].boardContents + "</p>";
    output += " <a href=\"#\" class=\"btn btn-primary\">댓글 보기 (댓글 수) &rarr;</a>";
    output += " <a href=\"#\" class=\"btn btn-primary\">" + result[0].likeCnt + "</a>";
    output += " </div>";
    output += " <div class=\"card-footer text-muted\">";
    output += " <label>" + result[0].memberNick + "</label> <label>" + dateFormat(result[0].boardRegDate) + "</label>";
    output += " </div>";
    output += " </div>";

    $("#boardElement").html(output);

}

function getBoardList() {
    $.ajax({
        type: "get",
        url: "boardList",
        success: function (result) {
            console.log(result);
            var output = "";
            for (var i in result) {
                output += "<div class=\"card mb-4\">";
                output += "<img class=\"card-img-top\" src=\"" + result[i].filePath + "\" alt=\"Card image cap\">";
                output += " <div class=\"card-body\">";
                output += "<h2 class=\"card-title\">" + result[i].boardSubject + "</h2>";
                output += "<p class=\"card-text\">" + result[i].boardContents + "</p>";
                output += " <a href=\"#\" class=\"btn btn-primary\">댓글 보기 (댓글 수) &rarr;</a>";
                output += " <a href=\"#\" class=\"btn btn-primary\">" + result[i].likeCnt + "</a>";
                output += " </div>";
                output += " <div class=\"card-footer text-muted\">";
                output += " <label>" + result[i].memberNick + "</label> <label>" + dateFormat(result[i].boardRegDate) + "</label>";
                output += " </div>";
                output += " </div>";
            }
            $("#boardList").html(output);
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

function dateFormat(date) {
    date = new Date(parseInt(date));
    year = date.getFullYear();
    month = date.getMonth();
    day = date.getDate();
    hour = date.getHours();
    minute = date.getMinutes();
    second = date.getSeconds();

    formatDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    return formatDate;
}