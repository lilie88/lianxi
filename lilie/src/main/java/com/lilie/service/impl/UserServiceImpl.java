package com.lilie.service.impl;

import com.lilie.common.Const;
import com.lilie.common.ServerResponse;
import com.lilie.dao.UserMapper;
import com.lilie.pojo.User;
import com.lilie.service.IUserService;
import com.lilie.util.MD5Util;

import com.lilie.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by geely
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    //    StringUtils需要导入commons-lang包
    @Autowired
    private UserMapper userMapper;

    //用户名登录密码
    @Override
    public ServerResponse<User> login(String username, String password) {
        //调用后台方法，先查询这个用户是否存在。如果存在，再进行用户名和密码的方法判断，最后再设置密码为空
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //到了这用户肯定存在，所以如果是空的，代表的是密码错误
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);//把密码设成一个空
        return ServerResponse.createBySuccess("登录成功", user);
    }


    //注册用户（和下面的方法是有联系的。），要先检验用户名和email是否存在，要设置用户的角色默认是普通用户，要进行密码的加密，然后插数据，
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //查询email，有dao层
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
//        设置用户默认为普通用户，0是普通用户
//        user中有role字段名，在Const中声明一个Role的接口类，定义属性普通用户和管理员。
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //校验注册，其实就是一个复用方法。和ServerResponse的性质一样。str是value值，type就是email或者username。str就是type对应的值，也就是用户添加的值
    public ServerResponse<String> checkValid(String str, String type) {
//        当type不为空
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //通过用户名查提示问题。
    // 先获取用户名，然后通过用户名进行查找。
    public ServerResponse selectQuestion(String username) {

        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
//        通过用户名查找提示问题
        String question = userMapper.selectQuestionByUsername(username);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    //校验提示问题答案是否正确
    //通过用户名，问题，和问题答案，校验问题的答案是否正确，和上面方法是有联系的。
    // 并且如果答案正确，就要给用户生成一个token。然后进行redis缓存设置。然后下面的方法重置密码中获取token
    //为什么一定要在这里得到用户的提示问题及答案，才能生成token。
    // 那是因为如果在登录时就生成token，那么修改密码，只需要获取登录时的状态就能修改密码，不安全。除非提供了提示问题及答案，才能修改密码
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题及问题答案是这个用户的,并且是正确的。UUID是java的
            String forgetToken = UUID.randomUUID().toString();
            //把token存到redis中，进行过期设置
            RedisPoolUtil.setEx(Const.TOKEN_PREFIX + username, forgetToken, 60 * 60 * 12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    //忘记密码，更改密码//
    // 忘记密码的重置密码，就需要token。因为token中含有用户名，问题，和问题答案，所以如果得到token，就能进行密码的重置。
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        //先判断用户名存在不
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
//        此用户名存在就得到token，判断token是否过期，如果没过期，就判断用户此时的forgetToken和得到的原来的token是否一致。
        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX + username);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
//           这一步token不为空，那么判断传过来的token和原来的token是否一致。如果一致，说明就是一个用户。就更改密码。
        if (org.apache.commons.lang3.StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    //登录状态下的更改密码，首先要验证旧密码，通过旧密码和用户的id查询，如果能查到才能修改新密码。
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),
        // 如果不指定id,name有很多用户的密码说不定都是这个都一样，那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
//        设置新的密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
//        更新用户，同时也就更新的用户的密码
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    //    更新用户信息，要通过email和指定用户的id，判断email是否存在。
    public ServerResponse<User> updateInformation(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
//        也就是说email不能重复，当查出来大于0，说明别人已经使用了，就不能再用了。
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();

        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setId(user.getId());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
//           更新用户信息
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }


    //    获取用户详细信息，通过id。
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        //如果用户登录，name制空密码
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }


    //backend

    /**
     * 校验是否是管理员
     *
     * @param user
     * @return
     */
//    分类管理下的校验用户是否是管理员
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
