
$("#btnReply").click(function () {
    var content = $("#replyContents").val();
    var param = $("#replyInsertForm").serialize();

    if (content == "" || content == null) {
        alert("댓글을 입력해주세요.");
        return;
    }
    console.log(param);
    $.ajax({
        url: '/reply',
        type: 'post',
        data: param,
        success: function (result) {
            alert(result);
            //commentList();
            $("#replyContents").val('');
        },
        error: function (response) {
            alert(response.responseText);
        }
    });
});

