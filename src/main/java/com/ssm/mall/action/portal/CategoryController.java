package com.ssm.mall.action.portal;


import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @RequestMapping(value = "children.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes getParallelCategories(Integer parentId) {
        return categoryService.getParallelCategories(parentId);
    }

    @RequestMapping(value = "deepCategory.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes deepCategory(Integer categoryId, HttpSession session) {
        //验证是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return categoryService.getDeepCategory(categoryId);

    }
}
