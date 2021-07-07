package com.ssm.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CartMapper;
import com.ssm.mall.dao.ProductMapper;
import com.ssm.mall.dao.pojo.Cart;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.CartItemVo;
import com.ssm.mall.dao.vo.CartVo;
import com.ssm.mall.service.iservice.CartService;
import com.ssm.mall.util.DecimalUtil;
import com.ssm.mall.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerRes<CartVo> list(Integer userId) {
        CartVo cartVo=getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,cartVo);
    }

    @Override
    public ServerRes<CartVo> addProduct(Integer userId, Integer productId, Integer num) {
        //处理商品插入
        Cart item=cartMapper.selectByUseridAndProduct(userId,productId);
        if(item==null){//购物车中没有该商品
            Cart newItem=new Cart(userId,productId,num,Const.Cart.CHECKED);
            cartMapper.insert(newItem);
        }else{//已经存在该商品
            Integer newNum=item.getQuantity()+num;
            item.setQuantity(newNum);
            cartMapper.updateByPrimaryKeySelective(item);
        }
        //取出购物车所有商品，返回
        CartVo cartVo=getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,cartVo);
    }

    @Override
    public ServerRes<CartVo> updateProductNum(Integer userId, Integer productId, Integer num) {
        //更新商品数量
        Cart item=cartMapper.selectByUseridAndProduct(userId,productId);
        if(item!=null){//购物车中没有该商品
            item.setQuantity(num);
            cartMapper.updateByPrimaryKeySelective(item);
        }
        //取出所有商品，返回
        CartVo cartVo=getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,cartVo);
    }

    @Override
    public ServerRes<CartVo> deleteProductBatch(Integer userId, String productIds) {
        List<String> ids= Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(ids)){
            ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        //批量删除商品
        int result=cartMapper.deleteProductByUserid(userId,ids);
        //取出购物车所有商品，返回
        CartVo cartVo=getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,cartVo);
    }

    @Override
    public ServerRes<CartVo> updateAllCheck(Integer userId, Integer checked) {
        cartMapper.updateAllCheck(userId,checked);
        CartVo cartVo=getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,cartVo);
    }

    @Override
    public ServerRes<CartVo> updateProductCheck(Integer userId, Integer productId, Integer checked) {
        cartMapper.updateUpdateProductCheck(userId,productId,checked);
        return this.list(userId);
    }

    @Override
    public ServerRes<Integer> productCount(Integer userId) {
        Integer count =cartMapper.productCount(userId);
        return ServerRes.success(Result.RESULT_SUCCESS,count);
    }

    //工具方法：根据用户id获取购物车信息（CartVo和CartItemVo的拼装）

    private CartVo getCartVoByUserid(Integer userId) {
        //1-首先根据userId取出购物车所有信息
        List<Cart> cartList = cartMapper.selectByUserid(userId);
        //2-装配CartItemVo和CartVo
        CartVo cartVo = new CartVo();
        List<CartItemVo> cartItemVoList = Lists.newArrayList();
        if (cartList != null && cartList.size() > 0) {
            for (Cart cart : cartList) {
                CartItemVo cartItemVo = new CartItemVo();
                //装配购物车信息
                cartItemVo.setId(cart.getId());
                cartItemVo.setProductId(cart.getProductId());
                cartItemVo.setUserId(cart.getUserId());
                cartItemVo.setChecked(cart.getChecked());//是否被选中
                //装配产品信息
                Integer productId = cart.getProductId();
                Product product = productMapper.selectByPrimaryKey(productId);
                if (product != null) {
                    cartItemVo.setProName(product.getName());
                    cartItemVo.setProSubtitle(product.getSubtitle());
                    cartItemVo.setProMainImage(product.getMainImage());
                    cartItemVo.setProStatus(product.getStatus());
                    cartItemVo.setProPrice(product.getPrice());
                    cartItemVo.setProStock(product.getStock());
                    //根据商品库存，装配购物车中该商品的购买数量（最大为库存量）
                    int stock = product.getStock();
                    int quantity = cart.getQuantity();
                    if (quantity <= stock) {//库存充足
                        cartItemVo.setLimitQuantity(Const.Cart.QUANTITY_SUCCESS);
                    } else {//库存不足
                        cartItemVo.setLimitQuantity(Const.Cart.QUANTITY_OUT_OF_STOCK);
                        //将购物车选项中购买数量更新为库存数量
                        cart.setQuantity(stock);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartItemVo.setQuantity(cart.getQuantity());//装配购买数量
                    //计算该商品的结算总价
                    BigDecimal cartItemTotalPrice = BigDecimal.valueOf(DecimalUtil.mul(product.getPrice().doubleValue(), cartItemVo.getQuantity()));
                    cartItemVo.setCartItemTotalPrice(cartItemTotalPrice);
                }
                cartItemVoList.add(cartItemVo);
            }
        }
        //装配整个购物车
        cartVo.setCartItemList(cartItemVoList);
        //计算整个购物车的结账总金额,以及判断是否被全部选中
        Boolean allChecked = true;
        BigDecimal cartTotalPrice = new BigDecimal("0");
        for (CartItemVo civ : cartItemVoList) {
            if (civ.getChecked() == Const.Cart.CHECKED) {//如果商品被选中
                cartTotalPrice = BigDecimal.valueOf(DecimalUtil.add(cartTotalPrice.doubleValue(), civ.getCartItemTotalPrice().doubleValue()));
            } else {
                allChecked = false;
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(allChecked);//设置是否被全选
        //装配FTP文件服务器地址
        cartVo.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return cartVo;
    }
}
