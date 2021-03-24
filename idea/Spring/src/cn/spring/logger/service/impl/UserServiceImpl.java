package cn.spring.logger.service.impl;

import cn.spring.logger.dao.UserDao;
import cn.spring.logger.entity.User;
import cn.spring.logger.service.UserService;

/**
 * Created by fic on 2019.03.30 10:11
 * Package:spring0330.cn.spring.logger.service.impl
 * Desc:
 */
public class UserServiceImpl implements UserService {
    private UserDao dao;

    //给Spring注入属性值使用
    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public void addNewUser(User user) {
        dao.save(user);
    }
}
