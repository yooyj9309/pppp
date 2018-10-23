

//댓글 목록
function commentList(){
    var boardId = $("#paramBoardId").val();
    var session = document.getElementById("sessionEmail").innerText;
    $.ajax({
        url : '/reply/list?boardId='+boardId,
        type : 'get',
        success : function(data){
            var replyHtml ='';
            for(var i in data){
                replyHtml += '<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
                replyHtml += '<div class="commentInfo'+data[i].replyId+'">'+'댓글번호 : '+data[i].replyId+' / 작성자 : '+data[i].memberNick;
                if(session == data[i].sessionEmail) {
                    replyHtml += '<a onclick="commentUpdate(' + data[i].replyId + ',\'' + data[i].replyContents + '\');"> 수정 </a>';
                    replyHtml += '<a onclick="commentDelete(' + data[i].replyId + ');"> 삭제 </a> ';
                }
                replyHtml += '</div><div class="commentContent'+data[i].replyId+'"> <p> 내용 : '+data[i].replyContents +'</p>';
                replyHtml += '</div></div>';
            }

            $("#replyList").html(replyHtml);
        }
    });
}

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
        url: '/reply/?boardId='+boardId,
        type: 'post',
        data: param,
        success: function (result) {
            alert(result);
            commentList();
            $("#replyContents").val('');
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});

//댓글 수정 - 댓글 내용 출력을 input 폼으로 변경
function commentUpdate(replyId, content){
    var updateHTML ='';

    updateHTML += '<div class="input-group">';
    updateHTML += '<input type="text" class="form-control" name="content_'+replyId+'" value="'+content+'"/>';

    updateHTML += '<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="commentUpdateProc('+replyId+');">수정</button> </span>';
    updateHTML += '</div>';

    $('.commentContent'+replyId).html(updateHTML);

}

//댓글 수정
function commentUpdateProc(replyId){
    var updateContent = $('[name=content_'+replyId+']').val();

    $.ajax({
        url : '/reply',
        type : 'put',
        data : {'content' : updateContent, 'replyId' : replyId},
        success : function(data){
            alert(data);
            commentList(); //댓글 수정후 목록 출력
        }
    });
}

//댓글 삭제
function commentDelete(replyId){
    $.ajax({
        url: '/reply/?replyId='+replyId,
        type : 'delete',
        success : function(data){
            alert(data);
            commentList();
        }
    });
}




$(document).ready(function(){
    commentList(); //페이지 로딩시 댓글 목록 출력
});
