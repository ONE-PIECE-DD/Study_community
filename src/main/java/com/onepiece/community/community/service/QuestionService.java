package com.onepiece.community.community.service;

import com.onepiece.community.community.dto.QuestionDTO;
import com.onepiece.community.community.mapper.QuesstionMapper;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.User;
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

    public List<QuestionDTO> list() {
        List<Question> questions = quesstionMapper.list();
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questions) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//这个工具类的目的：快速的将前一个对象中的数据装载到后一个对象上（通过反射）
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
