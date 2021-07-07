package com.ssm.mall.action.portal;


import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.vo.ProductDetailVo;
import com.ssm.mall.service.iservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductService productService;

    @ResponseBody
    @RequestMapping("detail.do")
    public ServerRes<ProductDetailVo> getDetail(Integer id){
        return productService.getProductDetail(id);
    }

    @ResponseBody
    @RequestMapping("list.do")
    public ServerRes<PageInfo> listByKeyCategory(HttpSession session,
                                              @RequestParam(value = "keywords",required = false) String keywords,
                                              @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                              @RequestParam(value = "pageNum",defaultValue = "1" )int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10" )int pageSize,
                                              @RequestParam(value = "orderBy",defaultValue = "") String orderBy) {

        return productService.listByKeyCategory(keywords,categoryId,pageNum,pageSize,orderBy);


    }
}
