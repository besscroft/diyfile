package com.besscroft.xanadu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.besscroft.xanadu.common.entity.Storage;
import com.besscroft.xanadu.mapper.StorageMapper;
import com.besscroft.xanadu.service.StorageService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Bess Croft
 * @Date 2022/12/18 21:13
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
}
