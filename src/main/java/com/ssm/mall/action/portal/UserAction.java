package com.ssm.mall.action.portal;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("user")
public class UserAction {
    @Autowired
    UserService userService;
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public @ResponseBody  ServerRes<User> login(
            @RequestParam String username,
            @RequestParam(value = "password",required = true) String password, HttpSession session){
        ServerRes<User> result = userService.login(username,password);
        if(result.getStatus() == Result.LOGIN_SUCCESS.getCode()){
            session.setAttribute(Const.CURRENT_USER,result.getData());
            System.out.println("result.getStatus: "+result.getStatus());
        }
        return result;
    }

    //1.2注销

    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    public @ResponseBody ServerRes logout(HttpSession session){

        session.removeAttribute(Const.CURRENT_USER);
        return ServerRes.success(Result.LOGOUT_SUCCESS);

    }

    //1.3用户注册
    @RequestMapping(value = "regist.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes registUser(User user){

        return userService.registry(user);

    }

    //1.4获取已登录的用户信息

    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> getLoginedUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            //如果session中存在CURRENT_USER，表示用户已登录，返回session中的用户信息即可
            return ServerRes.success(Result.RESULT_SUCCESS,user);
        }
        return ServerRes.error(Result.NEED_LOGIN);

    }

    //1.5忘记密码，根据用户名获得密码重置的预设问题
    @RequestMapping(value="getResetQuestion.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<String> getPasswordResetQusetion(String username){
        return userService.getQuestionByUsername(username);
    }

    //1.6根据用户名，预设问题检验预设答案
    @RequestMapping(value = "checkPreAnswer.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<String> checkPreAnswer(String username,String question,String answer){
        return userService.checkPreAnswer(username,question,answer);
    }

    //1.7根据Token令牌和用户名重置用户密码
    @RequestMapping(value="resetPassword.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes resetPassword(String username,String token,String newPassword){
        return userService.resetPassword(username,token,newPassword);
    }

    //1.8登录状态下，对用户密码进行修改(需要输入原密码）
    @RequestMapping(value = "modifyPassword.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes modifyPassword(
            String originPassword,//原密码
            String newPassword,//新密码
            HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if( user == null){
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return userService.modifyPassword(user.getUserId(),originPassword,newPassword);
    }

    //1.9获取登录用户信息
    @RequestMapping(value = "getLoginUserInfo.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> getLoginUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return userService.getLoginUserInfo(user.getUserId());
    }


    //1.10修改用户信息
    @RequestMapping(value="modifyUserInfo.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> modifyUserInfo(User newUser, HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerRes.error(Result.NEED_LOGIN);
        }
        newUser.setUserId(user.getUserId());
        newUser.setUsername(user.getUsername());
        ServerRes<User> sr=userService.modifyLoginUserInfo(newUser);
        if(sr.getStatus()==Result.RESULT_SUCCESS.getCode()){
            session.setAttribute(Const.CURRENT_USER,newUser);
        }
        return sr;
    }


}
