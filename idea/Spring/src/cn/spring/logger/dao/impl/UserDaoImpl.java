package cn.spring.logger.dao.impl;

import cn.spring.logger.dao.UserDao;
import cn.spring.logger.entity.User;

/**
 * Created by fic on 2019.03.30 10:08
 * Package:spring0330.cn.spring.logger.dao.impl
 * Desc:
 */
public class UserDaoImpl implements UserDao {
    @Override
    public void save(User user) {
        System.out.println("已将数据插入到数据库");
    }
}
