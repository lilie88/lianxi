package com.lilie.dao;

import com.lilie.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(User record);

    //通过用户名查找 是否有该用户名
    int checkUsername(String username);

    //    然后再通过同户名和密码查找用户是否存在
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //查找邮箱是否存在
    int checkEmail(String email);


    //通过用户名查找提示问题。
    String selectQuestionByUsername(String username);

    //     查找用户提示问题答案是否正确
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    //     忘记密码时的修改密码
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //     登录状态下的修改密码，这是通过id和旧密码查询用户，是否存在。存在的话，才能修改新密码
    int checkPassword(@Param(value = "password") String password, @Param("userId") Integer userId);

    //     登录状态下的修改密码
    int updateByPrimaryKeySelective(User record);

    // <!--通过id查找email是否存在-->
    int checkEmailByUserId(@Param(value = "email") String email, @Param(value = "userId") Integer userId);
}