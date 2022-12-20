package com.besscroft.xanadu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.besscroft.xanadu.common.entity.Storage;

import java.util.List;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/18 21:09
 */
public interface StorageMapper extends BaseMapper<Storage> {

    /**
     * 列表查询
     * @return
     */
    List<Storage> selectPage();

}
