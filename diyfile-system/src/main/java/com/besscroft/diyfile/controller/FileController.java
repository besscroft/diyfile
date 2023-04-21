package com.besscroft.diyfile.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.besscroft.diyfile.common.constant.MessageConstants;
import com.besscroft.diyfile.common.constant.RoleConstants;
import com.besscroft.diyfile.common.param.file.DeleteFileParam;
import com.besscroft.diyfile.common.param.file.GetFileInfoParam;
import com.besscroft.diyfile.common.param.file.GetItemByKeyParam;
import com.besscroft.diyfile.common.param.file.GetUploadUrlParam;
import com.besscroft.diyfile.common.result.AjaxResult;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.common.vo.FileInfoVo;
import com.besscroft.diyfile.common.vo.StorageInfoVo;
import com.besscroft.diyfile.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 文件接口
 * @Author Bess Croft
 * @Date 2023/1/20 21:54
 */
@Tag(name = "文件接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @SaIgnore
    @Operation(summary = "默认驱动信息")
    @GetMapping("/defaultStorage")
    public CommonResult<StorageInfoVo> defaultStorage() {
        return CommonResult.success(fileService.defaultStorage());
    }

    @SaIgnore
    @Operation(summary = "默认文件列表")
    @GetMapping("/defaultItem")
    public CommonResult<List<FileInfoVo>> defaultItem() {
        return CommonResult.success(fileService.defaultItem());
    }

    @SaIgnore
    @Operation(summary = "首页文件列表")
    @GetMapping("/getItem")
    public CommonResult<List<FileInfoVo>> base(@RequestParam(value = "storageId") Long storageId,
                                               @RequestParam(value = "folderPath") String folderPath) {
        // 校验路径
        PathUtils.checkPath(folderPath);
        return CommonResult.success(fileService.getItem(storageId, folderPath));
    }

    @SaIgnore
    @Operation(summary = "首页文件列表")
    @PostMapping("/getItemByKey")
    public CommonResult<List<FileInfoVo>> baseByKey(@RequestBody @Valid GetItemByKeyParam param) {
        // 校验路径
        PathUtils.checkPath(param.getFolderPath());
        return CommonResult.success(fileService.getItemByKey(param.getStorageKey(), param.getFolderPath()));
    }

    @SaIgnore
    @Operation(summary = "文件信息")
    @PostMapping("/getFileInfo")
    public CommonResult<FileInfoVo> getFileInfo(@RequestBody GetFileInfoParam param) {
        // 校验路径
        PathUtils.checkPath(param.getFilePath());
        return CommonResult.success(fileService.getFileInfo(param.getStorageKey(), param.getFilePath(), param.getFileName()));
    }

    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @Operation(summary = "获取文件上传地址")
    @PostMapping("/getUploadUrl")
    public AjaxResult getUploadUrl(@RequestBody @Valid GetUploadUrlParam param) {
        return AjaxResult.success("获取成功！", fileService.getUploadUrl(param.getStorageKey(), param.getPath()));
    }

    @SaCheckRole(
            value = {
                    RoleConstants.PLATFORM_SUPER_ADMIN,
                    RoleConstants.PLATFORM_ADMIN,
                    RoleConstants.PLATFORM_SELF_PROVISIONER
            },
            mode = SaMode.OR
    )
    @Operation(summary = "删除文件接口")
    @PostMapping("/deleteFile")
    public AjaxResult deleteFile(@RequestBody @Valid DeleteFileParam param) {
        // 校验路径
        PathUtils.checkPath(param.getPath());
        fileService.deleteFile(param.getStorageKey(), param.getPath());
        return AjaxResult.success(MessageConstants.DELETE_SUCCESS);
    }

}
