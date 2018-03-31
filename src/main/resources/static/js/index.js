function selectItem(obj) {
    $('.active.item').removeClass("active");
    obj.className += " active";
    if (obj.id == 'sidebar_item_edit') {
        $('#wrapper iframe').attr('src','/user/editInfo');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
    } else if (obj.id == 'sidebar_item_rzbl') {
        $('#wrapper iframe').attr('src',"/user/certifacationindex");
        $('.active1').removeClass('active1');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
        $('.active6').removeClass('active7');
    }  else if (obj.id == 'sidebar_item_rzzl') {
        $('#wrapper iframe').attr('src',"${pageContext.request.contextPath}/ouser/ousercertificationinfo.html");
        $('.active1').removeClass('active1');
        $('.active3').removeClass('active2');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
        $('.active7').removeClass('active7');

    }else if (obj.id == 'sidebar_item_wdxm') {//我的项目
        $('#wrapper iframe').attr('src',"${pageContext.request.contextPath}/item/mybanjian.html?page=1");
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active4').removeClass('active3');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
        $('.active7').removeClass('active7');
    } else if (obj.id == 'sidebar_item_wdbj') { //我的办件
        alert("此功能正在建设中");
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active6').removeClass('active6');
        $('.active7').removeClass('active7');

    }else if (obj.id == 'sidebar_item_tongjibiao') {//统计报表
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active6').removeClass('active5');
        $('.active7').removeClass('active7');
    }
    else if (obj.id == 'sidebar_item_zixun') {//服务咨询
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
        $('#wrapper iframe').attr('src',"${pageContext.request.contextPath}/item/addzx.html");
    }
}