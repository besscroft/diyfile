package com.besscroft.diyfile.common.converter;

import com.besscroft.diyfile.common.param.user.UserAddParam;
import com.besscroft.diyfile.common.param.user.UserUpdateParam;
import com.besscroft.diyfile.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description 用户对象转换器
 * @Author Bess Croft
 * @Date 2022/12/20 14:42
 */
@Mapper(componentModel = "spring")
public interface UserConverterMapper {

    UserConverterMapper INSTANCE = Mappers.getMapper(UserConverterMapper.class);

    User AddParamToUser(UserAddParam param);

    User UpdateParamToUser(UserUpdateParam param);

}
