package com.onepiece.community.community.mapper;

import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface QuestionExtMapper {
    int incView(Question record);//调用该方法的时候，自动映射到QuestionExtMapper.xml,映射关系靠xml文件当中的标签
}
