package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.param.storage.StorageAddParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateStatusParam;
import com.besscroft.diyfile.common.result.AjaxResult;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.common.util.CommonPage;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 存储接口
 * @Author Bess Croft
 * @Date 2022/12/18 21:23
 */
@Tag(name = "存储接口")
@RequestMapping("/storage")
@RestController
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/storagePage")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER,
                    RoleConstants.PLATFORM_VIEW
            },
            mode = SaMode.OR
    )
    @Operation(summary = "存储分页列表")
    public CommonResult<CommonPage<Storage>> storagePage(@RequestParam("pageNum") Integer pageNum,
                                                         @RequestParam("pageSize") Integer pageSize,
                                                         @RequestParam(value = "type", required = false) Integer type) {
        List<Storage> storageList = storageService.storagePage(pageNum, pageSize, type);
        return CommonResult.success(CommonPage.restPage(storageList));
    }

    @Operation(summary = "存储删除接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @DeleteMapping("/delete/{storageId:[\\d]+}")
    public AjaxResult delete(@PathVariable(name = "storageId") Long storageId) {
        storageService.deleteStorage(storageId);
        return AjaxResult.success("删除成功！");
    }

    @Operation(summary = "存储新增接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Valid StorageAddParam param) {
        storageService.addStorage(param);
        return AjaxResult.success("新增成功！");
    }

    @Operation(summary = "存储更新接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @PutMapping("/update")
    public AjaxResult update(@RequestBody @Valid StorageUpdateParam param) {
        storageService.updateStorage(param);
        return AjaxResult.success("更新成功！");
    }

    @Operation(summary = "存储详情接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @GetMapping("/info/{storageId:[\\d]+}")
    public CommonResult<StorageInfoVo> info(@PathVariable(name = "storageId") Long storageId) {
        return CommonResult.success(storageService.getInfo(storageId));
    }

    @Operation(summary = "存储启用状态更新接口")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @PutMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody @Valid StorageUpdateStatusParam param) {
        storageService.updateStatus(param.getStorageId(), param.getStatus());
        return AjaxResult.success("更新成功！");
    }

    @Operation(summary = "默认存储设置")
    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @PutMapping("/setDefault/{storageId:[\\d]+}")
    public AjaxResult setDefault(@PathVariable(name = "storageId") Long storageId) {
        storageService.setDefault(storageId);
        return AjaxResult.success("设置成功！");
    }

    @SaIgnore
    @Operation(summary = "已启用存储接口")
    @GetMapping("/getEnableStorage")
    public CommonResult<List<Storage>> getEnableStorage() {
        return CommonResult.success(storageService.getEnableStorage());
    }

}
