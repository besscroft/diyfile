package com.besscroft.xanadu.controller;

import com.besscroft.xanadu.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 存储接口
 * @Author Bess Croft
 * @Date 2022/12/18 21:23
 */
@RequestMapping("/storage")
@RestController
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

}
