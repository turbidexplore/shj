package com.turbid.explore.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "查询订单实体")
public class QueryBo {

    //商户订单号，商户网站订单系统中唯一订单号，必填
    @ApiModelProperty(value = "商户订单号，和商户订单号二选一")
    private String out_trade_no;

    //支付宝交易号
    @ApiModelProperty(value = "支付宝交易号，和商户订单号二选一")
    private String trade_no;
}
