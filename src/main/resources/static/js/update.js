 $(document).ready(function () {
     var boardId = $("#boardId").val();
     $("#btnWrite").click(function () {
         var formData = new FormData($("#writeForm")[0]);

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
             url: "../main",
             data: formData,
             processData: false,
             contentType: false,
             success: function (response) {
                 alert(response);
                 location.href = "../main"
             },
             error: function (response) {
                 alert(response.responseText);
             }
         });
     });
     $("#btnUpdate").click(function() {
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
             type : "post",
             url : boardId,
             data : formData,
             processData : false,
             contentType : false,
             success : function(response) {
                 alert(response);
                 location.href="/main/"+boardId;
             },
             error : function(response) {
                 alert(response.responseText);
             }
         });
     });

     $("#deleteButton").click(function() {
        if(confirm("게시글을 삭제하시겠습니까?")){
            $.ajax({
                type : "delete",
                url : boardId,
                success : function(response) {
                    alert(response);
                    location.href="/main";
                },
                error : function(response) {
                    alert(response.responseText);
                }
            });
        }
     });
 });
