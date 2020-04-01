package com.turbid.explore.configuration;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TencentCOSConfig {


    @Value("${com.turbid.tencentoss.qcloud_file_accesskey}")
    private  String ACCESSKEY;

    @Value("${com.turbid.tencentoss.qcloud_file_secretkey}")
    private  String SECRETKEY;

    @Value("${com.turbid.tencentoss.qcloud_file_region}")
    private  String REGION_NAME;

    @Value("${com.turbid.tencentoss.qcloud_file_bucket}")
    public   String QCLOUD_FILE_BUCKET;

    /**
     * 获取访问腾讯的客户端
     * @return
     */
    @Bean
    public  COSClient cosClient()
    {
        // 1 初始化用户身份信息(secretId, secretKey)

        COSCredentials cred = new BasicCOSCredentials(ACCESSKEY, SECRETKEY);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region(REGION_NAME));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        //String bucketName = "mybucket-1251668577";

        return cosClient;
    }
}
