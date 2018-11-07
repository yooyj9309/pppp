var page = 0;

//댓글 리스트 출력
function renderingReplyList(data) {
    console.log(data);
    var session = document.getElementById("sessionEmail").innerText;

    var replyHtml = '';
    for (var i in data) {
        replyHtml += '<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
        replyHtml += '<div class="commentInfo' + data[i].replyId + '">' + data[i].memberNick + '&nbsp;';

        if (session == data[i].sessionEmail && data[i].replyStatus != 2) {
            replyHtml += '<a type = "button" onclick="replyUpdateDisplay(' + data[i].replyId + ',\'' + data[i].replyContents + '\');"> 수정 </a>&nbsp;';
            replyHtml += '<a type = "button" onclick="replyDeleteByReplyId(' + data[i].replyId + ');"> 삭제 </a> &nbsp;';
        }
        replyHtml += '<a onclick="commentDisplay(' + data[i].replyId + ');"> 답글 </a> ';
        replyHtml += '</div><div id="commentContent' + data[i].replyId + '"> <p> ' + data[i].replyContents + '</p>';

        replyHtml += '<div style = "visibility: hidden;"id ="modifyReply' + data[i].replyId + '" class="input-group">';
        replyHtml += '<input type="text" class="form-control" name="content_' + data[i].replyId + '" value="' + data[i].replyContents + '"/>';
        replyHtml += '<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="replyUpdateByReplyId(' + data[i].replyId + ');">수정</button>';
        replyHtml += '<button class="btn btn-default" type="button" onclick="replyCancelDisplay(' + data[i].replyId + ');">취소</button> </span>';
        replyHtml += '</div>';

        replyHtml += '<div style = "visibility: hidden;"id ="addReply' + data[i].replyId + '" class="input-group">';
        replyHtml += '<input type="text" class="form-control" name="contentAdd_' + data[i].replyId + '" />';
        replyHtml += '<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="commentInsertByReplyId(' + data[i].replyId + ');">답글</button> ';
        replyHtml += '<button class="btn btn-default" type="button" onclick="commentCancelDisplay(' + data[i].replyId + ');">취소</button> </span>';
        replyHtml += '</div>';
        replyHtml += '<div id="commentList' + data[i].replyId + '">';

        for (var j in data[i].commentList) {
            replyHtml += '<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px; padding-left:20px;">';
            replyHtml += '<div class="commentInfo' + data[i].replyId + '">' + '답글 ' + data[i].commentList[j].memberNick + '&nbsp;';

            if (session == data[i].commentList[j].sessionEmail && data[i].commentList[j].replyStatus != 2) {
                replyHtml += '<a type = "button" onclick="replyUpdateDisplay(' + data[i].commentList[j].replyId + ',\'' + data[i].commentList[j].replyContents + '\');"> 수정 </a>&nbsp;';
                replyHtml += '<a type = "button" onclick="replyDeleteByReplyId(' + data[i].commentList[j].replyId + ');"> 삭제 </a> ';
            }
            replyHtml += '</div><div id="commentContent' + data[i].commentList[j].replyId + '"> <p> ' + data[i].commentList[j].replyContents + '</p>';

            replyHtml += '<div style = "visibility: hidden;"id ="modifyReply' + data[i].commentList[j].replyId + '" class="input-group">';
            replyHtml += '<input type="text" class="form-control" name="content_' + data[i].commentList[j].replyId + '" value="' + data[i].commentList[j].replyContents + '"/>';
            replyHtml += '<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="replyUpdateByReplyId(' + data[i].commentList[j].replyId + ');">수정</button> ';
            replyHtml += '<button class="btn btn-default" type="button" onclick="replyCancelDisplay(' + data[i].commentList[j].replyId + ');">취소</button> </span>';
            replyHtml += '</div>';
            replyHtml += '</div></div>';
        }
        replyHtml += '</div>'

        if (data[i].commentList != null && data[i].commentList.length == 3) {
            replyHtml += '<button onclick="commentMore(' + data[i].replyId + ');"> 더보기 </button> ';
        }
        replyHtml += '</div></div>';
    }
    return replyHtml;
}

function renderReply(data) {

    console.log(data);
    var session = document.getElementById("sessionEmail").innerText;
    var replyHTML = "";

    replyHTML += '<div class="media text-muted pt-3">';
    replyHTML += '<div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">';
    replyHTML += '<div>';
    replyHTML += '<strong class="text-gray-dark">사용자 닉네임</strong>';
    replyHTML += '<a type = "button" onclick="renderUpdateDisplay">수정</a>';
    replyHTML += '<a href="#">삭제</a>';
    replyHTML += '<a href="#">더보기</a>';
    replyHTML += '</div>';
    replyHTML += '<span class="d-block">댓글 내용</span>';
    replyHTML += '<span class="d-block">날짜</span>';
    replyHTML += '</div>';
    replyHTML += '</div>';

    return replyHTML;
}

function renderComment(data) {
    console.log(data);

    var commentHTML = "";

    commentHTML += '<div class="media text-muted pt-3">';
    commentHTML += '<p class="mr-2"  style="width: 32px; height: 32px;"> ▶ </p>';
    commentHTML += '<div class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">';
    commentHTML += '<div class="d-flex justify-content-between align-items-center w-100">';
    commentHTML += '<strong class="text-gray-dark">Full Name</strong>';
    commentHTML += '<a href="#">Follow</a>';
    commentHTML += '</div>';
    commentHTML += '<span class="d-block">@username</span>';
    commentHTML += '</div>';
    commentHTML += '</div>';

    return commentHTML;
}

function commentMore(replyId) {
    var session = document.getElementById("sessionEmail").innerText;
    $.ajax({
        type: "get",
        url: '/reply/comment?replyId=' + replyId,
        success: function (data) {
            console.log(data);

            var replyHtml = "";
            for (var i in data) {
                replyHtml += '<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px; padding-left:20px;">';
                replyHtml += '<div class="commentInfo' + data[i].replyId + '">' + ' 답글 ' + data[i].memberNick + '&nbsp;';

                if (session == data[i].sessionEmail && data[i].replyStatus != 2) {
                    replyHtml += '<a type = "button" onclick="replyUpdateDisplay(' + data[i].replyId + ',\'' + data[i].replyContents + '\');"> 수정 </a>&nbsp;';
                    replyHtml += '<a type = "button" onclick="replyDeleteByReplyId(' + data[i].replyId + ');"> 삭제 </a> ';
                }
                replyHtml += '</div><div id="commentContent' + data[i].replyId + '"> <p> ' + data[i].replyContents + '</p>';

                replyHtml += '<div style = "visibility: hidden;"id ="modifyReply' + data[i].replyId + '" class="input-group">';
                replyHtml += '<input type="text" class="form-control" name="content_' + data[i].replyId + '" value="' + data[i].replyContents + '"/>';
                replyHtml += '<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="replyUpdateByReplyId(' + data[i].replyId + ');">수정</button> ';
                replyHtml += '<button class="btn btn-default" type="button" onclick="replyCancelDisplay(' + data[i].replyId + ');">취소</button> </span>';
                replyHtml += '</div>';

                replyHtml += '</div></div>';
            }
            $("#commentList" + replyId).html(replyHtml);
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

//댓글 목록
function replyList() {
    var boardId = $("#paramBoardId").val();
    console.log(page);
    $.ajax({
        url: '/replies/?boardId=' + boardId + "&page=0",
        type: 'get',
        success: function (data) {
            var replyHTML = renderingReplyList(data);
            $("#replyList").append(replyHTML);
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

//댓글 더보기
$("#btnMoreReply").click(function () {
    var boardId = $("#paramBoardId").val();
    page = page + 1;
    console.log(page);
    $.ajax({
        type: "get",
        url: '/reply/list?boardId=' + boardId + "&page=" + page,
        success: function (data) {
            var replyHTML = renderingReplyList(data);
            $("#replyList").append(replyHTML);
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});

$("#btnReply").click(function () {
    var content = $("#replyContents").val();
    var param = $("#replyInsertForm").serialize();
    var boardId = $("#paramBoardId").val();
    if (content == "" || content == null) {
        alert("댓글을 입력해주세요.");
        return;
    }
    console.log(param);
    $.ajax({
        url: '/reply/?boardId=' + boardId,
        type: 'post',
        data: param,
        success: function () {
            alert("댓글 등록을 완료하였습니다.");
            location.href = "../main/" + boardId;
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});

//댓글 수정 - 댓글 내용 출력 보이게 하기
function replyUpdateDisplay(replyId) {
    $("#modifyReply" + replyId).css("visibility", "visible");
}

//댓글 수정
function replyUpdateByReplyId(replyId) {
    var updateContent = $('[name=content_' + replyId + ']').val();
    var boardId = $("#paramBoardId").val();
    $.ajax({
        url: '/reply',
        type: 'put',
        data: {'content': updateContent, 'replyId': replyId},
        success: function (data) {
            alert(data);
            location.href = "../main/" + boardId;
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

function replyCancelDisplay(replyId) {
    $("#modifyReply" + replyId).css("visibility", "hidden");
}

//댓글 삭제
function replyDeleteByReplyId(replyId) {
    var boardId = $("#paramBoardId").val();
    if (confirm("댓글을 삭제하시겠습니까?")) {
        $.ajax({
            url: '/reply/?replyId=' + replyId,
            type: 'delete',
            success: function (data) {
                alert(data);
                location.href = "../main/" + boardId;
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    }
}

function commentDisplay(replyId) {
    $("#addReply" + replyId).css("visibility", "visible");
}

function commentInsertByReplyId(replyId) {
    var commentContent = $('[name=contentAdd_' + replyId + ']').val();
    var boardId = $("#paramBoardId").val();
    $.ajax({
        url: '/reply/comment',
        type: 'post',
        data: {'content': commentContent, 'replyId': replyId},
        success: function (data) {
            alert(data);
            location.href = "../main/" + boardId;
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
}

function commentCancelDisplay(replyId) {
    $("#addReply" + replyId).css("visibility", "hidden");
}

$(document).ready(function () {
    replyList(); //페이지 로딩시 댓글 목록 출력
});
