package com.jia.ac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.ac.BusinessException.BusinessException;
import com.jia.ac.Mapper.UserMapper;
import com.jia.ac.common.ErrorCode;
import com.jia.ac.model.domain.User;
import com.jia.ac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jia.ac.constant.UserConstant.MANAGE_ROLE;
import static com.jia.ac.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户实现类
 *
 * @author 10926
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-03-04 14:55:05
 */

@Service
@Slf4j

public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 盐值作为一个常量混淆加密密码规律
     */
    private static final String SALT = "sdgc";

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码
     * @return 返回一个脱敏的user容器 包含必要的信息
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户小于4位");
        }
        if (userPassword.length() < 4 || checkPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于4位");
        }


        //账户不能包括特殊字符
        //校验特殊字符的正则表达式
        String validPatten = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatten).matcher(userAccount);
        //匹配到特殊字符抛异常
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能包含特殊字符");
        }
        //用户不能重复的校验功能
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "userAccount已被占用");
        }
        //星球编号不能重复
  /*      queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planeCode", planeCode); //查询列planeCode有没有 存有一样的planeCode的数据
        count = userMapper.selectCount(queryWrapper);

        if (count > 0) {
            return -1;
        }*/
        //检验两个密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        //2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount); //如果用表增加约束处理 增加数据的时候抛出异常
        user.setUserPassword(encryptPassword);
        //插入成功 返回1,是不是需要一个事务 没必要
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "稍后请重试");
        }


        return user.getId();
    }

    /**
     * 第一次登录会记录登录态
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public User userLoginin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户小于4位");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于4位");
        }

        //账户不能包括特殊字符
        //校验特殊字符的正则表达式
        String validPatten = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPatten).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能包含特殊字符");
        }
        //用户不能重复的校验功能


        //2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在
        if (user == null) {
            log.info("user login fail,userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登陆失败用户名和密码可能错误请重试");
        }

        //3.用户脱敏
        User safetyUser = getSafetyUser(user);


        //4.记录用户的登录态 session绘画技术 包含用户信息需要脱敏 用键值对存储 要用用户信息直接读 包装在request对象中
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);


        return safetyUser;

    }

    @Override
    public int userloginout(HttpServletRequest request) {
        //退出要把登录态给注销了
        request.getSession().removeAttribute(USER_LOGIN_STATE);

        //要不要加一个确认remove成功的操作
        return 1;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 写一个根据标签查询 sql
     * @param
     * @return
     */
    @Override
    public List<User> SerachByTag(List<String> tagsList) {

        //查询有两种 方法一个是老办法 用sql查询
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        for(String tagsName :tagsList) {
            objectQueryWrapper =objectQueryWrapper.like("tag", tagsName);
        }


        Page<User> page = new Page<>(1, 10);
        //如果你的表有几十万的数据 你一次查几万出来有必要吗
/*
        IPage<User> userPage = userMapper.selectPage(page, objectQueryWrapper);
        List<User> records = userPage.getRecords();
*/
        //分页放入List集合
        List<User> records = userMapper.selectPage(page, objectQueryWrapper).getRecords();

        //用流处理把里面的User加密 在用集合工具类转换回去
        return records.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }



    /**
     * 检验有没有管理员权限
     * @param request
     * @return
     */
    @Override
    public boolean isManage(HttpServletRequest request) {
        //校验用户状态是不是管理员
        //!!!!!这块需要着重理解一下 代码调用逻辑
        Object objUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) objUser;
        //如果是管理员就返回ture 不是就false
        return user != null && user.getUserRole() == MANAGE_ROLE;
    }


}




