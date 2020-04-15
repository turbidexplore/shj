package com.turbid.explore.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "关闭订单实体")
public class CloesBo {

    //商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
    //商户订单号，和支付宝交易号二选一
    @ApiModelProperty(value = "商户订单号，和支付宝交易号二选一")
    private String out_trade_no ;

    //支付宝交易号，和商户订单号二选一
    @ApiModelProperty(value = "支付宝交易号，和商户订单号二选一")
    private String trade_no;

}
