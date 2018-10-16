$(document).ready(function() {
    $("#btnWrite").click(function() {
        var formData = new FormData($("#writeForm")[0]);

        $.ajax({
            type : "post",
            url : "main",
            data : formData,
            processData : false,
            contentType : false,
            success : function(response) {
                alert(response);
                location.href = "/main";
            },
            error : function(response){
                alert(response.responseText);
            }
        });
    });
});
