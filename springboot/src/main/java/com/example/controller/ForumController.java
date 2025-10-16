package com.example.controller;

import com.example.common.Result;
import com.example.entity.Forum;
import com.example.service.ForumService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

    @PostMapping("/add")
    public Result add(@RequestBody Forum forum) {
        forumService.add(forum);
        return Result.success(forum);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Forum forum){
        forumService.updateById(forum);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        forumService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        forumService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Forum forum = forumService.selectById(id);
        return Result.success(forum);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Forum forum){
        List<Forum> list = forumService.selectAll(forum);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Forum forum,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<Forum> pageInfo = forumService.selectPage(forum, pageNum, pageSize);
       return Result.success(pageInfo);
    }
}
