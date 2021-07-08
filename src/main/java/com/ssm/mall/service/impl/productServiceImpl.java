package com.ssm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CategoryMapper;
import com.ssm.mall.dao.ProductMapper;
import com.ssm.mall.dao.pojo.Category;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.ProductDetailVo;
import com.ssm.mall.dao.vo.ProductListVo;
import com.ssm.mall.service.iservice.ICategoryService;
import com.ssm.mall.service.iservice.ProductService;
import com.ssm.mall.util.DateTimeUtil;
import com.ssm.mall.util.PropertyUtil;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;


@Service("productService")
public class productServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;

    @Override
    public ServerRes saveOrUpdate(Product product) {
        System.out.println("***************"+product);
       //将商品图片集合第一张图片选出，作为商品主题
        String imgs=product.getSubImage();
        int index=imgs.indexOf(",");
        String mainImg=imgs.substring(0,index);
        product.setMainImage(mainImg);

        //如果product已经存在，则更新，否则，新增
        if(productMapper.selectByPrimaryKey(product.getProductId())!=null) {
            int updateRows = productMapper.updateByPrimaryKeySelective(product);
            return updateRows > 0 ?
                    ServerRes.success(Result.UPDATE_PRODUCT_SUCCESS)
                    :ServerRes.error(Result.UPDATE_PRODUCT_ERROR);
        }else{
            int insertRows=productMapper.insert(product);
            return insertRows>0?
                    ServerRes.success(Result.INSERT_PRODUCT_SUCCESS)
                    :ServerRes.error(Result.INSERT_PRODUCT_ERROR);
        }

    }

    @Override
    public ServerRes setProductStatus(Integer id, Integer status) {
        Product product=new Product();
        product.setProductId(id);
        product.setStatus(status);
        int rows=productMapper.updateByPrimaryKeySelective(product);
        return rows>0?
                ServerRes.success(Result.UPDATE_STATUS_SUCCESS)
                :ServerRes.error(Result.UPDATE_STATUS_ERROR);
    }

    @Override
    public ServerRes<ProductDetailVo> getManagerDetail(Integer id) {
        Product product=productMapper.selectByPrimaryKey(id);
        if(product==null){
            return ServerRes.error(Result.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo vo =assembleProductDetailVo(product);
        return  ServerRes.success(Result.PRODUCT_DETAIL_SUCCESS,vo);
    }

    @Override
    public ServerRes getManagerList(int pageNum, int pageSize) {
        //PageHelper面向切面编程
        //1.PageHelper设置启动
        PageHelper.startPage(pageNum,pageSize);
        //2 查询
        List<Product> productList=productMapper.selectList();
        //3.使用PageInfo 封装分页后的结果，查询sql自动加入limit
        PageInfo pageResult=new PageInfo(productList);
        //4-组装vo
        List<ProductListVo> vos= Lists.newArrayList();
        for(Product product:productList){
            ProductListVo plv=assemblePructListVo(product);
            vos.add(plv);
        }
        //vo集合放入分页对象，替换原来的对象集合
        pageResult.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageResult);

    }

    @Override
    public ServerRes searchByIdName(Integer id, String name,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(name!=null){
            name=new StringBuilder().append("%").append(name).append("%").toString();

        }
        List<Product> productList=productMapper.searchByIdName(id,name);
        PageInfo pageResult=new PageInfo(productList);
        List<ProductListVo> vos= Lists.newArrayList();
        for(Product product:productList){
            ProductListVo plv=assemblePructListVo(product);
            vos.add(plv);
        }
        //vo集合放入分页对象，替换原来的对象集合
        pageResult.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageResult);
    }

    @Override
    public ServerRes<ProductDetailVo> getProductDetail(Integer id) {
        Product product=productMapper.selectByPrimaryKey(id);
        //前台获取商品详情，检查商品是否已经下架
        if(product==null||product.getStatus()!=Const.Product.ON_SALE){
            return ServerRes.error(Result.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo vo=assembleProductDetailVo(product);
        return ServerRes.success(Result.PRODUCT_DETAIL_SUCCESS,vo);
    }

    @Override
    public ServerRes<PageInfo> listByKeyCategory(
            String keywords, Integer categoryId, int pageNum, int pageSize,String orderBy) {
        if(StringUtils.isBlank(keywords) && categoryId == null){
            //keywords或categoryid二选一或都有，否则报错
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }

        // 对品类条件进行判断和处理(递归取出所有品类级别)
        List<Integer> categoryIds = Lists.newArrayList();
        if(categoryId != null){
            if(categoryMapper.selectByPrimaryKey(categoryId) == null){
                //根据品类id没有取出品类对象
                return ServerRes.error(Result.CATEGORY_NO_FOUND);
            }else{
                //如果Category品类传输来的是父级品类，例如手机，而手机品类下又分智能手机、普通手机等等
                //此时需要调用CategoryService中的递归显示品类列表的功能
                List<Category> categorys = (List<Category>) categoryService.getDeepCategory(categoryId).getData();
                for(Category category:categorys){
                    categoryIds.add(category.getCategoryId());
                }
            }
            System.out.println(categoryIds.toString());
        }

        //对关键字条件keyWords进行判断和处理
        if(StringUtils.isNotBlank(keywords)) {
            keywords = "%" + keywords + "%";
        }
        //如果查询条件正常,初始化分页公共模块
        PageHelper.startPage(pageNum,pageSize);
        //对动态排序条件orderBy进行判断和处理
        // 例如输入price_desc，则sql内就是price desc
        if(Const.Product.PRICE_ASC_DESC.contains(orderBy)){
            String[] orderBySQL = orderBy.split("_");
            PageHelper.orderBy(orderBySQL[0]+" "+orderBySQL[1]);
        }
        List<ProductListVo> vos = Lists.newArrayList();
        List<Product> products = productMapper.selectByKeywordsCategoryIds(
                StringUtils.isBlank(keywords)?null:keywords,
                categoryIds.size()==0?null:categoryIds);//对参数进行null值匹配
        PageInfo pageInfo = new PageInfo(products);//实现分页
        //组装VO，替换分页数据
        for(Product product:products){
            ProductListVo vo = assemblePructListVo(product);
            vos.add(vo);
        }
        pageInfo.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);

    }

    public ProductListVo assemblePructListVo(Product product) {
        ProductListVo  vo =new ProductListVo();
        vo.setId(product.getProductId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setSubtitle(product.getSubtitle());
        vo.setPrice(product.getPrice());
        vo.setMainImage(product.getMainImage());
        vo.setStatus(product.getStatus());
        vo.setImageHost(PropertyUtil.getProperty("ftp.server,http.prefix","http://img.happymmall.com"));
        return vo;

    }

    public ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo vo =new ProductDetailVo();
        vo.setId(product.getProductId());
        vo.setName(product.getName());
        vo.setStatus(product.getStatus());
        vo.setSubtitle(product.getSubtitle());
        vo.setDetail(product.getDetail());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCategoryId(product.getCategoryId());
        vo.setMainImage(product.getMainImage());
        vo.setSubImages(product.getSubImage());

        vo.setImageHost(PropertyUtil.getProperty("ftp.server,http.prefix","http://img.happymmall.com"));
        //获取上级目录id
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        vo.setParentCategoryId(category.getParentId());

        //时间
        String pattern = Const.STAND_DATETIME_FORMAT;
        vo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime(),pattern));
        vo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime(),pattern));
        return vo;
    }



}
