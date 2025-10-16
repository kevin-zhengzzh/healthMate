package com.example.controller;

import com.example.common.Result;
import com.example.entity.BodyRecords;
import com.example.service.BodyRecordsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodyRecords")
public class BodyRecordsController {

    @Resource
    private BodyRecordsService bodyRecordsService;

    @PostMapping("/add")
    public Result add(@RequestBody BodyRecords bodyRecords) {
        bodyRecordsService.add(bodyRecords);
        return Result.success(bodyRecords);
    }

    @PutMapping("/update")
    public Result update(@RequestBody BodyRecords bodyRecords){
        bodyRecordsService.updateById(bodyRecords);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        bodyRecordsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        bodyRecordsService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        BodyRecords bodyRecords = bodyRecordsService.selectById(id);
        return Result.success(bodyRecords);
    }

    @GetMapping("/selectAll")
    public Result selectAll(BodyRecords bodyRecords){
        List<BodyRecords> list = bodyRecordsService.selectAll(bodyRecords);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(BodyRecords bodyRecords,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<BodyRecords> pageInfo = bodyRecordsService.selectPage(bodyRecords, pageNum, pageSize);
       return Result.success(pageInfo);
    }
}
