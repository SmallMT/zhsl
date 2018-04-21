
    var flag = false;
    //检测用户名是否存在
    function checkUsernameIsExist(username){
        $("#usernameSPAN").html("");
        if (username == "") {
            $("#usernameSPAN").html("<font color='red'>请输入用户名</font>");
            return;
        }
        $.ajax({
            type:"get",
            url:"/user/usernameisexist?username="+username,
            success:function (data) {
                if (data == false) {
                    $("#usernameSPAN").html("<font color='red'>此用户名不存在</font>");
                }
            }
        });
    }

    //检测密码格式
    function checkPasswordFind(pwd){
        $("#passwordResult").html("");
        //检测密码是否符合要求
        if (pwd.length < 6 || pwd.length > 20) {
            $('#passwordResult').html("<font color='red'>密码6到20位</font>");
            return;
        }
        var reg=/^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,19}$/;
        if (!reg.test(pwd)) {
            $('#passwordResult').html("<font color='red'>必须有数字和字母组合6-20位</font>");
            return;
        }
    }

    //短信验证
    function checkCodeFind(code) {
        var phone = $("#phone").val();
        $.ajax({
            type:"get",
            url:"/code/verifycode?phone="+phone+"&code="+code,
            success:function(data){
                if (data == true) {
                    flag == true;
                }
            }
        })
    }

    function checkAllFind() {
        return flag;
    }