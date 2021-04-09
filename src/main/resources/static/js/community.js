//该函数在点击回复的时候被调用，获取questionId的值和内容，再在ajax里面设置传递方法、url同时拼接json
/*提交回复*/
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
/*展开二级评论*/
function collapseComments(e) {


    var id =e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {
        //展开二级评论
        comments.addClass("in");
        //标记二级评论展开状态
        e.setAttribute("data-collapse","in");
        e.classList.add("active");
    }



}