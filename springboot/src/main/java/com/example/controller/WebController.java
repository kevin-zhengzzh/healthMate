package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.BodyRecordsMapper;
import com.example.mapper.EatingRecordsMapper;
import com.example.mapper.SleepRecordsMapper;
import com.example.mapper.SportsRecordsMapper;
import com.example.service.AdminService;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private BodyRecordsMapper bodyRecordsMapper;
    @Resource
    private SportsRecordsMapper sportsRecordsMapper;
    @Resource
    EatingRecordsMapper eatingRecordsMapper;
    @Resource
    SleepRecordsMapper sleepRecordsMapper;

    /**
     * default request interface
     */
    @GetMapping("/test/hello")
    public Result testHello() {
        return Result.success();
    }


    /**
     * Login
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * Register
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }

    /**
     * Change password
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        if (RoleEnum.ADMIN.name().equals(account.getRole())){
            adminService.updatePassword(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())){
            userService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/selectLine1")
    public Result selectLine1(){
        Account currentUser = TokenUtils.getCurrentUser();
        Date date = new Date();
        DateTime startTime = DateUtil.offsetDay(date, -7);
        List<DateTime> dateTimeRange = DateUtil.rangeToList(startTime, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeRange.stream().map(DateUtil::formatDate).toList();
        List<Double> lowList = new ArrayList<>();
        List<Double> highList = new ArrayList<>();
        for (String day : dateStrList) {
            BodyRecords bodyRecords = bodyRecordsMapper.selectByUserIdAndDate(currentUser.getId(), day);
            if (bodyRecords == null) {
                lowList.add(0D);
                highList.add(0D);
            }else{
                lowList.add(bodyRecords.getLowPressure());
                highList.add(bodyRecords.getHighPressure());
            }
        }
        Dict dict = Dict.create().set("date", dateStrList.stream().map(d -> d.substring(6)).toList()).set("low", lowList).set("high", highList);
        return Result.success(dict);
    }

    @GetMapping("/selectLine2")
    public Result selectLine2(){
        Account currentUser = TokenUtils.getCurrentUser();
        Date date = new Date();
        DateTime startTime = DateUtil.offsetDay(date, -7);
        List<DateTime> dateTimeRange = DateUtil.rangeToList(startTime, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeRange.stream().map(DateUtil::formatDate).toList();
        List<Integer> duringList = new ArrayList<>();
        for (String day : dateStrList) {
            List<SportsRecords> sportsRecordsList = sportsRecordsMapper.selectByUserIdAndDate(currentUser.getId(), day);
            Integer during = sportsRecordsList.stream().map(SportsRecords::getDuring).reduce(Integer::sum).orElse(0);
            duringList.add(during);
        }
        Dict dict = Dict.create().set("date", dateStrList.stream().map(d -> d.substring(6)).toList()).set("during", duringList);
        return Result.success(dict);
    }

    //今日记录情况
    @GetMapping("/selectRecord")
    public Result selectRecord() {
        Account currentUser = TokenUtils.getCurrentUser();
        String today = DateUtil.today();
        BodyRecords bodyRecords = bodyRecordsMapper.selectByUserIdAndDate(currentUser.getId(), today);
        List<EatingRecords> eatingRecordsList = eatingRecordsMapper.selectByUserIdAndDate(currentUser.getId(), today);
        List<SportsRecords> sportsRecordsList = sportsRecordsMapper.selectByUserIdAndDate(currentUser.getId(), today);
        List<SleepRecords> sleepRecordsList = sleepRecordsMapper.selectByUserIdAndDate(currentUser.getId(), today);
        Dict dict = Dict.create().set("body", bodyRecords != null)
                .set("eating", !eatingRecordsList.isEmpty())
                .set("sports", !sportsRecordsList.isEmpty())
                .set("sleep", !sleepRecordsList.isEmpty());
        return Result.success(dict);
    }

}
