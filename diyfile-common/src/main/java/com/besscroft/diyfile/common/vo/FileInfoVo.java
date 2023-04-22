package com.besscroft.diyfile.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 文件信息视图对象
 * @Author Bess Croft
 * @Date 2023/1/20 11:31
 */
@Data
@Schema(title = "文件信息视图对象")
public class FileInfoVo {

    @Schema(title = "文件名", type = "String")
    private String name;

    @Schema(title = "时间", type = "Date")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastModifiedDateTime;

    @Schema(title = "大小", type = "Long")
    private Long size;

    @Schema(title = "类型", type = "String")
    private String type;

    @Schema(title = "所在路径", type = "String")
    private String path;

    @Schema(title = "下载地址", type = "String")
    private String url;

    @Schema(title = "代理下载地址", type = "String")
    private String proxyUrl;

    @Schema(title = "文件参数", type = "Object")
    private Object file;

}
