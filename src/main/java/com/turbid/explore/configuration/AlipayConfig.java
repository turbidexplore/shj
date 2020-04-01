package com.turbid.explore.configuration;


public class AlipayConfig {
    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String partner = "2088331507491892";

    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public static String app_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCWEk3U+mAgG8hPm2YNhk4rtzHznTY9eecWk+lxMUPMPAW51nUcgnknp1UxMuROiFMO3V4onl5AiUUuXiRqHIcU/kSjaJCGMRZNN+LlncLYbx8z5vrJ6uNXdKMwU4jHJLAdGjHGMO5yqXiy1uXZiLLRGgGiMTg+8xcqa/CW0dltSZqq8kgiO1tIMZmkfQ4jer2ElEhK1wWO6vXp9t+PvxBJGjHMuYREjAT9vhHrP/FFtAJ1f8aMFoGe1YropVhbsMYqP5vn2fyi8k+KalzvNNOsa8mFa1hFJMg89ELMRiby4nXpwaa/VCxUgrH2HCMo9AJn6KmG7pxhKdP/rL0Q2QwbAgMBAAECggEBAIX989ZscvVYOQKzGmyK9TLdD9GXFf3P4P/ssuso9ILfOLGUBN6etDQumMcjzGs+FwLXriGQPVFb/xQGZHcF36pj2L8aNqhKHcJ2WrVvpa//j5mU62jru26zqp0UiLReUoy5faWavNxN6oGrlHAlOJuwn7LERZcXDyEGXTscRQjo5RRbvfo4C1FYmj7qTahtwWPIK2DVCTRnzdUPeuFKMbunUXSGEfAl7p5Kcp8F2Gwf8dPjc/rfEy5a38LKtKKfNPC69X0VixuXM72YahpTNqC+VCP4NF4vNM6vmfvGxzBMYD4ufr1ioLeKV1gk7fxi4nJw9al9q+kCcqQ7cTIE5xECgYEA33X/6OOA+V4ICDaDq3ERrJHUoV0q9IKBuVpZp0ddH6G80spyTin3d/Gl2d6GYdZEpjHJB5bdOIPAAtiuCYWHfAss+LVGSrg0WOSeO34qVSsDQrsu7POw40zEOOBP73+5jStz7y0v2S22rWiDzoUtcWgwpBWKPOZy5lwRkwH+49MCgYEAq+yNCb7g/27VPiZ0WqF0p/7Ld9UEes+WDo39Wu3P5l1TyP8+ePlTribID8I+Y/YqDF/UlWl4YX1oCt/udKA8g9TiBaoPmxMksKz2fptxDChfu79WJZRvXKRBGR5e91JQihflFy+TvbqYc2dD0taMJyDOBb0L7EQ7VmqEXnNKsZkCgYEAtglea83JZ1tel7IxGRyHFVnHKZzIPV+DZgdgOZE9H2OWevFIpLwzSJamURGhz1nJmsm7xl7z3/IUHT1+qWsJr9c6z3B8zqm4myHCdRyWR8M+aMIrvAPY+ZGz19GDzMrz0TiwvQrGi9ldk24s0bRUDKqf1Utmza29tEMUXfmmNtkCgYAIs5dL1/phU0W9uIqJD8Tsv8uijAiHArywjeU11fWAzlZwROLvkob2uIXFrmoj+xCTwcHrsgPiw0shycWy521IqtyptrrFeYmQ26A6NjnZiK2Xvg5pJoMgaDvXPp9F20eyRZVVwBW1M52FscNQB4XecR2cUr4ovidWr1t1wFYWeQKBgQCefCu1wH+igGHC2BqSmNcEwNG/do2NTqArpZawZXAu6rlmG+LvSIMandAxclPG5yP0+czXI9DB2Uv0KYYw+w1YTOKbRg2dEKZqpOx3zHy0lImnZQTrC4CB+2wfoBCjfdPcUdd4yHDuPq8+ONmmNYzlx8XuhPEWKLWmJdPSFGQNDQ==";
    // 沙箱测试商户的私钥
    public static String app_private_key_dev = "************************************************************************************************************************************";

    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlhJN1PpgIBvIT5tmDYZOK7cx8502PXnnFpPpcTFDzDwFudZ1HIJ5J6dVMTLkTohTDt1eKJ5eQIlFLl4kahyHFP5Eo2iQhjEWTTfi5Z3C2G8fM+b6yerjV3SjMFOIxySwHRoxxjDucql4stbl2Yiy0RoBojE4PvMXKmvwltHZbUmaqvJIIjtbSDGZpH0OI3q9hJRIStcFjur16fbfj78QSRoxzLmERIwE/b4R6z/xRbQCdX/GjBaBntWK6KVYW7DGKj+b59n8ovJPimpc7zTTrGvJhWtYRSTIPPRCzEYm8uJ16cGmv1QsVIKx9hwjKPQCZ+iphu6cYSnT/6y9ENkMGwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://anoax.com/ali/callback";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://turbidexplore.com/pay/ali/success";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    // 应用ID
    public static String app_id = "2019010462819055";

    // 沙箱测试应用ID
    public static String app_id_dev = "***********************";

    // 上线正式地址
    public static String order_url = "https://openapi.alipay.com/gateway.do";

    // 沙箱测试地址
    public static String order_dev_url = "https://openapi.alipaydev.com/gateway.do";

    // 格式
    public static String data_format = "JSON";
}

