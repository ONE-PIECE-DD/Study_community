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
                window.location.reload();
            }else {
                if(response.code==2003){
                    var isAccepted = confirm(response.message)//弹出一个确认框，判断其是否登录
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=0a6fa384d45b2abe4527&redirect_url=http://localhost:8887/callback&scope=user&state=1");//打开一个新窗口
                        window.localStorage.setItem("closable", true);
                    }
                }else {

                    alert(response.message);//浏览器弹窗显示
                }
            }
        },
        dataType:"json"
    });
}