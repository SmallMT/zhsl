$(function(){
    $(".moren").click(function(){
        $(".morem_box").find('label input').val('');
        $(".morem_box").hide();

    });
    $(".zidingyi").click(function(){
        $(".morem_box").show();
    });
    $(".ziqu").click(function(){
        $(".toidifangshi").find('label input').val('');
        $(".toidifangshi").hide();

    });
    $(".toudi").click(function(){
        $(".toidifangshi").show();
    });
    $('.cfCheckBox').attr("disabled","disabled");
    $(".shiyou").click(function(){
        $('.huisun').next('.CheckBoxs').find('.cfCheckBox').removeAttr("checked");
        $('.huisun').next('.CheckBoxs').find('.cfCheckBox').attr("disabled","disabled");
        $(this).next('.CheckBoxs').find('.cfCheckBox').removeAttr("disabled","disabled");

    });
    $(".huisun").click(function(){
        $('.shiyou').next('.CheckBoxs').find('.cfCheckBox').removeAttr("checked");
        $('.shiyou').next('.CheckBoxs').find('.cfCheckBox').attr("disabled","disabled");
        $(this).next('.CheckBoxs').find('.cfCheckBox').removeAttr("disabled","disabled");
    })
    $(".CustomWidth").keyup(function(){
        var tmptxt=$(this).val();
        $(this).val(tmptxt.replace(/\D|^0/g,''));
    }).bind("paste",function(){
        var tmptxt=$(this).val();
        $(this).val(tmptxt.replace(/\D|^0/g,''));
    });
    var Mobile_Boolean = false;
    $('.jingbanren').blur(function(){
        var s = $(this).val();
        if ((/^1[34578]\d{9}$/).test($(this).val())){
            $('.mobile_hint').html("手机号正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_hint').html("请输入正确的手机号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
	$('#ApplicantMobile').blur(function(){
        var s = $(this).val();
        if ((/^1[34578]\d{9}$/).test($(this).val())){
            $('.mobile_mobile').html("手机号正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_mobile').html("请输入正确的手机号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.FaRenLianXiDianHua').blur(function(){

        if ((/^1[34578]\d{9}$/).test($(this).val())){
            $('.mobile_faren').html("手机号正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_faren').html("请输入正确的手机号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.LianXiRenDianHua').blur(function(){

        if ((/^1[34578]\d{9}$/).test($(this).val())){
            $('.mobile_lianxi').html("手机号正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_lianxi').html("请输入正确的手机号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.ShenFenZhengHaoMa').blur(function(){

        if ((/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/).test($(this).val())){
            $('.mobile_sfzhm').html("身份证号码正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_sfzhm').html("请输入正确的身份证号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
	 $('.ApplicantID').blur(function(){

        if ((/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/).test($(this).val())){
            $('.mobile_id').html("身份证号码正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_id').html("请输入正确的身份证号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.WeiTuoDaiLiRenShenFenZhengHaoM').blur(function(){

        if ((/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/).test($(this).val())){
            $('.mobile_wtrsfzhm').html("身份证号码正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_wtrsfzhm').html("请输入正确的身份证号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.GeTiJingYingZheShenFenZhengHao').blur(function(){

        if ((/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/).test($(this).val())){
            $('.mobile_gerensfzhm').html("身份证号码正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_gerensfzhm').html("请输入正确的身份证号码").css("color","red");
            Mobile_Boolean = false;
        }
    });
    $('.QiYeFuZeRenShenFenZhengHaoMa').blur(function(){

        if ((/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/).test($(this).val())){
            $('.mobile_qiyesfzhm').html("身份证号码正确").css("color","green");
            Mobile_Boolean = true;
        }else {
            $('.mobile_qiyesfzhm').html("请输入正确的身份证号码").css("color","red");
            Mobile_Boolean = false;
        }
    });


});