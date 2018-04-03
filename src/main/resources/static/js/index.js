function selectItem(obj) {
    $('.active.item').removeClass("active");
    obj.className += " active";
    if (obj.id == 'sidebar_item_edit') {//个人信息
        $('#wrapper iframe').attr('src','/user/editInfo');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
    } else if (obj.id == 'sidebar_item_rzbl') {//认证
        $('#wrapper iframe').attr('src',"/user/certification");
        $('.active1').removeClass('active1');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
    }  else if (obj.id == 'sidebar_item_rzzl') {//认证信息
        $('#wrapper iframe').attr('src',"/user/certifacationindex");
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
    }else if (obj.id == 'sidebar_item_wdbj') {//我的办件
        $('#wrapper iframe').attr('src',"/item/banjian");
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active5').removeClass('active5');
        $('.active6').removeClass('active6');
    }else if (obj.id == 'sidebar_item_tongjibiao') {//统计报表
        $('#wrapper iframe').attr('src',"/user/certifacationindex");
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active6').removeClass('active6');
    }
    else if (obj.id == 'sidebar_item_zixun') {//服务咨询
        $('.active1').removeClass('active1');
        $('.active2').removeClass('active2');
        $('.active3').removeClass('active3');
        $('.active4').removeClass('active4');
        $('.active5').removeClass('active5');
        $('#wrapper iframe').attr('src',"");
    }
}