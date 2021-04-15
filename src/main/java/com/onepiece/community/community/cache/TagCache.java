package com.onepiece.community.community.cache;

import com.onepiece.community.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOs=new ArrayList<>();
        TagDTO program =new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","mode","python","javascript","golong","c/c++","c#","scala"));
        tagDTOs.add(program);

        TagDTO framework =new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("larvel","spring","expres","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTOs.add(framework);

        TagDTO server=new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux","nginx","docker","apche","tomcat"));
        tagDTOs.add(server);


        TagDTO db=new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","sqlite","mongodb","sql","oracle","sqlserver","postgresql"));
        tagDTOs.add(db);

        TagDTO others=new TagDTO();
        others.setCategoryName("其它");
        others.setTags(Arrays.asList("git","github","visual","vim","xcode","eclipse"));
        tagDTOs.add(others);
        return tagDTOs;
    }
    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();
        //TODO:待分析
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
