 $(document).ready(function () {
        var session = /*[[${#httpServletRequest.remoteUser}]]*/;
        var memberEmail = /*[[${board.memberEmail}]]*/

        if (session != memberEmail) {
            $("#updateButton").hide();
            $("#deleteButton").hide();
        }
        console.log(session + " " + memberEmail);
    });
