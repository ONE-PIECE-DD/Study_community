//该函数在点击回复的时候被调用，获取questionId的值和内容，再在ajax里面设置传递方法、url同时拼接json
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type:"POST",
        url:"/comment",//更目录下
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type": 1
        }),
        success:function (response) {
            if(response.code==200){
                $("#comment_section").hide();//如果返回的状态码是成功，则将评论模块隐藏
            }else {
                alert(response.message);//浏览器弹窗显示
            }
            console.log(response);//浏览器控制台打印

        },
        dataType:"json"
    });
}