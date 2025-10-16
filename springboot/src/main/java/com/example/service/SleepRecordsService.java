package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.SleepRecords;
import com.example.mapper.SleepRecordsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SleepRecordsService {
@Resource
private SleepRecordsMapper sleepRecordsMapper;
    public void add(SleepRecords sleepRecords) {
        if (StrUtil.isBlank(sleepRecords.getDate())){
            sleepRecords.setDate(DateUtil.today());
        }

        // Sleep time calculation
        String sleepTime = DateUtil.formatDate(DateUtil.yesterday()) + " " + sleepRecords.getSleepTime() + ":00"; // 23:00   =>   2025-04-15 23:00:00
        String wakeupTime = DateUtil.today() + " " + sleepRecords.getWakeupTime() + ":00"; // 23:00   =>   2025-04-15 23:00:00
        long minutes = DateUtil.between(DateUtil.parseDateTime(sleepTime), DateUtil.parseDateTime(wakeupTime), DateUnit.MINUTE);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_UP);
        sleepRecords.setSleepDuration(hours.doubleValue());
        sleepRecordsMapper.insert(sleepRecords);
    }


    public void updateById(SleepRecords sleepRecords) {
        // Sleep time calculation
        String sleepTime = DateUtil.formatDate(DateUtil.yesterday()) + " " + sleepRecords.getSleepTime() + ":00"; // 23:00   =>   2025-04-15 23:00:00
        String wakeupTime = DateUtil.today() + " " + sleepRecords.getWakeupTime() + ":00"; // 23:00   =>   2025-04-15 23:00:00
        long minutes = DateUtil.between(DateUtil.parseDateTime(sleepTime), DateUtil.parseDateTime(wakeupTime), DateUnit.MINUTE);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_UP);
        sleepRecords.setSleepDuration(hours.doubleValue());
        sleepRecordsMapper.updateById(sleepRecords);
    }

    public void deleteById(Integer id) {
        sleepRecordsMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            sleepRecordsMapper.deleteById(id);
        }
    }

    public SleepRecords selectById(Integer id){
        return sleepRecordsMapper.selectById(id);
    }

    public List<SleepRecords> selectAll(SleepRecords sleepRecords){
        return sleepRecordsMapper.selectAll(sleepRecords);
    }

    public PageInfo<SleepRecords> selectPage(SleepRecords sleepRecords, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SleepRecords> list = sleepRecordsMapper.selectAll(sleepRecords);
        return PageInfo.of(list);
    }

}
