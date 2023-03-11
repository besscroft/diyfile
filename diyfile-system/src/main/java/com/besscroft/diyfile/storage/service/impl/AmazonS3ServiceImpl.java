package com.besscroft.diyfile.storage.service.impl;

import com.besscroft.diyfile.common.enums.StorageTypeEnum;
import com.besscroft.diyfile.common.param.storage.init.AmazonS3Param;
import com.besscroft.diyfile.storage.service.base.AbstractS3BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @Description Amazon S3 服务实现类
 * @Author Bess Croft
 * @Date 2023/3/10 23:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AmazonS3ServiceImpl extends AbstractS3BaseService<AmazonS3Param> {

    /**
     * https://docs.aws.amazon.com/zh_cn/sdk-for-java/latest/developer-guide/examples-s3.html
     * https://docs.aws.amazon.com/zh_cn/sdk-for-java/latest/developer-guide/home.html
     * https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2
     * https://docs.aws.amazon.com/zh_cn/sdk-for-java/latest/developer-guide/crt-based-s3-client.html
     * https://docs.aws.amazon.com/zh_cn/sdk-for-java/latest/developer-guide/transfer-manager.html
     */
    @Override
    public void init() {
        super.s3Client = S3Client.builder()
                .region(Region.of(initParam.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(initParam.getAccessKey(), initParam.getSecretKey())
                        )
                )
                .build();
    }

    @Override
    public String getFileDownloadUrl(String fileName, String filePath) {
        return null;
    }

    @Override
    public Integer getStorageType() {
        return StorageTypeEnum.AMAZON_S3.getValue();
    }

}
