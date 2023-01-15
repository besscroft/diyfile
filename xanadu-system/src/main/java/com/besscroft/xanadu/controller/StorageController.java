package com.besscroft.xanadu.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.xanadu.common.constant.RoleConstants;
import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.common.param.storage.StorageAddParam;
import com.besscroft.xanadu.common.param.storage.StorageUpdateParam;
import com.besscroft.xanadu.common.param.storage.StorageUpdateStatusParam;
import com.besscroft.xanadu.common.result.AjaxResult;
import com.besscroft.xanadu.common.result.CommonResult;
import com.besscroft.xanadu.common.util.CommonPage;
import com.besscroft.xanadu.common.vo.StorageInfoVo;
import com.besscroft.xanadu.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 存储接口
 * @Author Bess Croft
 * @Date 2022/12/18 21:23
 */
@RequestMapping("/storage")
@RestController
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/storagePage")
    @Operation(summary = "存储分页列表")
    public CommonResult<CommonPage<Storage>> storagePage(@RequestParam("pageNum") Integer pageNum,
                                                         @RequestParam("pageSize") Integer pageSize,
                                                         @RequestParam(value = "type", required = false) Integer type) {
        List<Storage> storageList = storageService.storagePage(pageNum, pageSize, type);
        return CommonResult.success(CommonPage.restPage(storageList));
    }

    @Operation(summary = "存储删除接口")
    @SaCheckRole(value = { RoleConstants.PLATFORM_SUPER_ADMIN, RoleConstants.PLATFORM_ADMIN }, mode = SaMode.OR)
    @DeleteMapping("/delete/{storageId:[\\d]+}")
    public AjaxResult delete(@PathVariable(name = "storageId") Long storageId) {
        storageService.deleteStorage(storageId);
        return AjaxResult.success("删除成功！");
    }

    @Operation(summary = "存储新增接口")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Valid StorageAddParam param) {
        storageService.addStorage(param);
        return AjaxResult.success("新增成功！");
    }

    @Operation(summary = "存储更新接口")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody @Valid StorageUpdateParam param) {
        storageService.updateStorage(param);
        return AjaxResult.success("更新成功！");
    }

    @Operation(summary = "存储详情接口")
    @GetMapping("/info/{storageId:[\\d]+}")
    public CommonResult<StorageInfoVo> info(@PathVariable(name = "storageId") Long storageId) {
        return CommonResult.success(storageService.getInfo(storageId));
    }

    @Operation(summary = "存储启用状态更新接口")
    @PutMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody @Valid StorageUpdateStatusParam param) {
        storageService.updateStatus(param.getStorageId(), param.getStatus());
        return AjaxResult.success("更新成功！");
    }

}
