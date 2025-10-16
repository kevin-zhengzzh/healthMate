package com.example.mapper;

import com.example.entity.Forum;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ForumMapper {

    void insert(Forum forum);

    void updateById(Forum forum);

    void deleteById(Integer id);

    @Select("select forum.*, user.name as userName, user.avatar as userAvatar from `forum` " +
            "left join user on `forum`.user_id = user.id where forum.id = #{id}")
    Forum selectById(Integer id);
    
    List<Forum> selectAll(Forum forum);

    @Update("update forum set read_count = read_count + 1 where id = #{id}")
    void updateReadCountById(Integer id);
}
