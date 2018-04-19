
var usernameFlag = false;
var pwdFlag = false;
var repwdFlag = false;
var phoneFlag = false;
//注册检测用户名
function checkUsername(){

    $("#usernameResult").html("");
    $("#aftercheckusername").html("");
    var username = $("#adminNo").val();
    if(username.length==0 || username == null){
        $("#usernameResult").html("<font color='red'>身份号码不能为空</font>");
        return;
    }
    var reg =/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;//必须有数字和字母组合5-10位
    if(!reg.test(username)){
        $("#usernameResult").html("<font color='red'><font>身份证不合法，请重新输入</font>");
        return;
    }
    $.ajax({
        type:'get',
        url:'/user/checkusername?username='+username,
        contentType:'application/json;charset=utf-8',
        success:function(data){
            if(data  == "身份证校验失败"){
                $("#usernameResult").html("<font color='red'>请输入正确的身份证号码</font>");
            }else if (data  == "true"){
                $("#usernameResult").html("<font color='red'>该身份号已注册</font>");
            }else {
                $("#usernameResult").html("<font color='green'><font>该账号可以使用</font>");
                usernameFlag = true;
            }
        }
    });
}
//注册用户名密码检测
function  checkPassword(pwd) {

    $("#aftercheckpassword").html("");
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
    pwdFlag = true;

}
//注册用户名密码确认
function recheckPwd(repwd) {
    var pwd = $('#password').val();
    if (repwd != pwd) {
        $('#repasswordResult').html("<font>两次密码不一致</font>");
        return;
    }
    repwdFlag = true;
}

//校验手机
function checkPhone(phone) {
    $("#aftercheckphone").html("");
    var reg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!reg.test(phone)) {
        return;
    }
    $.ajax({
        type:"get",
        url:"/user/checkphonehasused?phone="+phone,
        success:function(data){
            if (data == true){
                $("#phoneResult").html("<font color='red'>该手机号已经被使用</font>");
            }else {
                $("#phoneResult").html("<font color='green'>通过</font>");
            }
        }
    });
    phoneFlag = true;
}

//获取验证码
function getRegisterCode(obj) {
    var phone = $('#phone').val();
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!myreg.test(phone)) {
        $('#interfaceCode').html("<font>请填写正确的手机号</font>");
        return;
    }
    //获取手机验证码
    $.ajax({
        type:"get",
        url:"/user/getregistercode?phone="+phone,
        contentType:'application/json;charset=utf-8',
        success:function (data) {
            if (data == "true") {
                $('#interfaceCode').html("<font>获取验证码成功，请于规定时间内完成操作</font>");
                alert("获取验证码成功，请于规定时间内完成操作");
                settime(obj);
            }else if (data == "请填写正确的手机号"){
                $('#interfaceCode').html("<font>请填写正确的手机号</font>")
            }else {
                $('#interfaceCode').html("<font>flag</font>")
            }

        }
    })
}

//校验验证码
function checkCode(code) {
    $("#interfaceCode").html("");
    $("#aftercheckcode").html("");
    if (code.length != 6) {
        $("#interfaceCode").html("<font color='red'>请输入6位验证码</font>");
        return ;
    }
    
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