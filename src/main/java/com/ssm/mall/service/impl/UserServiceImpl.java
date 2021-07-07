package com.ssm.mall.service.impl;

import com.ssm.mall.common.Const;
import com.ssm.mall.util.GuavaCache;
import com.ssm.mall.util.MD5Util;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.UserMapper;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public ServerRes<User> login(String username, String password) {
//判断用户名是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerRes.error(Result.USERNAME_IS_NOT_EXIST);
        }//对密码进行MD5处理
        password = MD5Util.MD5EncodeUtf8(password);
        System.out.println("***************: " + password);
        //实现登录功能
        User user = userMapper.login(username, password);
        if (user == null) {
            return ServerRes.error(Result.PASSWORD_IS_WRONG);
        }//对取出的对象将password密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerRes.success(Result.LOGIN_SUCCESS, user);
    }

    @Override
    public ServerRes checkValid(String validName, String type) {
        return null;
    }

    @Override
    public ServerRes registry(User user) {
        //1-排除user中的username已存在

        int usernameFlag = userMapper.checkUsername(user.getUsername());

        if (usernameFlag > 0) {

            return ServerRes.error(Result.USER_ALREADY_EXIST);

        }

        //2-排除user中的email已存在

        int emailFlag = userMapper.checkEmail(user.getEmail());

        if (emailFlag > 0) {

            return ServerRes.error(Result.EMAIL_ALREADY_EXIST);

        }

        //3-如果username和email都不存在，则可以注册

        //密码的MD5加密需要处理

        String origin = user.getPassword();

        user.setPassword(MD5Util.MD5EncodeUtf8(origin));

        user.setRole(Const.Role.USER);//所有的注册用户，默认角色为USER

        int flag = userMapper.insertSelective(user);

        return flag > 0 ? ServerRes.success(Result.REGISTRY_SUCCESS) : ServerRes.error(Result.REGISTRY_ERROR);

    }

    @Override
    public ServerRes<String> getQuestionByUsername(String username) {
        //判断用户名是否存在
        int uFlag = userMapper.checkUsername(username);
        if (uFlag < 1) {
            return ServerRes.error(Result.USERNAME_IS_NOT_EXIST);
        } else {
            //如果存在，取出预设问题
            String quesstion = userMapper.getQuestionByUsername(username);
            if (StringUtils.isNotBlank(quesstion)) {
                return ServerRes.success(Result.RESULT_SUCCESS, quesstion);

            } else {
                return ServerRes.error(Result.NO_PASSWORD_RESET_QUESTION);
            }
        }
    }

    @Override

    public ServerRes<String> checkPreAnswer(String username, String question, String answer) {

        int paFlag = userMapper.checkPreAnswer(username, question, answer);

        if (paFlag > 0) {

            //如果预设答案一致，则生成token令牌，存入GUAVA缓存

            //UUID是通用唯一识别码(UniversallyUniqueIdentifier)的缩写，例如：9e3e3d65-77d0-4811-b0d3-77336bad8590

            String tokenValue = UUID.randomUUID().toString();

            GuavaCache.setTokenToCache(Const.TOKEN_PREFIX + username, tokenValue);

            return ServerRes.success(Result.RESULT_SUCCESS, tokenValue);

        }

        return ServerRes.error(Result.PASSWORD_RESET_ANSWER_ERROR);

    }

    @Override

    public ServerRes resetPassword(String username, String token, String newPassword) {

        //验证token令牌参数是否正常传递

        if (StringUtils.isBlank(token)) {

            return ServerRes.error(Result.NEED_TOKEN);

        }

        //验证用户名是否存在

        if (userMapper.checkUsername(username) < 1) {

            return ServerRes.error(Result.USERNAME_IS_NOT_EXIST);

        }

        //验证服务器GUAVA缓存中的TOKEN令牌是否已过期

        String serverToken = GuavaCache.getTokenFromCache(Const.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(serverToken)) {

            ServerRes.error(Result.TOKEN_EXPIRE);

        }

        //验证guava缓存中的令牌与客户提供的令牌是否一致

        if (StringUtils.equals(token, serverToken)) {

            //此处要先将密码进行MD5加密，然后更新原密码

            int resetFlag = userMapper.resetPassword(username, MD5Util.MD5EncodeUtf8(newPassword));

            if (resetFlag > 0) {

                return ServerRes.success(Result.PASSWORD_RESET_SUCCESS);

            }

        } else {

            return ServerRes.error(Result.TOKEN_ERROR);

        }

        return ServerRes.error(Result.PASSWORD_RESET_ERROR);

    }

    @Override
    public ServerRes modifyPassword(Integer id, String originPassword, String newPassword) {
        String userPwd = userMapper.getPasswordById(id);
        if (StringUtils.equals(userPwd, MD5Util.MD5EncodeUtf8(originPassword))) {
            int modifyFlag = userMapper.modifyPassword(id, MD5Util.MD5EncodeUtf8(newPassword));
            if (modifyFlag > 0) {
                return ServerRes.success(Result.MODIFY_PASSWORD_SUCCESS);
            } else {
                return ServerRes.error(Result.MODIFY_PASSWORD_ERROR);
            }
        } else {
            return ServerRes.error(Result.ORIGIN_PASSWORD_ERROR);
        }
    }

    @Override
    public ServerRes<User> getLoginUserInfo(Integer userid) {
        User user = userMapper.selectByPrimaryKey(userid);
        if (user == null) {
            return ServerRes.error(Result.USER_NOT_FOUND);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerRes.success(Result.RESULT_SUCCESS, user);
    }

    @Override
    public ServerRes<User> modifyLoginUserInfo(User newUser) {
        //业务要求，修改的email不能是其他用户已注册的邮箱地址，所以需要校验邮箱地址
        // 思考:为什么不能使用已有的checkEmail ?
        //如果邮箱并没有更改，该邮箱地址依然存在于数据库中，检测报错，需要排除对象本身的userid
        int eFlag = userMapper.checkEmailByUserId(newUser.getUserId(), newUser.getEmail());
        if (eFlag > 0) {
            return ServerRes.error(Result.EMAIL_ALREADY_EXIST);
        }
        int modifyUserFlag = userMapper.updateByPrimaryKey(newUser);
        if (modifyUserFlag < 1)
            return ServerRes.error(Result.MODIFY_USER_ERROR);
        return ServerRes.success(Result.RESULT_SUCCESS, newUser);


    }
}