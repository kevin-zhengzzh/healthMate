package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.EatingRecords;
import com.example.mapper.EatingRecordsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EatingRecordsService {
@Resource
private EatingRecordsMapper eatingRecordsMapper;
    public void add(EatingRecords eatingRecords) {
        if (StrUtil.isBlank(eatingRecords.getDate())){
            eatingRecords.setDate(DateUtil.today());
        }
        eatingRecordsMapper.insert(eatingRecords);
    }


    public void updateById(EatingRecords eatingRecords) {
        eatingRecordsMapper.updateById(eatingRecords);
    }

    public void deleteById(Integer id) {
        eatingRecordsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            eatingRecordsMapper.deleteById(id);
        }
    }

    public EatingRecords selectById(Integer id){
        return eatingRecordsMapper.selectById(id);
    }

    public List<EatingRecords> selectAll(EatingRecords eatingRecords){
        return eatingRecordsMapper.selectAll(eatingRecords);
    }

    public PageInfo<EatingRecords> selectPage(EatingRecords eatingRecords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EatingRecords> list = eatingRecordsMapper.selectAll(eatingRecords);
        return PageInfo.of(list);
    }

}
