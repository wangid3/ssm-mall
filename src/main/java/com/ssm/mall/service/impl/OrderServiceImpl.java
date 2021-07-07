package com.ssm.mall.service.impl;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.*;
import com.ssm.mall.dao.pojo.*;
import com.ssm.mall.dao.vo.*;
import com.ssm.mall.service.iservice.OrderService;
import com.ssm.mall.util.BigDecimalUtil;
import com.ssm.mall.util.FTPUtil;
import com.ssm.mall.util.PropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.print.attribute.standard.OrientationRequested;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ShippingMapper shippingMapper;

    private static final Log log = LogFactory.getLog(OrderServiceImpl.class);

    @Override
    public ServerRes pay(Integer userId, Long orderNo,  String path) {

        //根据userId和orderNo获取订单信息
        Order order = orderMapper.selectByUseridAndOrderno(userId,orderNo);
        if(order == null){
            return ServerRes.error(Result.ORDER_NOT_FOUND);
        }
        //组装返回的ResultMap
        Map<String,Object> resultMap = Maps.newHashMap();
        resultMap.put("order_no",order.getOrderNo());//返回订单编号
        //获取支付宝支付信息，到演示程序Main拷贝main.test_trade_precreate()代码备用
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder()
                .append("苏格月商城扫码支付，订单编号：")
                .append(order.getOrderNo()).toString();
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,
        // 则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();//直接获取order订单的订单总价格payment
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";//非必填
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";//此处使用默认即可
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("您本次订单").append(outTradeNo)
                .append("共消费：").append(totalAmount).append("元").toString();
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计, 例如收银员7号
        String operatorId = "test_cashier_007";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        //一般用于连锁店经营
        String storeId = "test_substore_id";
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 商品信息GoodsDetail，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，
        List<Item> itemList = itemMapper.selectAllByUseridAndOrderno(userId,orderNo);
        for(Item item:itemList){
            // 创建一个商品信息，包含商品id（使用国标，类型String）、商品名称、商品价格（注意：单位为分，类型为Long）、商品数量
            GoodsDetail goodsDetail = GoodsDetail.newInstance(item.getProductId().toString(),item.getProductName(),
                    item.getCurrentUnitPrice().multiply(new BigDecimal("100.0")).longValue(),
                    item.getQuantity()
            );
            goodsDetailList.add(goodsDetail);
        }
        // 创建扫码支付请求builder，设置请求参数，重点在与setNotifyUrl方法中的参数，必须是有效域名，需要应用到内网穿透技术
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject(subject)
                .setTotalAmount(totalAmount).setOutTradeNo(outTradeNo).setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId).setBody(body).setOperatorId(operatorId).setStoreId(storeId)
                .setExtendParams(extendParams).setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径
                .setNotifyUrl(PropertyUtil.getProperty("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList);
        Configs.init("zfbinfo.properties");
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS://根据业务逻辑需要，应该将生成的二维码上传ftp文件服务器，并返回订单编号及支付码地址
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                System.out.println(response);//打印response，观察结果
                File webServerPath = new File(path);//在web服务器端，创建指定的二维码存放目录
                if(!webServerPath.exists()){
                    webServerPath.setWritable(true);
                    webServerPath.mkdir();
                }
                //组装支付二维码的图片存储地址和文件名,注意/的添加
                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);//应用ZxingUtils生成二维码
                //将二维码图片上传到FTP服务器上
                File targetFile = new File(path,qrFileName);
                FTPUtil.upload("", Lists.newArrayList(targetFile));
                String qrUrl = PropertyUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();//获得ftp地址
                resultMap.put("qrUrl",qrUrl);
                return ServerRes.success(Result.RESULT_SUCCESS,resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerRes.error(Result.ALIPAY_TRADE_FAILED);
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerRes.error(Result.ALIPAY_TRADE_STATE_UNKNOWN);
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerRes.error(Result.ALIPAY_TRADE_NOT_SUPPLY);
        }
    }

    @Transactional
    public ServerRes createOrder(Integer userId,Integer shippingId){
        //1-从购物车中获得所有被勾选的产品（productId，userId）
        List<Cart> cartList = cartMapper.selectCheckedCarts(userId);
        //2-将购物车Cart的选项，组装成订单详情Item
        if(CollectionUtils.isEmpty(cartList)){
            return ServerRes.error(Result.NO_CART_SELECTED);
        }
        //3-遍历购物车选项，检验状态是否在售，库存是否充足，然后进行订单详情组装
        List<Item> itemList = Lists.newArrayList();
        for(Cart cart:cartList){
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            Item item = new Item();
            item.setUserId(userId);
            item.setProductId(product.getProductId());
            item.setProductName(product.getName());
            item.setProductImage(product.getMainImage());
            item.setCurrentUnitPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            //单个商品总价，商品单价*购买个数
            item.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            itemList.add(item);
        }

        //计算订单总价（包含所有的订单详情）
        BigDecimal payment = new BigDecimal("0");
        for(Item item:itemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),item.getTotalPrice().doubleValue());
        }
        //组装订单Order对象
        Long orderNo = generateOrderNo();//生成订单编号
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setShippingId(shippingId);
        newOrder.setOrderNo(orderNo);
        newOrder.setStatus(Const.OrderStatus.ORDER_NO_PAY.getCode());//订单状态：未支付
        newOrder.setPaymentType(Const.PaymentType.ON_LINE.getCode());//支付方式：在线支付
        newOrder.setPostage(0);//全场包邮，快递系统的数据是结合第三方API实现，与支付宝类似
        newOrder.setPayment(payment);
        //发货时间和付款时间等，后续根据订单状态进行修改，此时填写null即可
        //插入订单
        int flag = orderMapper.insertSelective(newOrder);
        //重新查询订单，将createTime等属性读出
        Order order = orderMapper.selectByUseridAndOrderno(userId,newOrder.getOrderNo());
        if(flag < 1){
            return ServerRes.error(Result.ORDER_CREATE_FAILED);
        }
        //订单插入成功后，批量插入订单详情
        for(Item item:itemList){
            item.setOrderNo(order.getOrderNo());//向每一个订单详情中追加新生成的订单编号
        }
        int flags = itemMapper.batchInsertItems(itemList);
        if(flags < 1){
            return ServerRes.error(Result.ORDER_ITEMS_BATCH_INSERT_FAILED);
        }
        //订单详情批量插入成功后，对订单中的每一件商品减库存
        for(Item item:itemList){
            Product product = productMapper.selectByPrimaryKey(item.getProductId());
            product.setStock(product.getStock()-item.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        //最后，清除购物车中已经被生成订单的选项
        for(Cart cart:cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        //对生成的订单进行OrderVO信息组装
        //重新读取订单详情，取出createTime等属性
        List<Item> itemListResult = itemMapper.selectAllByUseridAndOrderno(userId,order.getOrderNo());
        OrderVO result = assembleOrderVO(order,itemListResult);
        return ServerRes.success(Result.ORDER_CREATE_SUCCESS,result);
    }

    @Override

    public ServerRes productsPreview(Integer userId) {
        ItemsPreviewVO itemsPreviewVO = new ItemsPreviewVO();
        //将购物车选中的商品信息集合，遍历封装成Item集合，此功能在创建订单时有代码可复用
        List<Cart> cartList = cartMapper.selectCheckedCarts(userId);
        List<ItemVO> itemVOList = Lists.newArrayList();
        for(Cart cart:cartList){
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(Const.Product.ON_SALE != product.getStatus()){
                return ServerRes.error(Result.PRODUCT_SALE_FAILED);
            }
            if(cart.getQuantity()>product.getStock()){
                return ServerRes.error(Result.STOCK_NOT_ENOUGH);
            }
            ItemVO item = new ItemVO();//生成购物车选项对应的订单详情
            item.setProductId(product.getProductId());
            item.setProductName(product.getName());
            item.setProductImage(product.getMainImage());
            item.setCurrentUnitPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setTotalPrice(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            itemVOList.add(item);
        }
        itemsPreviewVO.setItemVOList(itemVOList);
        //计算订单总价
        BigDecimal totlePrice = new BigDecimal("0");
        for(ItemVO itemVO:itemVOList){
            totlePrice = BigDecimalUtil.add(totlePrice.doubleValue(),itemVO.getTotalPrice().doubleValue());
        }
        itemsPreviewVO.setOrderTotalPrice(totlePrice);
        itemsPreviewVO.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix"));
        return ServerRes.success(Result.RESULT_SUCCESS,itemsPreviewVO);
    }

    @Override
    public ServerRes listByKeyCategory(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //2 查询
        List<Order> orderList=orderMapper.selectByUserid(userId);
        if(CollectionUtils.isEmpty(orderList)){
            return ServerRes.error(Result.HAVE_NO_ORDERS);
        }

        PageInfo pageInfo=new PageInfo(orderList);
        //组装OrderVO
        List<OrderVO> orderVOList =Lists.newArrayList();
        for(Order order:orderList){
            List<Item> itemList=itemMapper.selectAllByUseridAndOrderno(userId,order.getOrderNo());
            OrderVO orderVO=assembleOrderVO(order,itemList);
            orderVOList.add(orderVO);
        }
        pageInfo.setList(orderList);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);
    }

    @Override
    public ServerRes getDetailByOrderNo(Integer userId, Long orderNo) {
        List<Item> items=itemMapper.selectAllByUseridAndOrderno(userId,orderNo);
        return ServerRes.success(Result.RESULT_SUCCESS,items);
    }

    @Override
    public ServerRes getDetailByOrderNo(Long orderNo) {
        Order order=orderMapper.selectByOrderno(orderNo);
        if(order==null){
            return ServerRes.error(Result.RESULT_ERROR);
        }
        List<Item> itemList=itemMapper.selectAllByOrderNo(orderNo);
        OrderVO orderVO=assembleOrderVO(order,itemList);
        Shipping shipping=shippingMapper.selectByPrimaryKey(order.getShippingId());
        orderVO.setReceiverName(shipping.getReceiverId());
        ShippingVO sv=assembleShippingVO(shipping);
        orderVO.setShippingVO(sv);
        return ServerRes.success(Result.RESULT_SUCCESS,orderVO);
    }

    @Override
    public ServerRes cancelOrder(Integer userId, Long orderNo) {
        Order order=orderMapper.selectByUseridAndOrderno(userId,orderNo);
        if(order==null){
            ServerRes.error(Result.ORDER_NOT_FOUND);
        }
        if(order.getStatus()!=Const.OrderStatus.ORDER_NO_PAY.getCode()){
            ServerRes.error(Result.ORDER_CANCEL_FAILED_ALEADY_PAY);
        }
        Order now=new Order();
        now.setId(order.getId());
        now.setStatus(Const.OrderStatus.ORDER_CANCLE.getCode());
        int flag=orderMapper.updateByPrimaryKeySelective(now);
        return flag>0?
                ServerRes.success(Result.RESULT_SUCCESS)
                :ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes mlist(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList= orderMapper.selectAll();
        if(CollectionUtils.isEmpty(orderList)){
            return ServerRes.error(Result.RESULT_ERROR);
        }
        PageInfo pageInfo=new PageInfo(orderList);
        //装配OrderVO对象
        List<OrderVO> result=Lists.newArrayList();
        for(Order order:orderList){
            List<Item> itemList=itemMapper.selectAllByOrderNo(order.getOrderNo());
            OrderVO orderVO=assembleOrderVO(order,itemList);
            result.add(orderVO);
        }
        pageInfo.setList(result);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);
    }

    @Override
    public ServerRes msearch(SearchVO sv, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList= orderMapper.msearch(sv);
        if(CollectionUtils.isEmpty(orderList)){
            return ServerRes.error(Result.RESULT_ERROR);
        }
        PageInfo pageInfo=new PageInfo(orderList);
        List<OrderVO> result=Lists.newArrayList();
        for(Order order:orderList){
            List<Item> itemList=itemMapper.selectAllByOrderNo(order.getOrderNo());
            OrderVO orderVO=assembleOrderVO(order,itemList);
            result.add(orderVO);
        }
        pageInfo.setList(result);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);
    }

    @Override
    public ServerRes updateOrderStatusSend(Long orderNo) {
        Order order =orderMapper.selectByOrderno(orderNo);
        if(order==null){
            return ServerRes.error(Result.RESULT_ERROR);
        }
        if(order.getStatus()!=Const.OrderStatus.ORDER_ALREADY_PAY.getCode()){
            return ServerRes.error(Result.ORDER_NOT_PAY);
        }
        Order newOrder =new Order();
        newOrder.setId(order.getId());
        newOrder.setStatus(order.getStatus());
        newOrder.setStatus(Const.OrderStatus.ORDER_ALREADY_SEND.getCode());
        orderMapper.updateByPrimaryKeySelective(newOrder);
        return ServerRes.success(Result.RESULT_SUCCESS);
    }

    private OrderVO assembleOrderVO(Order order,List<Item> itemList){
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPaymentTypeDesc(Const.PaymentType.getMsgByCode(order.getPaymentType()));
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setStatusDesc(Const.OrderStatus.getMsgByCode(order.getStatus()));
        orderVO.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderVO.setReceiverName(shipping.getReceiverId());
            ShippingVO sv = assembleShippingVO(shipping);
            orderVO.setShippingVO(sv);
        }
        orderVO.setPaymentTime(order.getPaymentTime());
        orderVO.setSendTime(order.getSendTime());
        orderVO.setEndTime(order.getEndTime());
        orderVO.setCloseTime(order.getCloseTime());
        orderVO.setCreateTime(order.getCreateTime());
        orderVO.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix"));
        List<ItemVO> itemVOList = Lists.newArrayList();
        for(Item item:itemList){
            ItemVO itemVO = new ItemVO();
            itemVO.setCreateTime(item.getCreateTime());
            itemVO.setCurrentUnitPrice(item.getCurrentUnitPrice());
            itemVO.setOrderNo(item.getOrderNo());
            itemVO.setProductId(item.getProductId());
            itemVO.setProductImage(item.getProductImage());
            itemVO.setProductName(item.getProductName());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setTotalPrice(item.getTotalPrice());
            itemVOList.add(itemVO);
        }
        orderVO.setItemVOList(itemVOList);
        return orderVO;
    }
    //组装ShippingVO
    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO sv = new ShippingVO();
        sv.setReceiverAddress(shipping.getReceiverAddress());
        sv.setReceiverCity(shipping.getReceiverCity());
        sv.setReceiverDistrict(shipping.getReceiverDistrict());
        sv.setReceiverMobile(shipping.getReceiverMobile());
        sv.setReceiverName(shipping.getReceiverId());
        sv.setReceiverPhone(shipping.getReceiverPhone());
        sv.setReceiverProvince(shipping.getReceiverProvince());
        sv.setReceiverZip(shipping.getReceiverZip());
        return sv;
    }
    /**
     * 订单编号的生成非常重要，如果是自增方式，通过订单号就可以知道网站的运行情况
     * 另外，如果可以在订单中加入一定的信息，分布式时，可以便捷的进行不同服务器的请求和查询
     * 此外，还需要考虑高并发的时候，有可能提交的订单是同时产生，此时该如何处理
     * （特别高并发情况下的处理方案：编写定时任务，夜间在缓存中生成第二天要使用的订单号，
     * 然后启动守护线程，根据订单编号的使用情况，向缓存中添加订单号【线程通讯的生产生消费者模型】）
     * * @return
     */
    private long generateOrderNo(){
        //以下以时间戳+随机数的方式简单演示
        return System.currentTimeMillis()+ new Random().nextInt(1000);
    }
}
