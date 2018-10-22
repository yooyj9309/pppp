$(document).ready(function () {
    renderReplyList();

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
                renderReplyList();
                $("#replyContents").val('');
            },
            error: function (response) {
                alert(response.responseText);
            }
        });
    });
    function renderReplyList(){
        var boardId = $("#paramBoardId").val();
        $.ajax({
            url : '/reply/?boardId='+boardId,
            type : 'get',
            success : function(reply){
                console.log(reply);

                var replyHtml ='';
                for (var i in reply) {
                    replyHtml += '<div class="commentArea" style="border-bottom:1px solid darkgray; margin-bottom: 15px;">';
                    replyHtml += '<div> 작성자 : '+reply[i].memberNick;
                    replyHtml += '<a onclick="commentUpdate('+reply[i].replyId+',\''+'\');"> 수정 </a>';
                    replyHtml += '<a onclick="commentDelete('+reply[i].replyId+');"> 삭제 </a> </div>';
                    replyHtml += '<div> <p> 내용 : '+reply[i].replyContents +'</p>';
                    replyHtml += '</div></div>';
                }

                $("#replyList").html(replyHtml);
            }
        });
    }
});

