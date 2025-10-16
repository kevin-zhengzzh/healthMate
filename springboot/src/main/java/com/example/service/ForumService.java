package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Forum;
import com.example.mapper.ForumMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {
@Resource
private ForumMapper forumMapper;
    public void add(Forum forum) {
        forum.setTime(DateUtil.now());
        forum.setReadCount(0);
        forum.setStatus("Under review");
        forumMapper.insert(forum);
    }


    public void updateById(Forum forum) {
        Account currentUser = TokenUtils.getCurrentUser();
        // The user edited the content and set it to the pending review status
        // 用户编辑了内容，设置为待审核状态
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            forum.setStatus("Under review");
        }
        forumMapper.updateById(forum);
    }

    public void deleteById(Integer id) {
        forumMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            forumMapper.deleteById(id);
        }
    }

    public Forum selectById(Integer id){
        forumMapper.updateReadCountById(id);
        return forumMapper.selectById(id);
    }

    public List<Forum> selectAll(Forum forum){
        return forumMapper.selectAll(forum);
    }

    public PageInfo<Forum> selectPage(Forum forum, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Forum> list = forumMapper.selectAll(forum);
        return PageInfo.of(list);
    }

}
