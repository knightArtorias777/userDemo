package com.jia.ac.service;

import com.jia.ac.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jialunyin
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testAdduser() {
        User user = new User();
        user.setUsername("yinshier");
        user.setUserAccount("yin12");
        user.setAvatarUrl("https://lh3.googleus ercontent.com/a/ACg8ocIbj7wcyo5cQuM1fF3gRM2huR_rX3yOZU_cTAWJayxf=k-s256");
        user.setGender(0);
        user.setUserPassword("123123");
        user.setPhone("132121");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "wang12";
        String userPassword = "123123";
        String checkPassword = "123123";
/*        userService.userRegister(userAccount, userPassword, checkPassword,planeCode);*/
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

    }

    @Test
    void userloginout() {
    }
    @Test
    public void Test01(){

        User user = new User();
        System.out.println(user.hashCode());


    }
}