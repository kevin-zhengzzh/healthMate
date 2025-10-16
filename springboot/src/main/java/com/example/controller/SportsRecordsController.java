package com.example.controller;

import com.example.common.Result;
import com.example.entity.SportsRecords;
import com.example.service.SportsRecordsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sportsRecords")
public class SportsRecordsController {

    @Resource
    private SportsRecordsService sportsRecordsService;

    @PostMapping("/add")
    public Result add(@RequestBody SportsRecords sportsRecords) {
        sportsRecordsService.add(sportsRecords);
        return Result.success(sportsRecords);
    }

    @PutMapping("/update")
    public Result update(@RequestBody SportsRecords sportsRecords){
        sportsRecordsService.updateById(sportsRecords);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        sportsRecordsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        sportsRecordsService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        SportsRecords sportsRecords = sportsRecordsService.selectById(id);
        return Result.success(sportsRecords);
    }

    @GetMapping("/selectAll")
    public Result selectAll(SportsRecords sportsRecords){
        List<SportsRecords> list = sportsRecordsService.selectAll(sportsRecords);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(SportsRecords sportsRecords,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<SportsRecords> pageInfo = sportsRecordsService.selectPage(sportsRecords, pageNum, pageSize);
       return Result.success(pageInfo);
    }
}
