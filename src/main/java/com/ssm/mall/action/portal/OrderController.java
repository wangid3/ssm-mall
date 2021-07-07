package com.ssm.mall.action.portal;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.OrderMapper;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.OrderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

@RequestMapping("order")
public class OrderController {
    private static Logger logger= (Logger) LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @RequestMapping(value="create.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes create(HttpSession session,Integer shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return orderService.createOrder(user.getUserId(),shipping);
    }

    //生成订单前，在购物车预览选中商品
    @RequestMapping(value="productsPreview.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes productsPreview(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return orderService.productsPreview(user.getUserId());
    }

    @RequestMapping(value="list.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes list(HttpSession session,
                                          @RequestParam(value = "pageNum",defaultValue = "1" )int pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "10" )int pageSize
                                          ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return orderService.listByKeyCategory(user.getUserId(),pageNum,pageSize);
    }

    @RequestMapping(value="datail.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes detail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return orderService.getDetailByOrderNo(user.getUserId(),orderNo);
    }

    @RequestMapping(value="cancel.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes cancel(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return orderService.cancelOrder(user.getUserId(),orderNo);
    }


}
