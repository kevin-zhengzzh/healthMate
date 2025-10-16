package com.example.mapper;

import com.example.entity.BodyRecords;
import com.example.entity.EatingRecords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EatingRecordsMapper {

    void insert(EatingRecords eatingRecords);

    void updateById(EatingRecords eatingRecords);

    void deleteById(Integer id);

    @Select("select * from `eating_records` where id = #{id}")
    EatingRecords selectById(Integer id);
    
    List<EatingRecords> selectAll(EatingRecords eatingRecords);

    @Select("select * from `eating_records` where user_id = #{userId} and date = #{date}")
    List<EatingRecords> selectByUserIdAndDate(@Param("userId") Integer userId, @Param("date") String date);
}
