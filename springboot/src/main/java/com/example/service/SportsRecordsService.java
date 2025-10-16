package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.SportsRecords;
import com.example.mapper.SportsRecordsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportsRecordsService {
@Resource
private SportsRecordsMapper sportsRecordsMapper;
    public void add(SportsRecords sportsRecords) {
        if (StrUtil.isBlank(sportsRecords.getDate())){
            sportsRecords.setDate(DateUtil.today());
        }
        sportsRecordsMapper.insert(sportsRecords);
    }


    public void updateById(SportsRecords sportsRecords) {
        sportsRecordsMapper.updateById(sportsRecords);
    }

    public void deleteById(Integer id) {
        sportsRecordsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            sportsRecordsMapper.deleteById(id);
        }
    }

    public SportsRecords selectById(Integer id){
        return sportsRecordsMapper.selectById(id);
    }

    public List<SportsRecords> selectAll(SportsRecords sportsRecords){
        return sportsRecordsMapper.selectAll(sportsRecords);
    }

    public PageInfo<SportsRecords> selectPage(SportsRecords sportsRecords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SportsRecords> list = sportsRecordsMapper.selectAll(sportsRecords);
        return PageInfo.of(list);
    }

}
