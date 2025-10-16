package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {

    void insert(Comment comment);

    void updateById(Comment comment);

    void deleteById(Integer id);

    @Select("select * from `comment` where id = #{id}")
    Comment selectById(Integer id);
    
    List<Comment> selectAll(Comment comment);

    List<Comment> selectRootByFid(Comment comment);

    List<Comment> selectByRootId(Integer rootId);

    @Select("select count(*) from comment where fid = #{fid}")
    Integer selectByFid(Integer fid);

    @Select("select * from `comment` where pid = #{pid}")
    List<Comment> selectByPid(Integer pid);
}
