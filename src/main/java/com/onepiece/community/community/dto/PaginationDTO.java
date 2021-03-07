package com.onepiece.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PaginationDTO {
    //此处的数据转换是页面分页有关的数据与数据库当中的数据转换
    //记录与翻页功能有关的当前页面信息
    private boolean showPrevious;//是否显示往上翻页符号
    private boolean showFirstPage;//是否显示转到首页符号
    private boolean showEndPage;//是否显示转到尾页符号
    private boolean showNext;//是否显示往下翻页符号
    private Integer page;//当前页数
    private List<Integer> pages=new ArrayList<>();//记录页数的数值
    private Integer totalPage;//记录能显示的总页数

    private List<QuestionDTO> questions;//记录每一页中的question对象

    public void setPagination(Integer totalCount, Integer page, Integer size) {//该方法未设置questions
        //判断总的页数
        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage = totalCount/size+1;
        }
        //判断传过来的page是否在合理（排除人为输入越界参数）
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        this.page=page;
        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }

        }



        //判断是否展示向前/后翻页功能
        if(page==1){
            showPrevious=false;
        }else{
            showPrevious=true;
        }

        if(page==totalPage){
            showNext=false;
        }else{
            showNext=true;
        }

        //判断是否展示首页尾页
        if(pages.contains(1)){
            showFirstPage=false;
        }else {
            showFirstPage=true;
        }
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }
    }
}
