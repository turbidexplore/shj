package com.turbid.explore.configuration;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2021001155675190";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCnXWKQSO+jtc66t/mAXWLJPElQ/aA1PHFQtigsro7wgDPei4J14xC1KYkJ0moRER+qfpZ/hue52PIg6sfCP1/rBH9Aa1yri2n5GbYgE0z5BAj0Tg0jWTkpTw8m1vm+DtjXMsi0Q6uCCL5KowXUyzE0T0OuUkQQBjpTpkXoY1BNOUCV0fMka1lYNhqBdpk5xhURSYzvZyVZAoPIYWbqf9DHWI2+Kz9xoqoHxUZm4OXaIY2drMz1eep5zU63i2ue9tkiHQZT/QKVsTwTHkZM5oT7xwCOgbkRSzEdFjQIfNzeLV41+6VOXHSJaNh0bQRrgPIhKyi/Nk6IGa2leRa0IN71AgMBAAECggEARN84BToOa2cQmGPG550GXttRLGQCfoXN97HOHH0E01FehvEQEaUMJhDelM0i2r0aM9X95qLETwVItmUHKRddSGBEYWUM8IbhaCG9gbuPjFWtvaM04tv/mtEu4gPLJ/wCHaSHVS8Xr94bOKmXZ7bqG6WWq3CtE48Mh4m/8N6VmAEO5ygRTeFNPByCsmzzj+xj/P5HZbi0YQ+NIzwaF/hoKh4izynccP9In5H+TUjpgs1FYSIwCQ9wluQgVUPfQ2DHl/TFK3upDBWis9iuVGEfEgXvpMH8BiH59D8CCJlMaoHM9ClTp7NVocT6kGOZz1zzoJLd/uAr5dWCYcB74nB0bQKBgQD6omOJ6n3Hz4NGnS+iLGFS+MzrzK165m8DCmtTv95J5XfeDQDPlBNz2tKoLZXJ3Ji+fpu4TmcJmjvb/vIo8GUA+Phe5Qcy2o6HTkaNCVL8Bb8qQNxd6XGhphH2VxaQIg0PyGr5BQA5Gb5ihWx1QOCKqCV9JkfIYj3rzMpK3I1pQwKBgQCq8qJbxD7GU6EKce0D70ZTkn1nc1CJaCUMNMP9baR1cJotdpdnXJRenVALFGxHFDkoIxYLKGwBdR+Q3+oGsAzT8tfjUYakIooBFUSZgHwQyUFHV2rH2g9OMXTujwi217Cko12CxEIbIfSXzvRP60uxvHOKCOb6zyayj6su4HeXZwKBgQCyZ4knEM17v1CYoSbY73JPl152snkCNKjspywCr17Rhw+t9d+P4r1+CFNO9zPaPrZTUtgRd22MvkbBsFY7ofKjoJVbJvfa0QwHcEZz+iEh2A1dTFIQXy1NfWLn0NH0wug9RdIvc8nW22ZTxtmPFSFAUbRtwyPxlUyyXlHYu+nE9QKBgAsgpCTpD1UFmJP/engpGSDThko3nWR0XQUAYkma23uxireu4ogd5KKMBuvRtoKh9F6CeirbxLwLjbR2fttUlmEti5QGeIcyoF0Vr1N2o5zMHO2x6Qd9UEW8vmnQ8bZFMABniey7LbpqaWuAUzLolBLLu5GeprpoxZIV/ASfI+m9AoGAfnv8hZmxrQSggnwfqaELKu92gi39XJHHfmQJ8/zqXnXVVambDg9MniU0fNx7PfAcdcpInxs+w0oC+Y9w000g7gBR/KN/8SAs1aXmElKDLRO3GcUtEX6k8Ks/isH9p/Z5KCmOLCqGlkkExnUqjsFZP6+UOVHSEs7o0ou7Pdq9GEE=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://anoax.com/pay/asyncnotify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "https://anoax.com/pay/notify";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhPFv0KYtMmeUGSxcggDJwSMAkKmJ+LQSb84ST8c1um0NNKlnddtPn3tYxJde0UiCjB5jGoV7K+p7XXdDD+qLit2BRo9RopnrVFtMUMrqADwOY79ie6b7ohiL8RVU7kxIgXXR54bw0637T4V05eQSKaUeLbaUUqTLZ59uTIx2HN31r3sCWY89mR5eiuJLUnUSVnrC5WP/Z17FMp76rlK5VC+iOKY6RXqL/uKiv+k3IliMZzMpfhwCuB6pzKRTj4NBtYyUlsunPSEDM0+DwskhjIoPYE1xoxfLV1jgu5crvi0BkyXuUN9FQdLFkvMdB8y7DBlzionUG7VY/zLHaiqHEQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
