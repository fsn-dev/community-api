package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.dao.mongo.UserDao;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.service.UserService;

import java.util.List;

@Component
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名 密码查询用户
     *
     * @param username
     * @param password
     * @param active
     * @return
     */
    @Override
    public List<User> findByUsernameAndPasswordAndActive(String username, String password, Boolean active) {
        return userDao.findByUsernameAndPasswordAndActive(username, password, active);
    }


    @Override
    public List<User> findUserByGithubIdIs(String githubId, boolean active) {
        return userDao.findUserByGithubIdIs(githubId,active);
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }


    /**
     * @param emailCode
     * @return
     */
    @Override
    public List<User> findUserByRegisterEmailCodeIs(String emailCode) {
        return userDao.findUserByRegisterEmailCodeIs(emailCode);
    }

    /**
     * @param emailCode
     * @return
     */
    @Override
    public List<User> findUserByPasswordEmailCodeIs(String emailCode) {
        return userDao.findUserByPasswordEmailCodeIs(emailCode);
    }

    /**
     * @param emailCode
     * @return
     */
    @Override
    public List<User> findUserByMailboxIs(String emailCode) {
        return userDao.findUserByMailboxIs(emailCode);
    }

    @Override
    public List<User> findUserByUsernameIs(String name) {
        return userDao.findUserByUsernameIs(name);
    }

}
