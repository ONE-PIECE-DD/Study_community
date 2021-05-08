package com.onepiece.community.community.service;

import com.onepiece.community.community.dto.NotificationDTO;
import com.onepiece.community.community.dto.PaginationDTO;
import com.onepiece.community.community.enums.NotificationEnum;
import com.onepiece.community.community.enums.NotificationStatusEnum;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.exception.CustomizeException;
import com.onepiece.community.community.mapper.NotificationMapper;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount= (int)notificationMapper.countByExample(notificationExample);


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
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));//取出当前页需要显示的question对象有哪些，存储到表中

        if(notifications.size()==0){
            return paginationDTO;
        }
        /*Set<Long> disUserIds = notifications.stream().map(notify -> notify.getNotifier()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>(disUserIds);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));
*/
        List<NotificationDTO> notificationDTOs=new ArrayList<>();//将数据库当中的questions转换我咱们前端需要的新的questions（含时间，头像，id等用户信息）
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOs);
        return paginationDTO;//返回一页的信息（ps：一个链表对象存储的是一页的，而非所有页的）
    }

    public Long unreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
