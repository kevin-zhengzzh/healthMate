package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.BodyRecords;
import com.example.exception.CustomException;
import com.example.mapper.BodyRecordsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodyRecordsService {
@Resource
private BodyRecordsMapper bodyRecordsMapper;
    public void add(BodyRecords bodyRecords) {
        if (StrUtil.isBlank(bodyRecords.getDate())){
            bodyRecords.setDate(DateUtil.today());
        }
        BodyRecords dbRecords = bodyRecordsMapper.selectByUserIdAndDate(bodyRecords.getUserId(), bodyRecords.getDate());
        if (dbRecords != null){
            throw new CustomException("500", "You have recorded your body information today");
        }
        bodyRecordsMapper.insert(bodyRecords);
    }


    public void updateById(BodyRecords bodyRecords) {
        bodyRecordsMapper.updateById(bodyRecords);
    }

    public void deleteById(Integer id) {
        bodyRecordsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            bodyRecordsMapper.deleteById(id);
        }
    }

    public BodyRecords selectById(Integer id){
        return bodyRecordsMapper.selectById(id);
    }

    public List<BodyRecords> selectAll(BodyRecords bodyRecords){
        return bodyRecordsMapper.selectAll(bodyRecords);
    }

    public PageInfo<BodyRecords> selectPage(BodyRecords bodyRecords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BodyRecords> list = bodyRecordsMapper.selectAll(bodyRecords);
        return PageInfo.of(list);
    }

}
