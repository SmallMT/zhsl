$(document).ready(function(){

    //密码的检测
    $("#password").blur(function(){
        var password=$("#password").val();

        var reg=/^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,19}$/;//必须有数字和字母组合6-20位

        if(password.length==0){
            $("#passwordResult").html("<font color='red'>密码不能为空</font>");
            return;
        }else{
            $("#passwordResult").html("<font color='green'>通过</font>");
        }

        if(!reg.test(password)){
            $("#passwordResult").html("<font color='red'>必须有数字和字母组合6-20位</font>");
            return;
        }else{
            $("#passwordResult").html("<font color='green'>通过</font>");
        }


    });

    //确认密码
    $("#rePassword").blur(function(){

        var password=$("#password").val();
        var oldpassword=$("#rePassword").val();

        if(oldpassword==password){
            if(oldpassword.length!=0){
                $("#repasswordResult").html("<font color='green'>通过</font>");
            }else {
                $("#repasswordResult").html("<font color='red'>确认密码不能为空</font>");
            }
        }else{
            $("#repasswordResult").html("<font color='red'>两次密码不一致</font>");
        }
    });

    //电话验证
    $("#phone").blur(function(){

        var telephone=$("#phone").val();
        var reg=/^[1][3,4,5,7,8][0-9]{9}$/;

        if(telephone.length==0){
            $("#phoneResult").html("<font color='red'>电话号码不能为空</font>");
            return;
        }else{
            $("#phoneResult").html("<font color='green'>通过</font>");
        }

        if(!reg.test(telephone)){
            $("#phoneResult").html("<font color='red'>请输入正确手机号</font>");
            return;
        }else{
            $("#phoneResult").html("<font color='green'>通过</font>");
        }
    });

});
//获取手机短信
function getcode(obj) {
    var phone = $("#phone").val();
    if(phone.length==0){
        $("#interfaceCode").css("color","red").html("手机号码不能为空");
        return;
    }
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if(!myreg.test(phone)){
        $("#interfaceCode").css("color","red").html("请输入有效手机号");
        return false;
    }
    $.ajax({
        type:"post",
        url:"/ouser/ogetcode.html",
        data:phone,
        dataType:"json",
        success:function (data) {
            if (data.error) {
                $("#interfaceCode").css("color","red").html("获取验证码失败，请重新获取");
            }else{
                $("#interfaceCode").css("color","green").html("获取验证码成功，请于规定时间内完成操作");
                alert("获取验证码成功，请于规定时间内完成操作");
                settime(obj);//开始倒计时
            }
        },
        error:function (result) {
        }
    });
    // $.ajax({
    //     type:'post',
    //     url:'/ouser/oregister.html',
    //     contentType:'application/json;charset=utf-8',
    //     dataType: 'text',
    //     data:phone,
    //     success:function(data){
    //         if(data=='200'){
    //             $("#interfaceCode").css("color","green").html("获取验证码成功，请于规定时间内完成操作");
    //             // alert("获取验证码成功，请于规定时间内完成操作");
    //             settime(obj);//开始倒计时
    //         }else if(data='400'){
    //             $("#interfaceCode").css("color","red").html("您的手机验证限数已达到一天6次上限");
    //         }else {
    //             $("#interfaceCode").css("color","red").html("获取验证码失败，请重新获取");
    //         }
    //     }
    // });
}
var countdown=60;
function settime(obj) {
    if (countdown == 0){
        obj.removeAttribute("disabled");
        obj.value="免费获取验证码";
        countdown = 60;
        return;
    } else {
        obj.setAttribute("disabled", true);
        obj.value="重发(" + countdown + ")";
        countdown--;
    }
    setTimeout(function() {
            settime(obj)}
        ,1000)
}
//短信验证
function authCode(obj) {
    var phone = $("#phone").val();
    if(phone.length==0){
        $("#interfaceCode").html.css("color","red").html("手机号码不能为空");
        // alert('手机号码不能为空')
        return false;
    }
    if(obj.value.length==0){
        return false;
    }
    $.ajax({
        type:'post',
       url:'../code/authCodeJson.html',
        contentType:'application/json;charset=utf-8',
        dataType: 'json',
        data:"{phone:'" + phone + "',code:'" + obj.value + "'}",
        success:function(data){
            if(data==true){
                $("#interfaceCode").html("<font color='green'>验证通过</font>");
            }else {
                $("#interfaceCode").html("<font color='red'>验证失败</font>");
            }
        }
    });
}
//提交控制
function checkAll(){
    var username=$("#adminNo").val();
    var code=$("#code").val();
    var phone=$("#phone").val();
    var interfaceCode=$("#interfaceCode").text();
    var password=$("#password").val();
    var oldpassword=$("#rePassword").val();

    if(username.length==0){
        alert("账号不能空");
        return false;
    }
    if(oldpassword.length!=0&&password.length!=0){
        if(oldpassword!=password){
            alert("密码不一致")
            return false;
        }
    }else {
        alert("两次密码不能为空")
        return false;
    }
    if(phone.length==0){
        alert("必须验证手机")
        return false;
    }
    if(code.length==0){
        alert("请输入短信验证码")
        return false;
    }
    if(interfaceCode=='验证失败'||interfaceCode==''){
        alert("验证码失败，请重新验证")
        return false;
    }
    return true;
}
