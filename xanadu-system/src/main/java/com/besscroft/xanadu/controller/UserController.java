package com.besscroft.xanadu.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.besscroft.xanadu.common.param.LoginParam;
import com.besscroft.xanadu.common.result.AjaxResult;
import com.besscroft.xanadu.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/15 14:34
 */
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody @Valid LoginParam param) {
        SaTokenInfo tokenInfo = userService.login(param.getUsername(), param.getPassword());
        return AjaxResult.success(tokenInfo);
    }

    @GetMapping("/info")
    public AjaxResult info() {
        Map<String, Object> info = userService.info();
        return AjaxResult.success(info);
    }

}

