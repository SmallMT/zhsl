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
                var url = "/item/insertitem";
            }else {//修改
                var url = "/item/doupdateitem";
            }
            var param = $("#"+formID).serializeJson();
             param = JSON.stringify(param);
			console.log(param);
             $.ajax({
                 async:false,
                 type:"post",
                 url:url,
                 data:param,
                 contentType:"application/json",
                 success:function(data){
                     var url_ = "/item/metails?dataId="+data;
                     window.location.href = url_;
                     alert("保存成功，请到进行材料提交");
                 },
                 error:function (data) {
                     alert("服务器异常,请刷新当前页面");
                 }
             });
            
        }
        function submitUpdate(){
            var formID = $('form').attr('id');
            var url = "/item/doupdateitem";
            var param = $("#"+formID).serializeJson();
            param = JSON.stringify(param);
            $.ajax({
                async:false,
                type:"post",
                url:url,
                data:param,
                contentType:"application/json",
                success:function(data){
                    var url_ = "/user/index";
                    self.location.href = url_;
                },
                error:function (data) {
                    alert("服务器异常,请刷新当前页面");
                }
            });
        }