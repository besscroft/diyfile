package com.besscroft.xanadu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.xanadu.common.entity.Storage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/18 21:09
 */
public interface StorageMapper extends BaseMapper<Storage> {

    /**
     * 列表查询
     * @param type 驱动类型
     * @return
     */
    List<Storage> selectPage(@Param("type") Integer type);

}
