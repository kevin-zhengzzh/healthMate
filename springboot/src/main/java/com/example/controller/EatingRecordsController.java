package com.example.controller;

import com.example.common.Result;
import com.example.entity.EatingRecords;
import com.example.service.EatingRecordsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eatingRecords")
public class EatingRecordsController {

    @Resource
    private EatingRecordsService eatingRecordsService;

    @PostMapping("/add")
    public Result add(@RequestBody EatingRecords eatingRecords) {
        eatingRecordsService.add(eatingRecords);
        return Result.success(eatingRecords);
    }

    @PutMapping("/update")
    public Result update(@RequestBody EatingRecords eatingRecords){
        eatingRecordsService.updateById(eatingRecords);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        eatingRecordsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        eatingRecordsService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        EatingRecords eatingRecords = eatingRecordsService.selectById(id);
        return Result.success(eatingRecords);
    }

    @GetMapping("/selectAll")
    public Result selectAll(EatingRecords eatingRecords){
        List<EatingRecords> list = eatingRecordsService.selectAll(eatingRecords);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(EatingRecords eatingRecords,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<EatingRecords> pageInfo = eatingRecordsService.selectPage(eatingRecords, pageNum, pageSize);
       return Result.success(pageInfo);
    }
}
