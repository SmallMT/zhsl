//提交控制
function checkAll(){

    var username=$("#username").val();
    var code=$("#code").val();
    var address=$("#address").val();

  /*  var photoFornt = document.getElementById('photoFornt')[0].value;
    var photoRear = document.getElementById('photoRear')[0].value;*/
    var photoFornt=$("#photoFornt").val();
    var photoRear=$("#photoRear").val();
    if(username.length==0){
        alert("请填写您的身份证姓名")
        return false;
    }
    if(code.length==0){
        alert("请填写您的身份证号码")
        return false;
    }
    if(address.length==0){
        alert("请填写您的身份证地址")
        return false;
    }
    if(photoFornt.length==0){
        alert("请上传您的身份证正面图")
        return false;
    }
    if(photoRear.length==0){
        alert("请上传您的身份证反面图")
        return false;
    }

    return true;
}