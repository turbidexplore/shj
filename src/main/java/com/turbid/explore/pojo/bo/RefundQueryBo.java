package com.turbid.explore.pojo.bo;

import lombok.Data;

@Data
public class RefundQueryBo {

    //商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
    //商户订单号，和支付宝交易号二选一
    private String out_trade_no;
    //支付宝交易号，和商户订单号二选一
    private String trade_no;
    //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
    private String out_request_no;

}
