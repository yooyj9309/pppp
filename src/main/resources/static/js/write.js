$(document).ready(function () {
    getBoardList();

    $("#btnWrite").click(function () {
        var formData = new FormData($("#writeForm")[0]);

        var subject = jQuery.trim( $("#writeSubject").val());
        var content = jQuery.trim( $("#writeContent").val());

        if(subject == "" || subject ==null){
            alert("제목을 입력해주세요.");
            return;
        }

        if(content == "" || content ==null){
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
                getBoardList();
                document.getElementById("imgFile").value = "";
                document.getElementById("writeSubject").value = "";
                document.getElementById("writeContent").value = "";
            },
            error: function (response) {
                alert(response.responseText);
              //  $("#savePostsModal").hide();
            }
        });
    });
});


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
                output += " <a href=\"#\" class=\"btn btn-primary\">상세보기</a>";
                output += " <a href=\"#\" class=\"btn btn-primary\">" + result[i].likeCnt + "</a>";
                output += " </div>";
                output += " <div class=\"card-footer text-muted\">";
                output += " <label>" + result[i].memberNick + "</label> <label>" +result[i].boardRegDate + "</label>";
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
    date = new Date();
    year = date.getFullYear();
    month = date.getMonth();
    day = date.getDate();
    hour = date.getHours();
    minute = date.getMinutes();
    second = date.getSeconds();

    formatDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    return formatDate;
}