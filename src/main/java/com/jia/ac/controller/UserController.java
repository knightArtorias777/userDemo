
package com.jia.ac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jia.ac.BusinessException.BusinessException;
import com.jia.ac.common.BaseResponse;
import com.jia.ac.common.ErrorCode;
import com.jia.ac.common.ResultUtils;
import com.jia.ac.model.domain.User;
import com.jia.ac.model.domain.request.UserLoginRequest;
import com.jia.ac.model.domain.request.UserRegisterRequest;
import com.jia.ac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jia.ac.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author jialunyin
 * @version 1.0
 */
@RestController
//返回体都是json
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    private BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {

            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        long result =userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    private BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        log.info("登录成功");
        User user = userService.userLoginin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }


/*    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(User user,HttpServletRequest request){
        //1.输入参数检测
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        //
        Integer result = userService.updateUser(user);



        return ResultUtils.success(result);
    }*/
    /**
     * 判断是否登录
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();

        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    //搜索和删除应该设置权限检验 是管理员才可以用
    @PostMapping("/search")
    private BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {

        if (!userService.isManage(request)) {
            /*return ResultUtils.error(ErrorCode.PARAMS_NULL_ERROR);*/
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }

        List<User> list = userService.list(queryWrapper);
        return ResultUtils.success(list);

    }

    @PostMapping("/delete")
    private BaseResponse<Boolean> userDelete(long userId, HttpServletRequest request) {
        if (!userService.isManage(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = userService.removeById(userId);
        return ResultUtils.success(b);
    }


    /**
     * 退出账户 登录态抹除
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    private BaseResponse<Integer> userLoginout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        int userloginout = userService.userloginout(request);
        return ResultUtils.success(userloginout);
    }




}
