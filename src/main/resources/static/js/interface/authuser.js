
function checkName(name) {
    $("#name").html("");
    $("#afterchecktruename").html("");
    if (name.length == 0) {
        $("#name").html("<font color='red'>请输入您位身份证姓名</font>");
        return;
    }
    $("#name").html("<font color='green'>正确</font>");
}

function checkTrueID(idcard) {
    $("#idcard").html("");
    $("#afterchecktrueid").html("");
    var reg =/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;//必须有数字和字母组合5-10位
    if (!reg.test(idcard)) {
        $("#idcard").html("<font color='red'>请输入正确的身份号码</font>")
        return;
    }
    $("#idcard").html("<font color='green'>正确</font>");
}
