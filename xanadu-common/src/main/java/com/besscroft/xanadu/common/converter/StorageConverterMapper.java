package com.besscroft.xanadu.common.converter;

import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.common.param.storage.StorageAddParam;
import com.besscroft.xanadu.common.param.storage.StorageUpdateParam;
import com.besscroft.xanadu.common.vo.StorageInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description 存储对象转换器
 * @Author Bess Croft
 * @Date 2022/12/21 15:03
 */
@Mapper(componentModel = "spring")
public interface StorageConverterMapper {

    StorageConverterMapper INSTANCE = Mappers.getMapper(StorageConverterMapper.class);

    Storage AddParamToStorage(StorageAddParam param);

    Storage UpdateParamToStorage(StorageUpdateParam param);

    StorageInfoVo StorageToInfoVo(Storage storage);

}
