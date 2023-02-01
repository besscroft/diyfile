package com.besscroft.xanadu.common.enums;

import com.besscroft.xanadu.common.interfaces.BasicEnum;

/**
 * @Description 存储类型枚举
 * @Author Bess Croft
 * @Date 2023/2/1 17:20
 */
public enum StorageTypeEnum implements BasicEnum<Integer> {

    LOCAL(0, "本地"),
    ONE_DRIVE(1, "OneDrive"),
    ALIYUN_OSS(2, "阿里云OSS");

    private final int value;
    private final String description;

    StorageTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
