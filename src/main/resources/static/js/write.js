$(document).ready(function () {
    var page = 0;
    var lastBoard = -1; //어느 board까지 받았는지 확인 하는 변수
    var firstBoard = -1;

    getBoardList(page);

    $(window).scroll(function() {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
            console.log(++page);
            getBoardList(page);
        }
    });

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
                location.href="/main"
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });

    function getBoardList(page) {
        console.log(lastBoard);

            $.ajax({
                type: "get",
                url: "boardList/?page="+page+"&boardId="+lastBoard,
                success: function (result) {
                    console.log(result);
                    if(result.length!=0) {
                        var output = "";

                        for (var i in result) {
                            output += "<div class=\"card mb-4\">";
                            output += "<a href = \"main/" + result[i].boardId + "\">";
                            output += "<img class=\"card-img-top\" src=\"" + result[i].filePath + "\" alt=\"이미지 로딩 중...\">";
                            output += "</a>";
                            output += " <div class=\"card-body\">";
                            output += "<a href = \"main/" + result[i].boardId + "\">";
                            output += "<h2 class=\"card-title\">" + result[i].boardSubject + "</h2>";
                            output += "</a>";
                            output += "<p class=\"card-text\">" + result[i].boardContents + "</p>";
                            output += " <a href=\"" + result[i].boardId + "\" class=\"btn btn-primary\">상세보기</a>";
                            output += " <a href=\"#\" class=\"btn btn-primary\">" + result[i].likeCnt + "</a>";
                            output += " </div>";
                            output += " <div class=\"card-footer text-muted\">";
                            output += " <label>" + result[i].memberNick + "</label> <label>" + result.boardRegDate + "</label>";
                            output += " </div>";
                            output += " </div>";
                        }
                        $("#boardList").append(output);
                        lastBoard = result[result.length - 1].boardId;
                    }
                },
                error: function (response) {
                    alert(response.responseText);
                }
            });
    }
});

//처음 main page에 보여줄 10개의 리스트를 가져오는 함수

