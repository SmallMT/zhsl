

    function checkPhoneHasUser(phone) {
        $("#aftercheckphone").html("");
        //验证手机格式
        var reg=/^[1][3,4,5,7,8][0-9]{9}$/;
        if (!reg.test(phone)) {
            $("#aftercheckphone").html("请输入正确手机号码");
        }
        //检测该手机号码是否已经绑定了账号
        $.ajax({
            type:"get",
            url:"/user/checkphonehasused?phone="+phone,
            success:function (data) {
                if (data == false) {
                    $("#aftercheckphone").html("<font color='red'>该手机号并没有绑定账号</font>");
                }
            }
        });
    }
    
    function getCode() {
        var phone = $("#phone").val();
        if (phone == '') {
            $("#aftercheckphone").html("<font color='red'>请输入手机号</font>");
            return;
        }
        $.ajax({
            type:"get",
            url:"/user/checkphonehasused?phone="+phone,
            success:function (data) {
                if (data == false) {
                    $("#aftercheckphone").html("<font color='red'>该手机号没有绑定账号</font>");
                    return;
                }
            },
            error:function (data){
                return;
            }
        });
        $.ajax({
            type:"get",
            url:"/code/getcode?phone="+phone,
            success:function (data) {
                if (data == false) {
                    alert("此手机号没有绑定账号!");
                    return;
                }
                alert("发送成功");
            }
        });
    }

    function checkCode(code) {
        if (code == '') {
            $("#interfaceCode").html("<font color='red'>请输入验证码</font>");
            return;
        }
        var phone = $("#phone").val();
        $.ajax({
            type:"get",
            url:"/code/verifycode?phone="+phone+"&code="+code,
            success:function(){

            }
        });
    }