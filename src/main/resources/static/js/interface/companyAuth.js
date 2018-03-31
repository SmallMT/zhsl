//提交控制
function checkAll(){

    var shxydm=$("#shxydm").val();
    var qymc=$("#qymc").val();
    var frxm=$("#frxm").val();
    var frsfzhm=$("#frsfzhm").val();
    var frdhhm=$("#frdhhm").val();
    var zcdz=$("#zcdz").val();
    var yyzz=$("#yyzz").val();

    if(shxydm.length==0){
        alert("信用代码不能为空")
        return false;
    }
    if(qymc.length==0){
        alert("企业名称不能为空")
        return false;
    }
    if(frxm.length==0){
        alert("法人真实姓名不能为空")
        return false;
    }
    if(frsfzhm.length==0){
        alert("法人身份证号不能为空")
        return false;
    }
    if(frdhhm.length==0){
        alert("法人电话号码不能为空")
        return false;
    }
    if(zcdz.length==0){
        alert("注册地址不能为空")
        return false;
    }
    if (yyzz == null || yyzz == "") {
        alert("请上传您的营业执照");
        return false;
    }else {
        //定义允许上传的文件类型
        var allow_ext = ".jpg|.jpeg|.png|.gif|.bmp|";
        //提取上传文件的类型
        var ext_name = file.substring(file.lastIndexOf("."));
        //alert(ext_name);
        //alert(ext_name + "|");
        //判断上传文件类型是否允许上传
        if (allow_ext.indexOf(ext_name + "|") == -1) {
            var errMsg = "该文件不允许上传，请上传" + allow_ext + "类型的文件,当前文件类型为：" + ext_name;
            alert(errMsg);
            return false;
        }
    }

    return true;
}