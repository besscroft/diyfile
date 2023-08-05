package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.besscroft.diyfile.common.constant.MessageConstants;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.entity.User;
import com.besscroft.diyfile.common.param.LoginParam;
import com.besscroft.diyfile.common.param.user.UserAddParam;
import com.besscroft.diyfile.common.param.user.UserUpdateParam;
import com.besscroft.diyfile.common.param.user.UserUpdatePwdParam;
import com.besscroft.diyfile.common.param.user.UserUpdateStatusParam;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.common.util.CommonPage;
import com.besscroft.diyfile.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户接口")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @SaIgnore
    @PostMapping("/login")
    @Operation(summary = "登录")
    public CommonResult<SaTokenInfo> login(@RequestBody @Valid LoginParam param) {
        SaTokenInfo tokenInfo = userService.login(param.getUsername(), param.getPassword());
        return CommonResult.success(tokenInfo);
    }

    @GetMapping("/info")
    @Operation(summary = "获取已登录用户信息")
    public CommonResult<Map<String, Object>> info() {
        Map<String, Object> info = userService.info();
        return CommonResult.success(info);
    }

    @GetMapping("/userPage")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER,
                    RoleConstants.PLATFORM_VIEW
            },
            mode = SaMode.OR
    )
    @Operation(summary = "用户分页列表")
    public CommonResult<CommonPage<User>> userPage(@RequestParam("pageNum") Integer pageNum,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   @RequestParam(value = "role", required = false) String role) {
        List<User> userPageVoList = userService.userPage(pageNum, pageSize, role);
        return CommonResult.success(CommonPage.restPage(userPageVoList));
    }

    @Operation(summary = "用户删除接口")
    @SaCheckRole({ RoleConstants.PLATFORM_SUPER_ADMIN })
    @DeleteMapping("/delete/{userId:[\\d]+}")
    public CommonResult<Void> delete(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
        return CommonResult.success(MessageConstants.DELETE_SUCCESS);
    }

    @Operation(summary = "用户信息获取接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @GetMapping("/info/{username}")
    public CommonResult<User> get(@PathVariable(name = "username") String username) {
        User user = userService.getUser(username);
        return CommonResult.success(user);
    }

    @Operation(summary = "用户新增接口")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @PostMapping("/add")
    public CommonResult<Void> add(@RequestBody @Valid UserAddParam param) {
        userService.addUser(param);
        return CommonResult.success();
    }

    @Operation(summary = "用户更新接口")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @PutMapping("/update")
    public CommonResult<Void> update(@RequestBody @Valid UserUpdateParam param) {
        userService.updateUser(param);
        return CommonResult.success();
    }

    @Operation(summary = "用户查询接口")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @GetMapping("/getUser/{userId:[\\d]+}")
    public CommonResult<User> getUser(@PathVariable(name = "userId") Long userId) {
        User user = userService.getUserById(userId);
        return CommonResult.success(user);
    }

    @Operation(summary = "用户启用状态更新接口")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @PutMapping("/updateStatus")
    public CommonResult<Void> updateStatus(@RequestBody @Valid UserUpdateStatusParam param) {
        userService.updateStatus(param.getUserId(), param.getStatus());
        return CommonResult.success(MessageConstants.UPDATE_SUCCESS);
    }

    @Operation(summary = "用户密码更新接口")
    @PutMapping("/updatePassword")
    public CommonResult<Void> updatePassword(@RequestBody @Valid UserUpdatePwdParam param) {
        userService.updatePassword(param.getUserId(), param.getIsSelf(), param.getOldPassword(), param.getNewPassword());
        return CommonResult.success(MessageConstants.UPDATE_SUCCESS);
    }

}

