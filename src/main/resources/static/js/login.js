$('.tabs .tab').click(function () {
    if ($(this).hasClass('signin')) {
        $('.tabs .tab').removeClass('active');
        $(this).addClass('active');
        $('.cont').hide();
        $('.signin-cont').show();
    }
    if ($(this).hasClass('signup')) {
        $('.tabs .tab').removeClass('active');
        $(this).addClass('active');
        $('.cont').hide();
        $('.signup-cont').show();
    }
});
$('.container .bg').mousemove(
    function (e) {
        var amountMovedX = (e.pageX * -1 / 30);
        var amountMovedY = (e.pageY * -1 / 9);
        $(this).css('background-position',
            amountMovedX + 'px ' + amountMovedY + 'px');
    });

$(document).ready(function () {

    $("#loginBtn").click(function () {

        var userLoginId = jQuery.trim($("#userLoginId").val());
        var userLoginPw = $("#userLoginPw").val();
        var param = $("#loginForm").serialize();

        if (userLoginId == "" || userLoginId == null) {
            alert("ID를 입력해주세요.");
            return;
        }
        if (userLoginPw == "" || userLoginPw == null) {
            alert("비밀번호를 입력해주세요.");
            return;
        }

        $.ajax({
            type: "post",
            url: "/login",
            data: param,
            success: function () {
                alert("로그인에 성공하셨습니다.");
                location.href = "/main"
            },
            error: function (response) {
                console.log(response);
                alert(response.responseJSON.message);
            }
        });
    });

    $("#joinBtn").click(function () {

        var joinEmail = jQuery.trim($("#joinEmail").val());
        var joinNick = jQuery.trim($("#joinNick").val());
        var joinCheck = jQuery.trim($("#joinCheck").val());
        var joinPw = jQuery.trim($("#joinPw").val());

        var param = $("#joinForm").serialize();

        if (joinEmail == "" || joinEmail == null) {
            alert("Email을 입력해주세요.");
            return;
        }
        if (joinNick == "" || joinNick == null) {
            alert("Email을 입력해주세요.");
            return;
        }
        if (joinPw == "" || joinPw == null) {
            alert("비밀번호를 입력해주세요.");
            return;
        }
        if (joinCheck == "" || joinCheck == null) {
            alert("비밀번호 확인 칸을 입력해주세요.");
            return;
        }
        if (joinCheck != joinPw) {
            alert("비밀번호가 다릅니다.");
            return;
        }

        $.ajax({
            type: "post",
            url: "/signup",
            data: param,
            success: function () {
                alert("이메일 인증을 해주세요.");
                location.href = "/login";
            },
            error: function (response) {
                console.log(response);
                alert(response.responseText);
            }
        });

    });
});

