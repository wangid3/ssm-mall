package com.ssm.mall.action.console;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.dao.vo.SearchVO;
import com.ssm.mall.service.iservice.OrderService;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("managerOrder")
public class OrderAdminController {

    private static final Log logger = LogFactory.getLog(OrderAdminController.class);

    @Autowired
    OrderService orderservice;

    @RequestMapping(value = "pay.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes pay(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        //获取web路径，制定二维码生成路径
        String webServerPath =session.getServletContext().getRealPath("upload");
        return orderservice.pay(user.getUserId(),orderNo,webServerPath);
    }

    /**
     * 获取支付宝下单后的回调信息
     *
     * @param request 注意，支付宝的回调信息都被写入在request中
     * @return
     */
    @RequestMapping(value = "alipay_callback.do", method = RequestMethod.POST)
    //此处的url地址，要与沙箱中配置的支付宝回调请求地址uri一致
    public @ResponseBody
    Object alipayCallback(HttpServletRequest request) {
        //对request中的参数进行格式化处理，将数组元素处理成逗号分割的字符串
        Map<String, String> paramResults = Maps.newHashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] vals = entry.getValue();
            if (vals.length == 1) {
                paramResults.put(key, vals[0]);
            } else {
                StringBuilder valueBuffer = new StringBuilder();
                for (String val : vals) {
                    valueBuffer.append(val + ",");
                }
                String value = valueBuffer.toString().substring(0, valueBuffer.length() - 1);
                paramResults.put(key, value);
            }
        }
        //打印支付宝回调日志信息
        logger.info("支付宝回调，sign:"+paramResults.get("sign")+",trade_status:"+paramResults.get("trade_status")+",参数:"+paramResults);
        //****验证回调信息(验证是否是支付宝发出的信息，还要避免重复通知)
        //1-出去两个无需验签的参数
        paramResults.remove("sign");
        paramResults.remove("sign_type");
        //2-按照RSA2协议进行验签(参数，支付宝公钥，字符集，sign_type)
        try {
            boolean RSA2Flag = AlipaySignature.rsaCheckV2(
                    paramResults, Configs.getAlipayPublicKey(),
                    "utf-8", Configs.getSignType());
            if(!RSA2Flag){
                return ServerRes.error(Result.ALIPAY_ILLEGAL_REQUEST_WARN);
            }
            //TODO：验签成功之后的业务逻辑处理，需要结合订单功能完成
        } catch (AlipayApiException e) {
            logger.info("支付宝验证回调异常", e);
        }
        //TODO:验证支付宝回调信息与订单中的数据是否匹配
        /*
        商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
        判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）；
        校验通知中的 seller_id（或者seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商户可能有多个 seller_id/seller_email）。
         */
        return null;
    }

    @RequestMapping(value = "mlist.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes pay(HttpSession session,
                                       @RequestParam(value = "pageNum",defaultValue = "1" )int pageNum,
                                       @RequestParam(value = "pageSize",defaultValue = "10" )int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return orderservice.mlist(pageNum, pageSize);
    }

    @RequestMapping(value = "mdetail.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes mdetail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return orderservice.getDetailByOrderNo(orderNo);
    }

    @RequestMapping(value = "msearch.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes msearch(HttpSession session,Long orderNo,
                                           String username,
                                           @RequestParam(value = "minPayment",required = false, defaultValue = "0" ) BigDecimal minPayment,
                                           @RequestParam(value = "maxPayment",required = false, defaultValue = "999999.99" )BigDecimal maxPayment,
                                           @RequestParam(value = "pageNum",defaultValue = "1" )int pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10" )int pageSize) {


        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        SearchVO sv=new SearchVO(orderNo,username,minPayment,maxPayment);
        return orderservice.msearch(sv,pageNum,pageSize);
    }

    @RequestMapping(value = "send.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes updateOrderStatusSend(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return orderservice.updateOrderStatusSend(orderNo);
    }

}
