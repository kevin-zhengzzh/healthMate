package com.example.mapper;

import com.example.entity.SleepRecords;
import com.example.entity.SportsRecords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SleepRecordsMapper {

    void insert(SleepRecords sleepRecords);

    void updateById(SleepRecords sleepRecords);

    void deleteById(Integer id);

    @Select("select * from `sleep_records` where id = #{id}")
    SleepRecords selectById(Integer id);
    
    List<SleepRecords> selectAll(SleepRecords sleepRecords);

    @Select("select * from `sleep_records` where user_id = #{userId} and date = #{date}")
    List<SleepRecords> selectByUserIdAndDate(@Param("userId") Integer userId, @Param("date") String date);
}
