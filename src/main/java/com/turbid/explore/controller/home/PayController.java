package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.qcloud.cos.utils.Base64;
import com.turbid.explore.configuration.AlipayConfig;
import com.turbid.explore.configuration.WeChatPayConfig;
import com.turbid.explore.pojo.*;
import com.turbid.explore.pojo.bo.*;
import com.turbid.explore.repository.*;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.IosVerifyUtil;
import com.turbid.explore.tools.MD5;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Api(description = "支付接口")
@Controller
@RequestMapping("/pay")
@CrossOrigin
public class PayController {

    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 支付宝web支付
     * @param webpayBo
     * @param response
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "支付宝web支付", notes="支付宝web支付")
    @PostMapping("/ali/webpay")
    public void aliwebpay(Principal principal, @RequestBody WebpayBo webpayBo, HttpServletResponse response) {
        orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"aliweb",webpayBo.getBody(),principal.getName(),0);
        webpayBo.setOut_trade_no(CodeLib.randomCode(12,1));
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(webpayBo.getOut_trade_no());
        System.out.println(model.getOutTradeNo());
        model.setSubject(webpayBo.getSubject());
        model.setTotalAmount(webpayBo.getTotal_amount());
        model.setBody(webpayBo.getBody());
        model.setTimeoutExpress(webpayBo.getTimeout_express());
        model.setProductCode(webpayBo.getProduct_code());
//            model.setEnablePayChannels("credit_group,pcreditpayInstallment,moneyFund,balance,creditCard,pcredit,moneyFund,debitCardExpress");
//            ExtendParams extendParams=new ExtendParams();
//            extendParams.setHbFqNum("3");
//            extendParams.setHbFqSellerPercent("100");
//            model.setExtendParams(extendParams);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "查询订单", notes="查询订单")
    @PostMapping("/order/get")
    @ResponseBody
    public Mono<Info> getOrder(@RequestParam("orderno")String orderno) {
        return Mono.just(Info.SUCCESS(orderService.findByOrderNo(orderno)));
    }

    @Autowired
    private IntegralGoodsOrderRepository integralGoodsOrderRepository;


    @ApiOperation(value = "查询我的订单", notes="查询我的订单")
    @PostMapping("/order/myiospay")
    @ResponseBody
    public Mono<Info> myiospay(Principal principal,@RequestParam("page")Integer page) {
        return Mono.just(Info.SUCCESS( orderService.findByUserIos(principal.getName(),page)));
    }

    @ApiOperation(value = "查询我的订单", notes="查询我的订单")
    @PostMapping("/order/my")
    @ResponseBody
    public Mono<Info> ordermy(Principal principal,@RequestParam("page")Integer page) {
        List data=new ArrayList();
        orderService.findByUser(principal.getName(),page).forEach(v->{
            Map item=new HashMap();
            item.put("data",v);
            switch (v.getGoodscode()){
                case "NEEDS_URGENT":
                    item.put("body",projectNeedsService.getByOrder(v.getOrderno()));
                    break;
                case "SEE_NEEDS":
                    item.put("body",needsRelationService.getByOrder(v.getOrderno()));
                    break;
                case "SEE_STUDY":
                    item.put("body",studyService.getByOrder(v.getOrderno()));
                    break;
                case "JF_GOODS":
                    item.put("body",integralGoodsOrderRepository.findByOrderno(v.getOrderno()));
                    break;
            }
            data.add(item);
        });

        return Mono.just(Info.SUCCESS(data));
    }

    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Autowired
    private NeedsRelationService needsRelationService;

    @PostMapping("/ali/asyncnotify")
    public void aliasyncnotify(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        System.out.println("success-------------------------------------");	//请不要修改或删除
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        Order order=orderService.findByOrderNo(out_trade_no);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
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

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            switch (order.getGoodscode()){
                case "NEEDS_URGENT":
                    projectNeedsService.updateURGENT(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的需求加急订单已支付成功。", "支付通知", 1, 0));
                    }
                    break;
                case "SEE_NEEDS":
                    needsRelationService.updateSEE(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的查看需求订单已支付成功。", "支付通知", 1, 0));
                    }
                    break;
                case "SEE_STUDY":
                    studyService.updateSTUDY(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的课程订单已支付成功。", "支付通知", 1, 0));
                    }
                    break;
            }
            //////////////////////////////////////////////////////////////////////////////////////////
           order.setStatus(1);

        }else{//验证失败
            System.out.println("fail");
            order.setStatus(2);
        }
        orderService.save(order);
    }

    @GetMapping("/ali/notifiy")
    public void alinotifiy(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        Order order=orderService.findByOrderNo(out_trade_no);
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //该页面可做页面美工编辑
            System.out.println("验证成功<br />------------------------------------");
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            switch (order.getGoodscode()){
                case "NEEDS_URGENT":
                    projectNeedsService.updateURGENT(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的需求加急订单已支付成功。", "支付通知", 1, 0));
                    }
                    break;
                case "SEE_NEEDS":
                    needsRelationService.updateSEE(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的查看需求订单已支付成功。", "支付通知", 1, 0));
                    }
                    break;
                case "SEE_STUDY":
                    studyService.updateSTUDY(order.getOrderno());
                    if(order.getStatus()==0) {
                        noticeRepository.save(new Notice(order.getUserphone(), "您的课程订单已支付成功。", "支付通知", 1, 0));
                    }
            }
            order.setStatus(1);
            //////////////////////////////////////////////////////////////////////////////////////////
        }else{
            //该页面可做页面美工编辑
            System.out.println("验证失败");
            order.setStatus(2);
        }
        orderService.save(order);
    }

    /**
     * 统一收单线下交易查询
     * @param queryBo
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "统一收单线下交易查询", notes="统一收单线下交易查询")
    @ResponseBody
    @PostMapping("/ali/query")
    public Mono<Info> aliquery(@RequestBody QueryBo queryBo) throws AlipayApiException, UnsupportedEncodingException {

        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeQueryRequest alipay_request = new AlipayTradeQueryRequest();

        AlipayTradeQueryModel model=new AlipayTradeQueryModel();
        model.setOutTradeNo(queryBo.getOut_trade_no());
        //model.setTradeNo(queryBo.getTrade_no());
        alipay_request.setBizModel(model);

        AlipayTradeQueryResponse alipay_response =client.execute(alipay_request);
        return Mono.just(Info.SUCCESS(JSONObject.parseObject(alipay_response.getBody())));
    }

    /**
     * 查询对账单下载地址
     * @param downloadurlBo
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @PostMapping("/ali/downloadurl")
    public Mono<Info> alidownloadurl(@RequestBody DownloadurlBo downloadurlBo) throws AlipayApiException, UnsupportedEncodingException {
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayDataDataserviceBillDownloadurlQueryRequest alipay_request = new AlipayDataDataserviceBillDownloadurlQueryRequest();

        AlipayDataDataserviceBillDownloadurlQueryModel model =new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillType(downloadurlBo.getBill_type());
        model.setBillDate(downloadurlBo.getBill_date());
        alipay_request.setBizModel(model);

        AlipayDataDataserviceBillDownloadurlQueryResponse alipay_response = client.execute(alipay_request);
        return Mono.just(Info.SUCCESS(alipay_response.getBillDownloadUrl()));
    }

    /**
     * 统一收单交易关闭接口
     * @param cloesBo
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @ApiOperation(value = "统一收单交易关闭接口", notes="统一收单交易关闭接口")
    @ResponseBody
    @PostMapping("/ali/cloes")
    public Mono<Info> alicloes(@RequestBody CloesBo cloesBo) throws AlipayApiException, UnsupportedEncodingException {
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeCloseRequest alipay_request=new AlipayTradeCloseRequest();

        AlipayTradeCloseModel model =new AlipayTradeCloseModel();
        model.setOutTradeNo(cloesBo.getOut_trade_no());
        model.setTradeNo(cloesBo.getTrade_no());
        alipay_request.setBizModel(model);

        AlipayTradeCloseResponse alipay_response=client.execute(alipay_request);
        return Mono.just(Info.SUCCESS(alipay_response.getBody()));

    }

    /**
     * 统一收单交易退款接口
     * @param refundBo
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @ResponseBody
    @PostMapping("/ali/refund")
    public Mono<Info> alirefund(@RequestBody RefundBo refundBo) throws AlipayApiException {
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        model.setOutTradeNo(refundBo.getOut_trade_no());
        //model.setTradeNo(refundBo.getTrade_no());
        model.setRefundAmount(refundBo.getRefund_amount());
        model.setRefundReason(refundBo.getRefund_reason());
        //model.setOutRequestNo(refundBo.getOut_request_no());
        alipay_request.setBizModel(model);

        AlipayTradeRefundResponse alipay_response =client.execute(alipay_request);
        return Mono.just(Info.SUCCESS(alipay_response.getBody()));
    }

    /**
     * 统一收单交易退款查询接口
     * @param refundQueryBo
     * @throws AlipayApiException
     */
    @ResponseBody
    @PostMapping("/ali/refundQuery")
    public Mono<Info> alirefundQuery(@RequestBody RefundQueryBo refundQueryBo) throws AlipayApiException {

        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeFastpayRefundQueryRequest alipay_request = new AlipayTradeFastpayRefundQueryRequest();

        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(refundQueryBo.getOut_trade_no());
        model.setTradeNo(refundQueryBo.getTrade_no());
        model.setOutRequestNo(refundQueryBo.getOut_request_no());
        alipay_request.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse alipay_response = client.execute(alipay_request);
        return Mono.just(Info.SUCCESS(alipay_response.getBody()));
    }

    @ApiOperation(value = "支付宝app支付", notes="支付宝app支付")
    @ResponseBody
    @PostMapping("/ali/apppayorder")
    public Mono<Info> apppayorder(Principal principal,@RequestBody WebpayBo webpayBo){
        orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"aliapp",webpayBo.getBody(),principal.getName(),0);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(webpayBo.getOut_trade_no());
        model.setSubject(webpayBo.getSubject());
        model.setTotalAmount(webpayBo.getTotal_amount());
        model.setBody(webpayBo.getBody());
        model.setTimeoutExpress(webpayBo.getTimeout_express());
        model.setProductCode(webpayBo.getProduct_code());
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);

        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            return Mono.just(Info.SUCCESS(response.getBody()));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Mono.just(Info.SUCCESS(""));
        }
    }


    @ApiOperation(value = "微信web支付", notes="微信web支付")
    @ResponseBody
    @PostMapping("/wechat/webpay")
    public Mono<Info> wechatweb(Principal principal,@RequestBody WebpayBo webpayBo){
        try {
            orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"wechatweb",webpayBo.getBody(),principal.getName(),0);
          Map<String,String> map=  doXMLParse(SendPayment(webpayBo.getBody(),webpayBo.getOut_trade_no(),Double.valueOf(webpayBo.getTotal_amount()),webpayBo.getProduct_code(),"MWEB",WeChatPayConfig.APP_ID));
            return Mono.just(Info.SUCCESS(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(null);

    }

    @ApiOperation(value = "微信APP支付", notes="微信APP支付")
    @ResponseBody
    @PostMapping("/wechat/apppay")
    public Mono<Info> apppay(Principal principal,@RequestBody WebpayBo webpayBo){
        try {
            orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"wechatapp",webpayBo.getBody(),principal.getName(),0);
            Map<String,String> map=  doXMLParse(SendPayment(webpayBo.getBody(),webpayBo.getOut_trade_no(),Double.valueOf(webpayBo.getTotal_amount()),webpayBo.getProduct_code(),"APP",WeChatPayConfig.OPEN_APP_ID));
            Map<String,String> param = new HashMap<String,String>();
            String timestamp=String.valueOf(System.currentTimeMillis()/1000);
            System.out.println(map);
            param.put("appid",WeChatPayConfig.OPEN_APP_ID);
            param.put("partnerid", WeChatPayConfig.MCH_ID);
            param.put("prepayid", map.get("prepay_id"));
            param.put("package", "Sign=WXPay");
            param.put("noncestr", map.get("nonce_str"));
            param.put("timestamp",timestamp);
            String sign = GetSign(param);
            param.put("sign", sign);
            return Mono.just(Info.SUCCESS(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(null);

    }

    @ApiOperation(value = "微信回调", notes="微信回调")
    @RequestMapping("/wechat/notifiy")
    public String wechatnotifiy(HttpServletRequest httpServletRequest){
        Order order=null;
        try {
            InputStream inStream = httpServletRequest.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                //将流转换成字符串
                String result = new String(outStream.toByteArray(), "UTF-8");

                //将XML格式转化成MAP格式数据
                Map<String, String> resultMap =doXMLParse(result);
                String out_trade_no=  resultMap.get("out_trade_no");
                 order=orderService.findByOrderNo(out_trade_no);
                switch (order.getGoodscode()){
                    case "NEEDS_URGENT":
                        projectNeedsService.updateURGENT(order.getOrderno());
                        if(order.getStatus()==0) {
                            noticeRepository.save(new Notice(order.getOrderno(),order.getUserphone(), "您的需求加急订单已支付成功。", "支付通知", 1, 0));
                        }
                        break;
                    case "SEE_NEEDS":
                        needsRelationService.updateSEE(order.getOrderno());
                        if(order.getStatus()==0) {
                            noticeRepository.save(new Notice(order.getOrderno(),order.getUserphone(), "您的查看需求订单已支付成功。", "支付通知", 1, 0));
                        }
                        break;
                    case "SEE_STUDY":
                        studyService.updateSTUDY(order.getOrderno());
                        if(order.getStatus()==0) {
                            noticeRepository.save(new Notice(order.getOrderno(),order.getUserphone(), "您的课程订单已支付成功。", "支付通知", 1, 0));
                        }
                }
                order.setStatus(1);
                //后续具体自己实现
            }
            //通知微信支付系统接收到信息
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            order.setStatus(2);
        }finally {
            orderService.save(order);
        }
        //如果失败返回错误，微信会再次发送支付信息

        return "fail";
    }



    /** 微信支付 *************************/



    /*
     * 发起支付请求
     * body	商品描述
     * out_trade_no	订单号
     * total_fee	订单金额		单位  元
     * product_id	商品ID
     */
    public static String SendPayment(String body, String out_trade_no, double total_fee, String product_id,String type,String appid){
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String xml = WXParamGenerate(body,out_trade_no,total_fee,product_id,type,appid);
        String res = httpsRequest(url,"POST",xml);
        return res;
    }

    public static String NonceStr(){
        String res = UUID.randomUUID().toString().replace("-","");
        return res;
    }

    public static String GetIp() {
        InetAddress ia=null;
        try {
            ia=InetAddress.getLocalHost();
            String localip=ia.getHostAddress();
            return localip;
        } catch (Exception e) {
            return null;
        }
    }

    public static String GetSign(Map<String,String> param){
        String StringA =  formatUrlMap(param, false, false);
        System.out.println(StringA);
        String stringSignTemp = MD5.MD5Encode(StringA+"&key="+ WeChatPayConfig.API_KEY).toUpperCase();
        return stringSignTemp;
    }

    //Map转xml数据
    public static String GetMapToXML(Map<String,String> param){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String,String> entry : param.entrySet()) {
            sb.append("<"+ entry.getKey() +">");
            sb.append(entry.getValue());
            sb.append("</"+ entry.getKey() +">");
        }
        sb.append("</xml>");
        return sb.toString();
    }


    //微信统一下单参数设置
    public static String WXParamGenerate(String description,String out_trade_no,double total_fee,String product_id,String type,String appid){
        int fee = (int)(total_fee * 100.00);
        Map<String,String> param = new HashMap<String,String>();
        param.put("appid", appid);
        param.put("mch_id", WeChatPayConfig.MCH_ID);
        param.put("nonce_str",NonceStr());
        param.put("body", description);
        param.put("out_trade_no",out_trade_no);
        param.put("total_fee", fee+"");
        param.put("spbill_create_ip", GetIp());
        param.put("notify_url", WeChatPayConfig.WEIXIN_NOTIFY);
        param.put("trade_type", type);
        param.put("product_id", product_id+"");
        String sign = GetSign(param);
        param.put("sign", sign);
        return GetMapToXML(param);
    }

    //发起微信支付请求
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}"+ ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}"+ e);
        }
        return null;
    }

    //退款的请求方法
    public static String httpsRequest2(String requestUrl, String requestMethod, String outputStr) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        StringBuilder res = new StringBuilder("");
        FileInputStream instream = new FileInputStream(new File("/home/apiclient_cert.p12"));
        try {
            keyStore.load(instream, "".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1313329201".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            StringEntity entity2 = new StringEntity(outputStr , Consts.UTF_8);
            httpost.setEntity(entity2);
            System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);

            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text = "";
                    res.append(text);
                    while ((text = bufferedReader.readLine()) != null) {
                        res.append(text);
                        System.out.println(text);
                    }

                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return  res.toString();

    }

    //xml解析
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String,String> m = new HashMap<String,String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }


    /**
     *
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URLENCODE
     * @param keyToLower    是否需要将Key转换为全小写
     *            true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
    {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
            {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                if (StringUtils.isNotBlank(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }

    @Autowired
    private OrderService orderService;

    public void orderinfo(String orderno,String code,String price,String type,String body,String userphone,Integer status){
        Order order=new Order();
        order.setUserphone(userphone);
        order.setBody(body);
        order.setOrderno(orderno);
        order.setPaytype(type);
        order.setGoodscode(code);
        order.setPrice(price);
        order.setStatus(status);
        orderService.save(order);
    }

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private StudyService studyService;

    @ApiOperation(value = "积分支付", notes="积分支付")
    @PostMapping("/shbpay")
    @ResponseBody
    public Mono<Info> shbpay(Principal principal,@RequestBody WebpayBo webpayBo) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        if(null!=userSecurity.getShb()&&userSecurity.getShb()>Integer.parseInt(webpayBo.getTotal_amount())){
            userSecurity.setShb(userSecurity.getShb()-Integer.parseInt(webpayBo.getTotal_amount()));
            userSecurityService.save(userSecurity);
            studyService.updateSTUDY(webpayBo.getOut_trade_no());
            orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"shb",webpayBo.getBody(),principal.getName(),1);
            return Mono.just(Info.SUCCESS("支付成功"));
        }else {
            return Mono.just(Info.ERROR("设汇币余额不足"));
        }

    }

    @ApiOperation(value = "设汇币支付", notes="设汇币支付")
    @PostMapping("/balancepay")
    @ResponseBody
    public Mono<Info> balancepay(Principal principal,@RequestBody WebpayBo webpayBo) {

        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        if(null!=userSecurity.getBalance()&&userSecurity.getBalance()>Integer.parseInt(webpayBo.getTotal_amount())){
            userSecurity.setBalance(userSecurity.getBalance()-Integer.parseInt(webpayBo.getTotal_amount()));
            userSecurityService.save(userSecurity);
                       switch (webpayBo.getProduct_code()){
                case "NEEDS_URGENT":
                    orderinfo( webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"balance",webpayBo.getBody(),principal.getName(),1);
                    projectNeedsService.updateURGENT( webpayBo.getOut_trade_no());
                        noticeRepository.save(new Notice(webpayBo.getOut_trade_no(),principal.getName(), "您的需求加急订单已支付成功。", "支付通知", 1, 0));
                    break;
                case "SEE_NEEDS":
                    orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"balance",webpayBo.getBody(),principal.getName(),1);
                    needsRelationService.updateSEE(webpayBo.getOut_trade_no());
                    noticeRepository.save(new Notice(webpayBo.getOut_trade_no(),principal.getName(), "您的查看需求订单已支付成功。", "支付通知", 1, 0));
                    break;
                case "SEE_STUDY":
                    orderinfo(webpayBo.getOut_trade_no(),webpayBo.getProduct_code(),webpayBo.getTotal_amount(),"balance",webpayBo.getBody(),principal.getName(),1);
                    studyService.updateSTUDY(webpayBo.getOut_trade_no());
                    noticeRepository.save(new Notice(webpayBo.getOut_trade_no(),principal.getName(), "您的课程订单已支付成功。", "支付通知", 1, 0));
            }
            return Mono.just(Info.SUCCESS("支付成功"));
        }else {
            return Mono.just(Info.ERROR("设汇币余额不足","recharge"));
        }

    }



    /**
     * 苹果内购校验
     * @param priceId 会员价格ID
     * @param transactionId 苹果内购交易ID
     * @param payload 校验体（base64字符串）
     * @return
     */
    @PostMapping("/iospay")
    @ResponseBody
    public Mono<Info> iosPay(Principal principal,String priceId,String transactionId, String payload) {
         System.out.println("苹果内购校验开始，交易ID：" + transactionId + " base64校验体：" + payload);
        //线上环境验证
        String verifyResult = IosVerifyUtil.buyAppVerify(payload, 0);
        if (verifyResult == null) {
            return Mono.just(Info.ERROR("苹果验证失败，返回数据为空"));
        } else {
            System.out.println("线上，苹果平台返回JSON:" + verifyResult);
            JSONObject appleReturn = JSONObject.parseObject(verifyResult);
            String states = appleReturn.getString("status");
            //无数据则沙箱环境验证
            if ("21007".equals(states)) {
                verifyResult = IosVerifyUtil.buyAppVerify(payload, 0);
                 System.out.println("沙盒环境，苹果平台返回JSON:" + verifyResult);
                appleReturn = JSONObject.parseObject(verifyResult);
                states = appleReturn.getString("status");
            }
             System.out.println("苹果平台返回值：appleReturn" + appleReturn);
            // 前端所提供的收据是有效的    验证成功
            if (states.equals("0")) {
                String receipt = appleReturn.getString("receipt");
                JSONObject returnJson = JSONObject.parseObject(receipt);
                String inApp = returnJson.getString("in_app");
                List<HashMap> inApps = JSONObject.parseArray(inApp, HashMap.class);
                if (!CollectionUtils.isEmpty(inApps)) {
                    ArrayList<String> transactionIds = new ArrayList<String>();
                    for (HashMap app : inApps) {
                        transactionIds.add((String) app.get("transaction_id"));
                    }
                    //交易列表包含当前交易，则认为交易成功
                    if (transactionIds.contains(transactionId)) {
                        //处理业务逻辑
                        Integer price=0;
                        switch (priceId){
                            case "com.shehuijia.explore01":
                                price=6;
                                break;
                            case "com.shehuijia.explore02":
                                price=30;
                                break;
                            case "com.shehuijia.explore03":
                                price=50;
                                break;
                            case "com.shehuijia.explore04":
                                price=108;
                                break;
                            case "com.shehuijia.explore05":
                                price=208;
                                break;
                            case "com.shehuijia.explore06":
                                price=418;
                                break;
                            case "com.shehuijia.explore07":
                                price=998;
                                break;
                            case "com.shehuijia.explore08":
                                price=1598;
                                break;
                        }
                        orderinfo(transactionId,priceId,price.toString(),"iospay","ios内购",principal.getName(),1);
                        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
                        if(null!=userSecurity.getBalance()) {
                            userSecurity.setBalance(userSecurity.getBalance() + price);
                        }else {
                            userSecurity.setBalance(0 + price);
                        }
                        userSecurityService.save(userSecurity);
                        noticeRepository.save(new Notice(transactionId, principal.getName(), "您的设汇币充值成功。", "支付通知", 1, 0));
                        return Mono.just(Info.SUCCESS("支付成功"));
                    }
                    return Mono.just(Info.ERROR("当前交易不在交易列表中"));
                }
                return Mono.just(Info.ERROR("未能获取获取到交易列表"));
            } else {
                return Mono.just(Info.ERROR("支付失败，错误码:"+states));
            }
        }
    }

    @Autowired
    private NeedsRelationRepositroy needsRelationRepositroy;

    @Autowired
    private StudyRelationRepository studyRelationRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private ShopService shopService;

    public boolean isvip(String day){
        if(day==null||day==""){
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        try {
            Date date = simpleDateFormat.parse(day);
            if(date.getTime()>new Date().getTime()){ return true; }else { return false; }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @ApiOperation(value = "是否需要支付", notes="是否需要支付 0不需要支付 1需要支付")
    @ResponseBody
    @PostMapping(value = "/ispay")
    public Mono<Info> ispay(Principal principal,@RequestParam(name = "code")String code,@RequestParam("type")Integer type) {
        try {
          UserSecurity userSecurity=  userSecurityService.findByPhone(principal.getName());
            switch (type){
                case 0:
                    if(2<=userSecurity.getType()) {
                        if(null!=userSecurity.getUserAuth()&&null!=userSecurity.getShopcode()) {
                            Shop shop=shopService.getByCode(userSecurity.getShopcode());
                            if(!isvip(shop.getVipday())){
                                Map map = new HashMap<>();
                                map.put("type", 4);
                                return Mono.just(Info.SUCCESS(map));
                            }else{
                                Map map = new HashMap<>();
                                map.put("type", 0);
                                return Mono.just(Info.SUCCESS(map));
                            }
                        }else {
                            Map map=new HashMap<>();
                            map.put("type",2);
                            return Mono.just(Info.SUCCESS(map));
                        }
                    }else {
                        if (0 < needsRelationRepositroy.findneedsR(principal.getName(), code)) {
                            Map map = new HashMap<>();
                            map.put("type", 0);
                            return Mono.just(Info.SUCCESS(map));
                        } else {
                            Map map = new HashMap<>();
                            map.put("type", 1);
                            map.put("key", "逐条查看信息");
                            map.put("value", "SEE_NEEDS");
                            map.put("price", 1);
                            map.put("balance",1);
                            return Mono.just(Info.SUCCESS(map));
                        }
                    }
                case 1:
                    if(0<studyRelationRepository.issee(principal.getName(),code)){
                        Map map=new HashMap<>();
                        map.put("type",0);
                        return Mono.just(Info.SUCCESS(map));
                    }else {
                        Study study=studyRepository.getOne(code);
                        Map map=new HashMap<>();
                        map.put("type",1);
                        map.put("key","课程购买");
                        map.put("value","SEE_STUDY");
                        map.put("price",study.getPrice());
                        map.put("shb",study.getShb());

                        return Mono.just(Info.SUCCESS(map));
                    }
                case 2:
                    if(0<0){
                        Map map=new HashMap<>();
                        map.put("type",0);
                        projectNeedsService.updateURGENT(code);
                        return Mono.just(Info.SUCCESS(map));
                    }else {
                        Map map=new HashMap<>();
                        map.put("type",1);
                        map.put("key","需求加急");
                        map.put("value","NEEDS_URGENT");
                        map.put("price",1);
                        map.put("balance",1);
                        return Mono.just(Info.SUCCESS(map));
                    }
            }
            return Mono.just(Info.SUCCESS(true));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }


    }
}
