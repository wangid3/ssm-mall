package com.ssm.mall.service.iservice;


import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Test
    public void login() {
        ServerRes<User> res1  = userService.login("scott","DE6D76FE7C40D5A1A8F04213F2BEFBEE");
        System.err.println("***********************"+res1);
        ServerRes<User> res2  = userService.login("scott11","tiger");
        System.err.println("***********************"+res2);
        ServerRes<User> res3  = userService.login("scott","tiger11");
        System.err.println("***********************"+res3);

    }

    @Test

    public void regist() {

        User user = new User("qian1111","123","qian111@mall.com","12345678911","qu","an");

        ServerRes sr =  userService.registry(user);

        System.err.println(sr);

    }

    @Test
    public void checkPreAnswer(){
        System.out.println(userService.checkPreAnswer("scott","qu","an"));
        System.out.println(userService.checkPreAnswer("qian1111","qu","an"));
    }


}