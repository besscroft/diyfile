package com.besscroft.diyfile.controller;

import com.besscroft.diyfile.common.constant.MessageConstants;
import com.besscroft.diyfile.common.param.sync.SyncTaskParam;
import com.besscroft.diyfile.common.result.CommonResult;
import com.besscroft.diyfile.common.util.PathUtils;
import com.besscroft.diyfile.service.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 存储数据同步
 * @Author Bess Croft
 * @Date 2023/3/21 21:26
 */
@Tag(name = "存储数据同步")
@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping("/taskAdd")
    @Operation(summary = "添加同步任务")
    public CommonResult<Void> taskAdd(@RequestBody SyncTaskParam param) {
        PathUtils.checkPath(param.getBeforePath());
        PathUtils.checkPath(param.getAfterPath());
        syncService.taskAdd(param.getBeforeStorageKey(),
                PathUtils.decode(param.getBeforePath()),
                param.getAfterStorageKey(),
                PathUtils.decode(param.getAfterPath()));
        return CommonResult.success(MessageConstants.ADD_SUCCESS);
    }

}
