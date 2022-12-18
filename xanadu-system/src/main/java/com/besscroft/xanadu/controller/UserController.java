package com.besscroft.xanadu.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.besscroft.xanadu.common.entity.User;
import com.besscroft.xanadu.common.param.LoginParam;
import com.besscroft.xanadu.common.result.AjaxResult;
import com.besscroft.xanadu.common.result.CommonResult;
import com.besscroft.xanadu.common.util.CommonPage;
import com.besscroft.xanadu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description 用户接口
 * @Author Bess Croft
 * @Date 2022/12/15 14:34
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public AjaxResult login(@RequestBody @Valid LoginParam param) {
        SaTokenInfo tokenInfo = userService.login(param.getUsername(), param.getPassword(), param.getIsRememberMe());
        return AjaxResult.success(tokenInfo);
    }

    @GetMapping("/info")
    @Operation(summary = "获取已登录用户信息")
    public AjaxResult info() {
        Map<String, Object> info = userService.info();
        return AjaxResult.success(info);
    }

    @GetMapping("/userPage")
    @Operation(summary = "用户分页列表")
    public CommonResult<CommonPage<User>> userPage(@RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize) {
        List<User> userPageVoList = userService.userPage(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(userPageVoList));
    }

    @Operation(summary = "用户删除接口")
    @DeleteMapping("/delete/{userId:[\\d]+}")
    public AjaxResult delete(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
        return AjaxResult.success("删除成功！");
    }

}

