package com.besscroft.xanadu.common.interfaces;

/**
 * @Description 枚举接口
 * @Author Bess Croft
 * @Date 2023/2/1 17:17
 */
public interface BasicEnum<T> {

    /**
     * 获取枚举的值
     * @return 枚举值
     */
    T getValue();

    /**
     * 获取枚举的描述
     * @return 描述
     */
    String getDescription();

}
