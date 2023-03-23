package com.besscroft.diyfile.common.param.sync;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description 同步任务参数
 * @Author Bess Croft
 * @Date 2023/3/21 21:28
 */
@Data
@Schema(title = "同步任务参数")
public class SyncTaskParam {

    /** 同步前存储 key */
    @Schema(title = "同步前存储 key", type = "String")
    private String beforeStorageKey;

    /** 同步前的路径（文件夹或文件相对路径） */
    @Schema(title = "同步前的路径（文件夹或文件相对路径）", type = "String")
    private String beforePath;

    /** 同步后存储 key */
    @Schema(title = "同步后存储 key", type = "String")
    private String afterStorageKey;

    /** 同步后的路径（文件夹相对路径） */
    @Schema(title = "同步后的路径（文件夹相对路径）", type = "String")
    private String afterPath;

}
