package com.ssm.mall.action.console;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("managerCategory")
public class CategoryAdminController {
    @Autowired
    ICategoryService categoryService;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes addCategory(
            @RequestParam(value = "parentld", required = false, defaultValue = "0") Integer parentId, String categoryName,
            HttpSession session) {
        //权跟校验--Controller【权限是:已登录的管理员】
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        return categoryService.addCategory(parentId, categoryName);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes updateCategory(Integer categoryId, String categoryName, HttpSession session) {
        //验证是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        //检验用户权限
        if (user.getRole() != Const.Role.ADMIN) {
            return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
        }
        //执行更新操作
        return categoryService.updateCategory(categoryId, categoryName);
    }

    @RequestMapping(value = "children.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes getParallelCategories(Integer parentId) {
        return categoryService.getParallelCategories(parentId);
    }


}