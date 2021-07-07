package com.ssm.mall.action.console;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("managerProduct")
public class ProductAdminController {
    @Autowired
    ProductService productService;
    @ResponseBody
    @RequestMapping(value="save.do",method = RequestMethod.POST)
    public ServerRes save(HttpSession session,Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return productService.saveOrUpdate(product);
    }

    @ResponseBody
    @RequestMapping(value="update.do",method = RequestMethod.POST)
    public ServerRes update(HttpSession session,Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return productService.saveOrUpdate(product);
    }

    @RequestMapping(value="/updateStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRes updateProductStatus(HttpSession session,Integer id,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        if(id==null||status==null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return productService.setProductStatus(id,status);
    }

    @RequestMapping(value="/productDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRes getManagerDetail(HttpSession session,Integer id){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return productService.getManagerDetail(id);
    }

    @RequestMapping(value="/list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRes getManagerList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return productService.getManagerList(pageNum,pageSize);
    }

    //管理界面，根据id,name搜索商品(name为模糊查询)
    @RequestMapping(value="/search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRes getManagerList(HttpSession session,
                                    @RequestParam(value = "id",required = false) Integer id,
                                    @RequestParam(value = "name",required = false) String name,
                                    @RequestParam(value = "pageNum",defaultValue = "1" )int pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10" )int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return productService.searchByIdName(id, name,pageNum, pageSize);
    }

}
