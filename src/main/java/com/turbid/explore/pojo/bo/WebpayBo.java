package com.turbid.explore.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "支付订单信息")
public class WebpayBo {

    // 商户订单号，商户网站订单系统中唯一订单号，必填
    @ApiModelProperty(value = "商户订单号，商户网站订单系统中唯一订单号，必填")
    private String out_trade_no;

    // 订单名称，必填
    @ApiModelProperty(value = "订单名称，必填")
    private String subject;

    // 付款金额，必填
    @ApiModelProperty(value = "付款金额，必填")
    private String total_amount;

    // 商品描述，可空
    @ApiModelProperty(value = "商品描述，可空")
    private String body;

    // 超时时间 可空
    @ApiModelProperty(value = "超时时间 可空")
    private String timeout_express="2m";

    // 销售产品码 必填
    @ApiModelProperty(value = "销售产品码 必填")
    private String product_code;
}
