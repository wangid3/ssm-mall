package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User login(@Param("username") String username,@Param("password") String password);

    int checkEmail(String email);

    String getQuestionByUsername(String username);

    int checkPreAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int resetPassword(@Param("username") String username,@Param("newPassword") String newPassword);

    String getPasswordById(Integer id);

    int modifyPassword(@Param("userid") Integer id,@Param("newPassword") String newPassword);

    int checkEmailByUserId(@Param("userid") Integer userId,@Param("email") String email);
}