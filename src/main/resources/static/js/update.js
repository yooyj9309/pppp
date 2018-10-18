 $(document).ready(function () {
     $("#btnUpdate").click(function() {
         var formData = new FormData($("#updateForm")[0]);
         var subject = $("#updateSubject").val();
         var content = $("#updateContent").val();
         var boardId = $("#boardId").val();

         console.log(boardId);

         if (subject == "" || subject == null) {
             alert("제목을 입력해주세요.");
             return;
         }
         if (content == "" || content == null) {
             alert("세부 사항을 입력 해주세요.");
             return;
         }

         $.ajax({
             type : "post",
             url : boardId,
             data : formData,
             processData : false,
             contentType : false,
             success : function(response) {
                 alert(response);
                 location.href="/main";
             },
             error : function(response) {
                 alert(response.responseText);
             }
         });
     });
 });
