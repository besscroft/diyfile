package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.constant.MessageConstants;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.entity.Storage;
import com.besscroft.diyfile.common.exception.DiyFileException;
import com.besscroft.diyfile.common.param.storage.StorageAddParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateParam;
import com.besscroft.diyfile.common.param.storage.StorageUpdateStatusParam;
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
import java.util.Objects;

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
    public CommonResult<Void> delete(@PathVariable(name = "storageId") Long storageId) {
        storageService.deleteStorage(storageId);
        return CommonResult.success(MessageConstants.DELETE_SUCCESS);
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
    public CommonResult<Void> add(@RequestBody @Valid StorageAddParam param) {
        if (Objects.equals("proxy", param.getStorageKey())) {
            throw new DiyFileException("存储 key 不能为 proxy");
        }
        if (StrUtil.contains(param.getStorageKey(), "/")) {
            throw new DiyFileException("存储 key 不能包含 /");
        }
        storageService.addStorage(param);
        return CommonResult.success(MessageConstants.ADD_SUCCESS);
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
    public CommonResult<Void> update(@RequestBody @Valid StorageUpdateParam param) {
        storageService.updateStorage(param);
        return CommonResult.success(MessageConstants.UPDATE_SUCCESS);
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

    @SaIgnore
    @Operation(summary = "存储详情接口(不鉴权，脱敏处理)")
    @GetMapping("/infoByKey/{storageKey}")
    public CommonResult<StorageInfoVo> info(@PathVariable(name = "storageKey") String storageKey) {
        return CommonResult.success(storageService.getInfoByStorageKey(storageKey));
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
    public CommonResult<Void> updateStatus(@RequestBody @Valid StorageUpdateStatusParam param) {
        storageService.updateStatus(param.getStorageId(), param.getStatus());
        return CommonResult.success(MessageConstants.UPDATE_SUCCESS);
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
    public CommonResult<Void> setDefault(@PathVariable(name = "storageId") Long storageId) {
        storageService.setDefault(storageId);
        return CommonResult.success(MessageConstants.UPDATE_SUCCESS);
    }

    @SaIgnore
    @Operation(summary = "已启用存储接口")
    @GetMapping("/getEnableStorage")
    public CommonResult<List<Storage>> getEnableStorage() {
        return CommonResult.success(storageService.getEnableStorage());
    }

    @SaIgnore
    @Operation(summary = "获取 AWS Region 列表")
    @GetMapping("/getAwsRegions")
    public CommonResult<List<String>> getAwsRegions() {
        return CommonResult.success(storageService.getAwsRegions());
    }

}
