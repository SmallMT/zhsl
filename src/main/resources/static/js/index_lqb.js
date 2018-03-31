/*tabs切换*/
    		$.fn.tabs =  function() {
		        var opts = {
		            switchingMode: "mouseover"  // or "click"---通过点击鼠标事件切换tabs
		        };
		        var obj = $(this);
		        var clickIndex = 0;
		        obj.addClass("tabsDiv");
		        
 		   /*      $("ul li:first", obj).addClass("tabsSeletedLi");
		        $("ul li", obj).not(":first").addClass("tabsUnSeletedLi"); */
		        $("ul li", obj).bind(opts.switchingMode,
			        function() {
			            if (clickIndex != $("ul li", obj).index($(this))) {
			                clickIndex = $("ul li", obj).index($(this));
			                
			                //$(".tabsSeletedLi", obj).removeClass("tabsSeletedLi").addClass("tabsUnSeletedLi");
			                $("ul li", obj).each(function(){
			                	var classname = $(this).attr("class");
			                	$(this).attr("class", classname.substr(0,4) + "UnSelected");
			                });			                
			                
			                classname = $(this).attr("class");
			                $(this).attr("class", classname.substr(0,4) + "Selected");
			                	
			                //$(this).removeClass("tabsUnSeletedLi").addClass("tabsSeletedLi");
			                
/* 			                $(".tabsSeletedLi span:eq(0)").css("color", '#194488');
			                
			                $(".tabsSeletedLi span:eq(1)").css("color", '#000');
			                $(".tabsUnSeletedLi span").css("color", '#fff'); */

			                var divid = "tab_" + $(this).attr("class").substr(0, 4);
			                //alert(divid);
			           		$(".dotab").hide();
			                $("#" + divid).show();
			                /* $("#" + divid, obj).slideDown("slow"); */
			            };
			        });
	    	};
        	$(".item3").tabs();
		 var $div_li =$('div.userinfomenu ul li');
		    $div_li.mouseover(function(){
				$(this).addClass("selected").siblings().removeClass("selected"); 
			});
			$div_li.mouseout(function(){
				$(this).removeClass("selected"); 
			});
			
			var $login_li =$('div.loginmenu ul li');
		    $login_li.mouseover(function(){
				$(this).addClass("selected").siblings().removeClass("selected"); 
			});
			$login_li.mouseout(function(){
				$(this).removeClass("selected"); 
			});
			
			var $regedit_li =$('div.regeditmenu ul li');
		    $regedit_li.mouseover(function(){
				$(this).addClass("selected").siblings().removeClass("selected"); 
			});
			$regedit_li.mouseout(function(){
				$(this).removeClass("selected"); 
			});
			
			
			$('#userinfomenu').mouseover(function(){
				$('#userinfomenu').show();
			});		
			
			$('#userinfomenu').bind('mouseleave',function(){
				$('#userinfomenu').hide();
			});
			
			
			$('#loginmenu').mouseover(function(){
				$('#loginmenu').show();
			});	
			$('#loginmenu').bind('mouseleave',function(){
				$('#loginmenu').hide();
			});
			
			
			$('#regeditmenu').mouseover(function(){
				$('#regeditmenu').show();
			});	
			$('#regeditmenu').bind('mouseleave',function(){
				$('#regeditmenu').hide();
			});
		 function showuserinfo() {
        $('#userinfomenu').show();
    }

    function hideuserinfo() {
        $('#userinfomenu').hide();
    }

    function showlogin() {
        $('#loginmenu').show();

    }

    function hidelogin() {
        $('#loginmenu').hide();
    }

    function showregedit() {
        $('#regeditmenu').show();
    }

    function hideregedit() {
        $('#regeditmenu').hide();
    }
    //项目登记-审批类登记
    $(".dfpt").click(function(){
    	layer.closeAll();
		$("html,body").animate({scrollTop: $("#sxpt_entrance").offset().top}, 1000);
    });
    $(".zypt").click(function(){
    	layer.closeAll();
    	window.open("http://zwzx.sanya.gov.cn/");
    });
	$(".sjptsp").click(function(){
    	layer.closeAll();
    	window.open("http://zwzx.sanya.gov.cn/");
    });
    $(".sjpthz").click(function(){
    	layer.closeAll();
    	window.open("http://zwzx.sanya.gov.cn/");
    });	
    $("#sjxwhz").click(function(){
    	layer.closeAll();
    	window.open("http://zwzx.sanya.gov.cn/");
    });
     $("#splsb").click(function(){
   	    window.open("http://zwzx.sanya.gov.cn/");
    }) 
     $("#hzlsb").click(function(){
   	 window.open("http://zwzx.sanya.gov.cn/");
    })
    //项目报批-备案类申报
    $("#balsb").click(function(){
   	 window.open("http://zwzx.sanya.gov.cn/");
    })
$(function(){
    $(".item_r_middle_top .bjjl_tit_sub").mouseover(function(){

        $(this).addClass("on").siblings().removeClass("on");
        var index=$(this).index();
        $(".bjjl_list_main .bjjl_list_sub").eq(index).fadeIn(300).siblings().hide();

    });
    $(".wrap7 .Personal_login .menua li").click(function(){
        $(this).removeClass("on").siblings().addClass("on");
        var index=$(this).index();
        $(".Personal_login").find('.form1').eq(index).fadeIn(300).siblings().hide();
        $('.Personal_login .menua').css("display","block");
        $('.Personal_login .zhuce').css("display","block");

    })
})

