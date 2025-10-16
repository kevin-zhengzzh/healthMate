package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        commentService.add(comment);
        return Result.success(comment);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Comment comment){
        commentService.updateById(comment);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        commentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        commentService.deleteBatch(ids);
        return Result.success();
    }

    @DeleteMapping("/deepDelete/{id}")
    public Result deepDelete(@PathVariable Integer id){
        commentService.deepDelete(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Comment comment = commentService.selectById(id);
        return Result.success(comment);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Comment comment){
        List<Comment> list = commentService.selectAll(comment);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
       PageInfo<Comment> pageInfo = commentService.selectPage(comment, pageNum, pageSize);
       return Result.success(pageInfo);
    }

    @GetMapping("/selectTotal/{fid}")
    public Result selectTotal(@PathVariable Integer fid){
        Integer total = commentService.selectTotal(fid);
        return Result.success(total);
    }

    @GetMapping("/selectTree")
    public Result selectTree(Comment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Comment> pageInfo = commentService.selectTree(comment, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
