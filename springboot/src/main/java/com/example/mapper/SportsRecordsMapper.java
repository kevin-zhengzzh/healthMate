package com.example.mapper;

import com.example.entity.SportsRecords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SportsRecordsMapper {

    void insert(SportsRecords sportsRecords);

    void updateById(SportsRecords sportsRecords);

    void deleteById(Integer id);

    @Select("select * from `sports_records` where id = #{id}")
    SportsRecords selectById(Integer id);
    
    List<SportsRecords> selectAll(SportsRecords sportsRecords);
    @Select("select * from `sports_records` where user_id = #{userId} and date = #{date}")
    List<SportsRecords> selectByUserIdAndDate(@Param("userId") Integer userId, @Param("date") String date);
}
