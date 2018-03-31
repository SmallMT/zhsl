
function checkCompanyCode(value) {
    $("#companycode").html("");
    $("#aftercheckcompanycode").html("");
    if (value.length == 0) {
        $("#companycode").html("<font color='red'>请填写统一社会信用代码</font>")
    }
}

function checkcompanyName(value) {
    $("#companyname").html("");
    $("#aftercheckcompanyname").html("");
    if (value.length == 0) {
        $("#companyname").html("<font color='red'>请填写企业名称</font>")
    }
}

function checkLegalName(value) {
    $("#legalpersonname").html("");
    $("#afterchecklegalpersonname").html("");
    if (value.length == 0) {
        $("#legalpersonname").html("<font color='red'>请填写法定代表人姓名</font>")
    }
}

function checkLegalID(value) {
    $("#legalpersonid").html("");
    $("#afterchecklegalpersonid").html("");
    if (value.length == 0) {
        $("#legalpersonid").html("<font color='red'>请填写法定代表人身份证号码</font>")
        return;
    }
    var reg =/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;//必须有数字和字母组合5-10位
    if (!reg.test(value)) {
        $("#legalpersonid").html("<font color='red'>请填写正确的身份证号码</font>")
    }
}

function checkLegalPhone(value) {
    $("#legalpersonphone").html("");
    $("#afterchecklegalpersonphone").html("");
    if (value.length == 0) {
        $("#legalpersonphone").html("<font color='red'>请填写法定代表人手机号</font>")
        return;
    }
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!myreg.test(value)) {
        $("#legalpersonphone").html("<font color='red'>请填写正确的手机号码</font>")
    }
}

function checkLegalCompanyAddress(value) {
    $("#companyaddress").html("");
    $("#aftercheckcompanyaddress").html("");
    if (value.length == 0) {
        $("#companyaddress").html("<font color='red'>请填写公司注册地址</font>")
        return;
    }
}


