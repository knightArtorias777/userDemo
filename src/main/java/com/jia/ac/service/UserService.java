package com.jia.ac.service;

import com.jia.ac.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author 10926
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-03-04 14:55:05
 */
@Service
public interface UserService extends IService<User> {
    /**
     * 用户登录态键
     */

    /**
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码

     * @return userRegister userID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      用户登录态
     * @return 脱敏后的用户信息
     */
    User userLoginin(String userAccount, String userPassword, HttpServletRequest request);

    int userloginout(HttpServletRequest request);

    User getSafetyUser(User user);





    List<User> SerachByTag(List<String> tagsList);

    /**
     * 管理员权限检验
     * @param request
     * @return 是管理返回1不是返回0
     */
    boolean isManage(HttpServletRequest request);
}
