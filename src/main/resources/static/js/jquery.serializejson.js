 $(function(){
		
            (function($){
                $.fn.serializeJson=function(){
                    var serializeObj={};
                    var array=this.serializeArray();
                    var str=this.serialize();
                    $(array).each(function(){
                        if(serializeObj[this.name]){
                            if($.isArray(serializeObj[this.name])){
                                serializeObj[this.name].push(this.value);
                            }else{
                                serializeObj[this.name]=[serializeObj[this.name],this.value];
                            }
                        }else{
                            serializeObj[this.name]=this.value;    
                        }
                    });
                    return serializeObj;
                };
            })(jQuery);
            
        }); 
		 function submitSave(obj){
            var formID = $('form').attr('id');
            if (obj == 0){ //添加
                var url = "insertitem.html";
            }else {//修改
                var url = "doupdateitem.html";
            }

            var param = $("#"+formID).serializeJson();
             param = JSON.stringify(param);
			console.log(param);
             // $.ajax({
             //     async:false,
             //     type:"post",
             //     url:url,
             //     data:param,
             //     contentType:"application/json",
             //     success:function(data){
             //         if (data == 'isnull'){
             //             alert("经营地址必须填写!!");
             //             return;
             //         }
             //         if (data == 'hasapply') {
             //             alert("该事项已经申报，无法修改！");
             //             window.location.href = "../user/memberInfo.html?jsp=4";
             //             return;
             //         }
             //         if ( data!=null && data!= '' && data !='no' && data!='repeat'){
             //             if (data == 'yes') {
             //                 var url_ = "../user/memberInfo.html?jsp=4";
             //                 window.location.href = url_;
             //                 // alert("修改成功，请进行申报");
             //             }else {
             //                 var url_ = "../item/metailspage.html?dataId="+data;
             //                 window.location.href = url_;
             //                 // alert("保存成功，请到进行材料提交");
             //             }
             //
             //
             //         }else if (data == 'repeat') {
             //             alert("请不要重复提交表单");
             //         }else if (data == 'no'){
             //             alert("保存失败，请重新保存");
             //         }
             //     },
             //     error:function (data) {
             //         alert("服务器异常,请刷新当前页面");
             //     }
             // });
            
        }

     // function writeBack(value,code) {
     //     if (value == "")
     //         return;
     //     var param1 = "'"+value+"'";
     //     var param2 = "'"+code+"'";
     //     var param = "{"+'code:'+param2+','+'eid:'+param1+"}";
     //        $.ajax({
     //            type:"post",
     //            url:"writeback.html",
     //            data:param,
     //            contentType:"application/json;charset=utf-8",
     //            dataType:"text",
     //            success:function (data) {
     //                var json = JSON.parse(data);
     //                for (var d in json){
     //                    document.getElementById(""+d+"").value = json[d];
     //                }
     //                // document.getElementById("FuZeRenJingYingZhe").value = json.FuZeRenJingYingZhe;
     //                // document.getElementById("LianXiDianHua").value = json.LianXiDianHua;
     //                // document.getElementById("WeiTuoDaiLiRen").value = json.WeiTuoDaiLiRen;
     //                // document.getElementById("ShenFenZhengHaoMa").value = json.ShenFenZhengHaoMa;
     //
     //            }
     //        })
     // }
