package com.example.controller;

import com.example.common.Result;
import com.example.entity.SleepRecords;
import com.example.service.SleepRecordsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sleepRecords")
public class SleepRecordsController {

    @Resource
    private SleepRecordsService sleepRecordsService;

    @PostMapping("/add")
    public Result add(@RequestBody SleepRecords sleepRecords) {
        sleepRecordsService.add(sleepRecords);
        return Result.success(sleepRecords);
    }

    @PutMapping("/update")
    public Result update(@RequestBody SleepRecords sleepRecords){
        sleepRecordsService.updateById(sleepRecords);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        sleepRecordsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        sleepRecordsService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        SleepRecords sleepRecords = sleepRecordsService.selectById(id);
        return Result.success(sleepRecords);
    }

    @GetMapping("/selectAll")
    public Result selectAll(SleepRecords sleepRecords){
        List<SleepRecords> list = sleepRecordsService.selectAll(sleepRecords);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(SleepRecords sleepRecords,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<SleepRecords> pageInfo = sleepRecordsService.selectPage(sleepRecords, pageNum, pageSize);
       return Result.success(pageInfo);
    }
}
