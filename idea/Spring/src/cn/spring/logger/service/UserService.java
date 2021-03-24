package cn.spring.logger.service;

import cn.spring.logger.dao.UserDao;
import cn.spring.logger.entity.User;

/**
 * Created by fic on 2019.03.30 10:10
 * Package:spring0330.cn.spring.logger.service
 * Desc:
 */
public interface UserService {
    void addNewUser(User user);
}
