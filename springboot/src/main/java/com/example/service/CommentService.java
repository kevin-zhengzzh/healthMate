package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
@Resource
private CommentMapper commentMapper;
    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        if (comment.getPid() != null) { //说明是子评论
            Integer pid = comment.getPid();
            Comment parentComment = commentMapper.selectById(pid);
            comment.setRootId(parentComment.getRootId());
        }
        commentMapper.insert(comment);

        if (comment.getPid() == null){
            comment.setRootId(comment.getId());
            commentMapper.updateById(comment);
        }
    }


    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    //递归删除
    public void deepDelete(Integer pid) {
        commentMapper.deleteById(pid);
        List<Comment> commentList = commentMapper.selectByPid(pid); //找到所有子级评论
        for (Comment comment : commentList) {
            deepDelete(comment.getId());
        }
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            commentMapper.deleteById(id);
        }
    }

    public Comment selectById(Integer id){
        return commentMapper.selectById(id);
    }

    public List<Comment> selectAll(Comment comment){
        return commentMapper.selectAll(comment);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public PageInfo<Comment> selectTree(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectRootByFid(comment);
        for (Comment rootComment : list){
            List<Comment> children = commentMapper.selectByRootId(rootComment.getId());
            rootComment.setChildren(children);
        }
        return PageInfo.of(list);
    }

    public Integer selectTotal(Integer fid) {
        return commentMapper.selectByFid(fid);
    }

}
