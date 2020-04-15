package com.turbid.explore.pojo.bo;

import lombok.Data;

@Data
public class RefundBo {

    //商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
    //商户订单号，和支付宝交易号二选一
    private String out_trade_no;
    //支付宝交易号，和商户订单号二选一
    private String trade_no;
    //退款金额，不能大于订单总金额
    private String refund_amount;
    //退款的原因说明
    private String refund_reason;
    //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
    private String  out_request_no;

}
