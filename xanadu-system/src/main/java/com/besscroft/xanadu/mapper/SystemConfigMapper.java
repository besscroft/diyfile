package com.besscroft.xanadu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.xanadu.common.entity.SystemConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 系统配置持久层
 * @Author Bess Croft
 * @Date 2023/1/7 19:05
 */
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    /**
     * 查询系统配置
     * @param configKey 配置键
     * @return 系统配置
     */
    SystemConfig queryByConfigKey(@Param("configKey") String configKey);

    /**
     * 查询系统配置
     * @param type 配置类型
     * @return 系统配置
     */
    List<SystemConfig> queryAllByType(@Param("type") Integer type);

    /**
     * 更新配置
     * @param configKey 配置键
     * @param configValue 配置值
     * @param updater 更新人
     */
    int updateConfig(@Param("configKey") String configKey,
                     @Param("configValue") String configValue,
                     @Param("updater") Long updater);

}
