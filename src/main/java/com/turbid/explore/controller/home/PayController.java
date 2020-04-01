package com.turbid.explore.controller.home;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.turbid.explore.configuration.AlipayConfig;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Api(description = "支付接口")
@RestController
@RequestMapping("/pay")
@CrossOrigin
public class PayController {


    Logger logger= LoggerFactory.getLogger(PayController.class);

    /**
     * 封装好参数，前台通过form表单来调起支付宝
     */
    @ApiOperation(value = "阿里支付", notes="阿里支付")
    @RequestMapping("/alipay")
    public void wappay(HttpServletResponse response, @RequestParam("pid")String pid, @RequestParam("price")String price){

        try {
            Date curDate=new Date();
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.order_url, AlipayConfig.app_id, AlipayConfig.app_private_key, AlipayConfig.data_format, AlipayConfig.input_charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            //创建API对应的request类
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

            model.setBody("服务费");
            model.setOutTradeNo(curDate.getTime()+"");
            model.setSubject("服务费");
            if (StringUtils.isNotBlank("")) {
                model.setTimeoutExpress("");
            }
            model.setTotalAmount(price);
            alipayRequest.setBizModel(model);
            //调用SDK生成表单
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.input_charset);
            //直接将完整的表单html输出到页面
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (AlipayApiException e) {
            logger.error("生成订单失败(AliPay)：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("生成订单失败(IO)：" + e.getMessage());
        }
    }


    /**
     * 手机网站支付回调接口
     *
     * @param request
     */
    @RequestMapping(value = "/callback")
    public void callback(HttpServletRequest request) {
        try {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            //支付宝交易号
            String trade_no = request.getParameter("trade_no");
            //交易状态
            String trade_status = request.getParameter("trade_status");
            //计算得出通知验证结果
            if (StringUtils.isNotBlank                                                                         (out_trade_no) && StringUtils.isNotBlank(trade_no) && StringUtils.isNotBlank(trade_status)) {
                boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.input_charset, AlipayConfig.sign_type);
                if (verify_result) {
                    //验证成功
                    logger.info("支付成功！订单号：" + out_trade_no + "，支付宝交易号：" + trade_no);
                    if(trade_status.equals("TRADE_FINISHED")){
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序
                        //注意：
                        //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                        //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。

                    } else if (trade_status.equals("TRADE_SUCCESS")){
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                        //注意：
                        //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误：" + e.getMessage());
            e.printStackTrace();
            System.out.print(boolean.class);

        }
    }
}
