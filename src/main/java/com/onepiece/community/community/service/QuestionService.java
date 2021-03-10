package com.onepiece.community.community.service;

import com.onepiece.community.community.dto.PaginationDTO;
import com.onepiece.community.community.dto.QuestionDTO;
import com.onepiece.community.community.mapper.QuesstionMapper;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.User;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//service:有了这个之后Spring会自动管理，在里面可以同时使用QuestionMapper、UserMapper-起到组装的作用。当一个请求需要组装User-question的时候便需要service（习惯将中间层这么叫）
@Service
public class QuestionService {

    @Autowired
    private QuesstionMapper quesstionMapper;
    @Autowired
    private UserMapper userMapper;


    //寻找首页的问题
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount=quesstionMapper.count();


        if(totalCount%size==0)
        {
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size+1;
        }
        //判断从前端传入的参数是否合理
        if(page<1){
            page=1;
        }
        if(page>totalPage) {
            page=totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        Integer offset=size*(page-1);
        List<Question> questions = quesstionMapper.list(offset,size);//取出当前页需要显示的question对象有哪些，存储到表中
        List<QuestionDTO> questionDTOList=new ArrayList<>();//将数据库当中的questions转换我咱们前端需要的新的questions（含时间，头像，id等用户信息）
        for (Question question : questions) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//这个工具类的目的：快速的将前一个对象中的数据装载到后一个对象上（通过反射）
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);//请求页所需的所有信息加载完成
        return paginationDTO;//返回一页的信息（ps：一个链表对象存储的是一页的，而非所有页的）
    }

    //寻找个人的问题
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount=quesstionMapper.countByUserId(userId);

        if(totalCount%size==0)
        {
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size+1;
        }


        //判断从前端传入的参数是否合理
        if(page<1){
            page=1;
        }
        if(page>totalPage) {
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        Integer offset=size*(page-1);
        List<Question> questions = quesstionMapper.listByUserId(userId,offset,size);//取出当前页需要显示的question对象有哪些，存储到表中
        List<QuestionDTO> questionDTOList=new ArrayList<>();//将数据库当中的questions转换我咱们前端需要的新的questions（含时间，头像，id等用户信息）
        for (Question question : questions) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//这个工具类的目的：快速的将前一个对象中的数据装载到后一个对象上（通过反射）
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);//请求页所需的所有信息加载完成
        return paginationDTO;//返回一页的信息（ps：一个链表对象存储的是一页的，而非所有页的）

    }


    public QuestionDTO getById(Integer id) {
        Question question = quesstionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null)
        {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            quesstionMapper.create(question);
        }else{
            question.setGmtModified(question.getGmtCreate());

            quesstionMapper.update(question);
        }
    }
}

