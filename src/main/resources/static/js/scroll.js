$(document).ready(function () {
    var lastBoard = -1; //어느 board까지 받았는지 확인 하는 변수
    var firstBoard = -1; //맨위 board가 무엇인지 확인 하는 변수
    var check = true;

    getBoardList();

    $(window).scroll(function () {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {
            getBoardList();
        }
        else if ($(window).scrollTop() == 0) {
            console.log("data 가져오기");
            if (firstBoard != -1) {
                getUpdatedBoardList();
            }
        }
    });

    function getUpdatedBoardList() {
        $.ajax({
            type: "get",
            url: "boardList/?boardId=" + firstBoard + "&type=up",
            success: function (result) {
                console.log(result);
                if (result.length != 0) {
                    var render = renderingBoard(result);

                    $("#boardList").prepend(render);
                    firstBoard = result[0].boardId;
                }
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    }

    function getBoardList() {
        console.log(lastBoard);
        $.ajax({
            type: "get",
            url: "boardList/?boardId=" + lastBoard + "&type=down",
            success: function (result) {
                console.log(result);
                if (check) {
                    firstBoard = result[0].boardId;
                    check = false;
                }
                if (result.length != 0) {
                    var render = renderingBoard(result);

                    $("#boardList").append(render);
                    lastBoard = result[result.length - 1].boardId;
                }
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    }


    function renderingBoard(result) {
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
            output += " <a href=\"/main/" + result[i].boardId + "\" class=\"btn btn-primary\">상세보기 [" + result[i].viewCnt + "]</a>&nbsp;&nbsp;";
            output += '<a onclick="likeProcess(' + result[i].boardId + ',' + result[i].likeStatus + ');">';

            console.log(result[i].likeStatus);

            if (result[i].likeStatus == 0) {
                output += '<img src = "images/noheart.png" id="like' + result[i].boardId + '">';
            }else{
                output += '<img src = "images/heart.png" id="like' + result[i].boardId + '">';
            }

            output += '</a> <label id="likeCnt' + result[i].boardId + '">' + result[i].likeCnt + '</label>';
            output += " </div>";
            output += " <div class=\"card-footer text-muted\">";

            if (result[i].boardStatus == 0) {
                output += " <label>" + "게시자 : " + result[i].memberNick + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</label> <label>" + "작성일 : " + timeFormat(result[i].boardModDate) + "</label>";
            } else if (result[i].boardStatus == 1) {
                output += " <label>" + "게시자 : " + result[i].memberNick + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</label> <label>" + "수정일 : " + timeFormat(result[i].boardModDate) + "</label>";
            }

            output += ' <label>댓글 수 : ' + result[i].commentCnt + '</label>';
            output += " </div>";
            output += " </div>";
        }
        return output;
    }

    function timeFormat(date) {
        console.log(new Date(date));

        date = new Date(date);
        var strDate = date + "";
        var boardDate = strDate.split(' ');
        return boardDate[0] + " " + boardDate[1] + " " + boardDate[2] + " " + boardDate[3] + " " + boardDate[4];
    }
});

function likeProcess(boardId, likeStatus) {
    console.log(boardId+" "+likeStatus);
    var likeCnt = document.getElementById("likeCnt" + boardId).innerText;

    $.ajax({
        type: "post",
        url: "main/like?boardId="+boardId,
        success: function (result) {
            console.log(result);
            switch (result) {
                case 0: //좋아요를 누른 후 상태 값
                    $("#likeCnt" + boardId).text(parseInt(likeCnt) + 1);
                    $("#like"+boardId).prop("src","images/heart.png");
                    break;
                case 1:// 좋아요 취소를 누른 후 상태 값
                    $("#likeCnt" + boardId).text(parseInt(likeCnt) - 1);
                    $("#like"+boardId).prop("src","images/noheart.png");
                    break;
                default:
                    alert("잘못된 접근입니다.");
                    break;
            }
        },
        error: function (response) {
            alert(response.responseText);
        }
    });

}