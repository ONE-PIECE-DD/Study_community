//该函数在点击回复的时候被调用，获取questionId的值和内容，再在ajax里面设置传递方法、url同时拼接json
/*提交回复*/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content);
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content)
}
function comment2target(targetId,type,content) {
    if(!content){
        alert("不能回复空内容");
        return;
    }
    /*向/comment对应mapper下的方法传送json数据*/
    $.ajax({
        type:"POST",
        url:"/comment",//更目录下
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type": type
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

        //调用的接口按照/comment/{id}联系起来
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length!=1){
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse","in");
            e.classList.add("active");

        }else{
            $.getJSON( "/comment/"+id, function( data ) {
                $.each( data.data.reverse(), function(index,comment) {

                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                            "html":comment.content
                        })).append($("<div/>",{
                            "class":"menu"
                        }).append($("<span/>",{
                            "class":"pull-right",
                            "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                        })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement=$("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            });
        }
    }
}
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous.indexOf(value)==-1){

        if(previous) {
            $("#tag").val(previous + ',' + value);
        }else {
            $("#tag").val(value);
        }
    }

}
function showSelectTag(){
    $("#select-tag").show();
}