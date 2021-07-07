package com.ssm.mall.action.console;


import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class AdminAction {
    @Autowired
    UserService userService;
    @RequestMapping(value="login.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> login(String username, String password, HttpSession session){
        ServerRes<User> sr=userService.login(username,password);
        User user=sr.getData();
        if(user!=null){
            Integer role= user.getRole();
            if(role==Const.Role.ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);

            }else{
                return ServerRes.error(Result.ADMIN_LOGIN_ERROR);
            }

        }
        return sr;
    }

}
